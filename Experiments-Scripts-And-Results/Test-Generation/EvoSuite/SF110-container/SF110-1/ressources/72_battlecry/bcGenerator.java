package bcry;

import java.util.*;

class bcGenerator
{
    private final String GRAMMAR_START = "START";
    private final String GRAMMAR_TITLE = "TITLE";
    private Random r;
    private int currentSyllables;
    private String rSeg;
    private bcModule module;
    private bcVoice voice;
    
    public bcGenerator(final bcModule m, final bcVoice v) {
        this.r = new Random();
        this.currentSyllables = 0;
        this.rSeg = "";
        this.voice = v;
        this.module = m;
    }
    
    private String parseGrammar(final String segment) {
        String output = "";
        try {
            final String seg = this.replaceBrackets(this.module.getGrammar().getProperty(segment, segment));
            String[] temp;
            if (seg.indexOf(";") == -1) {
                temp = new String[] { seg.substring(1, seg.length() - 1) };
            }
            else {
                temp = seg.substring(1, seg.length() - 1).split(";");
            }
            for (int i = 0; i < temp.length; ++i) {
                if (temp[i].indexOf("#") != -1) {
                    final String[] temp2 = temp[i].split("#");
                    temp[i] = temp2[this.r.nextInt(temp2.length)];
                }
                if (temp[i].startsWith("(") && temp[i].endsWith(")")) {
                    temp[i] = temp[i].replace('&', ';');
                    temp[i] = temp[i].replace('|', '#');
                    temp[i] = "{" + temp[i].substring(1, temp[i].length() - 1) + "}";
                    output += this.parseGrammar(temp[i]);
                    if (i < temp.length - 1) {
                        output += "@";
                    }
                }
                else if (temp[i].startsWith("*(") && temp[i].endsWith(")")) {
                    if (this.r.nextInt(2) == 1) {
                        temp[i] = temp[i].replace('&', ';');
                        temp[i] = temp[i].replace('|', '#');
                        temp[i] = "{" + temp[i].substring(2, temp[i].length() - 1) + "}";
                        output += this.parseGrammar(temp[i]);
                        if (i < temp.length - 1) {
                            output += "@";
                        }
                    }
                }
                else if (temp[i].startsWith("'") && temp[i].endsWith("'")) {
                    if (this.isWord(temp[i].substring(1, temp[i].length() - 1))) {
                        this.currentSyllables += this.getWordFromLists(temp[i].substring(1, temp[i].length() - 1)).getSyllables();
                    }
                    output += temp[i];
                    if (i < temp.length - 1) {
                        output += "@";
                    }
                }
                else if (temp[i].startsWith("*'") && temp[i].endsWith("'")) {
                    if (this.r.nextInt(2) == 1) {
                        if (this.isWord(temp[i].substring(2, temp[i].length() - 1))) {
                            this.currentSyllables += this.getWordFromLists(temp[i].substring(2, temp[i].length() - 1)).getSyllables();
                        }
                        output += temp[i].substring(1, temp[i].length());
                        if (i < temp.length - 1) {
                            output += "@";
                        }
                    }
                }
                else if (temp[i].startsWith("[") && temp[i].endsWith("]")) {
                    output += this.parseGrammar(temp[i].substring(1, temp[i].length() - 1));
                    if (i < temp.length - 1) {
                        output += "@";
                    }
                }
                else if (temp[i].startsWith("*[") && temp[i].endsWith("]")) {
                    if (this.r.nextInt(2) == 1) {
                        output += this.parseGrammar(temp[i].substring(2, temp[i].length() - 1));
                        if (i < temp.length - 1) {
                            output += "@";
                        }
                    }
                }
                else if (temp[i].startsWith("*")) {
                    if (this.r.nextInt(2) == 1) {
                        output += temp[i].substring(1, temp[i].length());
                        if (i < temp.length - 1) {
                            output += "@";
                        }
                    }
                }
                else {
                    output += temp[i].substring(0, temp[i].length());
                    if (i < temp.length - 1) {
                        output += "@";
                    }
                }
            }
        }
        catch (Exception e) {
            this.voice.sysout("Error: Missing or illegal expression in grammar.dat - Segment:" + segment);
            this.voice.sysout(e.toString());
        }
        if (output.endsWith("@")) {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }
    
    private void parseLayout() {
        String[] chorus = null;
        for (int i = 0; i < this.module.getLayout().size(); ++i) {
            if (this.module.getLayout().get(i).indexOf(":") != -1) {
                final String[] temp = this.module.getLayout().get(i).split(":");
                if (temp[0].equals("lines")) {
                    final int lines = Integer.parseInt(temp[1]);
                    this.voice.sing(this.makeLines(lines));
                }
                else if (temp[0].equals("verse")) {
                    this.voice.sing(this.makeChorus(temp[1]));
                }
                else if (temp[0].equals("chorus")) {
                    this.voice.sing("[Chorus]");
                    if (chorus == null) {
                        chorus = this.makeChorus(temp[1]);
                    }
                    this.voice.sing(chorus);
                }
                else if (temp[0].equals("bridge")) {
                    final int lines = Integer.parseInt(temp[1]);
                    this.voice.sing(this.makeBridge(lines));
                }
            }
            else {
                this.voice.sing(this.fill(this.parseGrammar(this.module.getLayout().get(i))));
            }
        }
    }
    
    private String[] xLiner(int number) {
        if (number <= 2) {
            number = 2;
        }
        else {
            number = 3;
        }
        final String[] result = new String[number];
        final String[] lastSegs = new String[number - 1];
        String lastWord = "";
        String tempWord = "";
        final int[] tempSyls = new int[number];
        int tempSyl = 0;
        bcWordList lst = null;
        bcWord current = null;
        for (int l = 0; l <= number - 2; ++l) {
            boolean match;
            do {
                match = true;
                if (l == 0) {
                    result[this.currentSyllables = 0] = this.fill(this.parseGrammar("START"));
                    lastWord = this.getLastWord(result[0]);
                    tempSyls[0] = this.currentSyllables;
                    this.currentSyllables = 0;
                }
                else {
                    lastWord = this.getLastWord(result[1]);
                }
                result[l + 1] = this.parseGrammar("START");
                if (result[l + 1].indexOf("@") == -1) {
                    lastSegs[l] = result[l + 1];
                    result[l + 1] = "";
                    tempSyls[l + 1] = (this.currentSyllables = 0);
                }
                else {
                    for (int j = result[l + 1].length() - 1; j >= 0; --j) {
                        if (result[l + 1].charAt(j) == '@') {
                            lastSegs[l] = result[l + 1].substring(j + 1);
                            result[l + 1] = this.fill(result[l + 1].substring(0, j));
                            tempSyls[l + 1] = this.currentSyllables;
                            this.currentSyllables = 0;
                            break;
                        }
                    }
                }
                if (lastSegs[l].startsWith("'") && lastSegs[l].endsWith("'")) {
                    tempWord = lastSegs[l].substring(1, lastSegs[l].length() - 1);
                    tempSyl = this.getWordFromLists(tempWord).getSyllables();
                    if (this.rhyming(tempWord, lastWord) < 1 || tempSyl + tempSyls[l + 1] > tempSyls[0] + this.module.getSyllableTolerance() || tempSyl + tempSyls[l + 1] < tempSyls[0] - this.module.getSyllableTolerance()) {
                        match = false;
                    }
                    else {
                        final StringBuilder sb = new StringBuilder();
                        final String[] array = result;
                        final int n = l + 1;
                        array[n] = sb.append(array[n]).append(" ").append(tempWord).toString();
                        final int[] array2 = tempSyls;
                        final int n2 = l + 1;
                        array2[n2] += tempSyl;
                    }
                }
                else {
                    lst = this.module.getWordList(lastSegs[l]);
                    final List possibleMatches = new LinkedList();
                    for (int i = 0; i < lst.getNumberOfWords(); ++i) {
                        current = lst.getItem(i);
                        tempWord = current.getWord();
                        if (this.rhyming(tempWord, lastWord) == 1 && current.getSyllables() + tempSyls[l + 1] <= tempSyls[0] + this.module.getSyllableTolerance() && current.getSyllables() + tempSyls[l + 1] >= tempSyls[0] - this.module.getSyllableTolerance()) {
                            possibleMatches.add(current);
                        }
                    }
                    if (possibleMatches.size() == 0) {
                        match = false;
                    }
                    else {
                        current = possibleMatches.get(this.r.nextInt(possibleMatches.size()));
                        tempWord = current.getWord();
                        final StringBuilder sb2 = new StringBuilder();
                        final String[] array3 = result;
                        final int n3 = l + 1;
                        array3[n3] = sb2.append(array3[n3]).append(" ").append(tempWord).toString();
                        final int[] array4 = tempSyls;
                        final int n4 = l + 1;
                        array4[n4] += current.getSyllables();
                    }
                }
                if (this.module.getSyllableTolerance() == 0 && !result[l + 1].equals("") && !this.metricMatch(result[0], result[l + 1])) {
                    match = false;
                }
            } while (!match);
        }
        return result;
    }
    
    private String[] makeBridge(int lines) {
        final String[] result = new String[lines];
        if (lines % 2 != 0) {
            ++lines;
        }
        String[] temp;
        for (int i = 0; i < lines; ++i, result[i] = temp[1], ++i) {
            temp = this.xLiner(2);
            result[i] = temp[0];
        }
        return result;
    }
    
    private String[] makeChorus(String scheme) {
        final String[] result = new String[scheme.length()];
        final String[][] schemes = new String[5][3];
        scheme = scheme.toLowerCase();
        final int[] count = new int[5];
        for (int i = 0; i < 5; ++i) {
            schemes[i] = new String[3];
            for (int j = 0; j < 3; ++j) {
                schemes[i][j] = "";
            }
            count[i] = 0;
        }
        for (int i = 0; i < scheme.length(); ++i) {
            if (scheme.substring(i).startsWith("a")) {
                final int[] array = count;
                final int n = 0;
                ++array[n];
            }
            else if (scheme.substring(i).startsWith("b")) {
                final int[] array2 = count;
                final int n2 = 1;
                ++array2[n2];
            }
            else if (scheme.substring(i).startsWith("c")) {
                final int[] array3 = count;
                final int n3 = 2;
                ++array3[n3];
            }
            else if (scheme.substring(i).startsWith("d")) {
                final int[] array4 = count;
                final int n4 = 3;
                ++array4[n4];
            }
            else if (scheme.substring(i).startsWith("e")) {
                final int[] array5 = count;
                final int n5 = 4;
                ++array5[n5];
            }
        }
        boolean rhyming;
        do {
            rhyming = false;
            for (int i = 0; i < 5; ++i) {
                if (count[i] == 1) {
                    schemes[i][0] = this.fill(this.parseGrammar("START"));
                }
                else if (count[i] >= 2) {
                    schemes[i] = this.xLiner(count[i]);
                }
                for (int j = 0; j <= i; ++j) {
                    if (i != j && (this.rhyming(this.getLastWord(schemes[i][0]), schemes[j][0]) == 1 || this.rhyming(this.getLastWord(schemes[i][0]), schemes[j][0]) == -2)) {
                        rhyming = true;
                    }
                }
            }
            this.voice.progressBarDot(".");
        } while (rhyming);
        for (int i = 0; i < 5; ++i) {
            count[i] = 0;
        }
        for (int i = 0; i < scheme.length(); ++i) {
            if (scheme.substring(i).startsWith("a")) {
                result[i] = schemes[0][count[0]];
                final int[] array6 = count;
                final int n6 = 0;
                ++array6[n6];
            }
            else if (scheme.substring(i).startsWith("b")) {
                result[i] = schemes[1][count[1]];
                final int[] array7 = count;
                final int n7 = 1;
                ++array7[n7];
            }
            else if (scheme.substring(i).startsWith("c")) {
                result[i] = schemes[2][count[2]];
                final int[] array8 = count;
                final int n8 = 2;
                ++array8[n8];
            }
            else if (scheme.substring(i).startsWith("d")) {
                result[i] = schemes[3][count[3]];
                final int[] array9 = count;
                final int n9 = 3;
                ++array9[n9];
            }
            else if (scheme.substring(i).startsWith("e")) {
                result[i] = schemes[4][count[4]];
                final int[] array10 = count;
                final int n10 = 4;
                ++array10[n10];
            }
        }
        return result;
    }
    
    private String[] makeLines(final int lines) {
        final String[] result = new String[lines];
        for (int i = 0; i < lines; ++i) {
            this.currentSyllables = 0;
            result[i] = this.fill(this.parseGrammar("START"));
        }
        return result;
    }
    
    private String fill(final String seg) {
        String result = "";
        if (seg.indexOf("@") == -1) {
            if (seg.startsWith("'") && seg.endsWith("'")) {
                result = seg.substring(1, seg.length() - 1);
            }
            else {
                result = this.getRandomWord(seg);
            }
        }
        else {
            final String[] segs = seg.split("@");
            for (int i = 0; i < segs.length; ++i) {
                if (segs[i].startsWith("'") && segs[i].endsWith("'")) {
                    result += segs[i].substring(1, segs[i].length() - 1);
                }
                else if (!segs[i].equals("")) {
                    result += this.getRandomWord(segs[i]);
                }
                if (i < segs.length - 1) {
                    result += " ";
                }
            }
        }
        return result;
    }
    
    private String getRandomWord(final String listName) {
        String result = "[LIST NOT FOUND]";
        for (int i = 0; i < this.module.getWordLists().size(); ++i) {
            if (this.module.getWordLists().get(i).getFileName().equals(listName)) {
                result = this.module.getWordLists().get(i).getRandomItem().getWord();
                this.currentSyllables += this.getWordFromLists(result).getSyllables();
                break;
            }
        }
        if (result.equals("[LIST NOT FOUND]")) {
            this.voice.sysout("Error: Word list '" + listName + "' not found, check data files!");
        }
        return result;
    }
    
    private bcWord getWordFromLists(final String word) {
        bcWord result = null;
        for (int i = 0; i < this.module.getWordLists().size(); ++i) {
            if (this.module.getWordLists().get(i).getItem(word) != null) {
                result = this.module.getWordLists().get(i).getItem(word);
                break;
            }
        }
        if (result == null) {
            this.voice.sysout("Warning: Word not listed: '" + word + "'");
        }
        return result;
    }
    
    private String getLastWord(final String line) {
        String result = line;
        if (line.indexOf(" ") > 0) {
            final String[] temp = line.split(" ");
            int i = 0;
            do {
                ++i;
                result = temp[temp.length - i];
            } while ((result.equals("!") || result.equals("")) && i >= temp.length);
        }
        return result;
    }
    
    private int rhyming(String lineA, String lineB) {
        int result = 0;
        lineA = this.getLastWord(lineA.toLowerCase());
        lineB = this.getLastWord(lineB.toLowerCase());
        if (lineA.equals("") || lineB.equals("")) {
            result = -1;
        }
        else if (lineA.equals(lineB)) {
            result = -2;
        }
        else if (this.rhymingAux(lineA, lineB)) {
            result = 1;
        }
        else {
            result = 0;
        }
        return result;
    }
    
    private boolean rhymingAux(String wordA, String wordB) {
        boolean last = false;
        final bcWord A = this.getWordFromLists(wordA);
        final bcWord B = this.getWordFromLists(wordB);
        if (A.getSyllables() == 1 || B.getSyllables() == 1) {
            last = true;
        }
        wordA = A.getRhymeKey(last).replaceAll("AE", "EH");
        wordB = B.getRhymeKey(last).replaceAll("AE", "EH");
        return wordA.equals(wordB);
    }
    
    private boolean metricMatch(String lineA, String lineB) {
        boolean result = true;
        int count = 0;
        lineA = this.metricCode(lineA);
        lineB = this.metricCode(lineB);
        if (lineA.length() == lineB.length()) {
            for (int i = 0; i < lineA.length(); ++i) {
                final char A = lineA.charAt(i);
                final char B = lineB.charAt(i);
                if ((A == '0' && B == '1') || (A == '1' && B == '0')) {
                    result = false;
                }
                else if ((A == 'O' && (B == '1' || B == 'I')) || (A == 'I' && (B == '0' || B == 'O')) || (A == '0' && B == 'I') || (A == '1' && B == 'O')) {
                    ++count;
                }
            }
            if (count > this.module.getMetricTolerance()) {
                result = false;
            }
        }
        else {
            result = false;
        }
        return result;
    }
    
    private String metricCode(final String line) {
        String metrum = "";
        if (line.indexOf(" ") == -1) {
            metrum += this.getWordFromLists(line).getMetricCode();
        }
        else {
            final String[] temp = line.split(" ");
            for (int j = 0; j < temp.length; ++j) {
                metrum += this.getWordFromLists(temp[j]).getMetricCode();
            }
        }
        return metrum;
    }
    
    private String replaceBrackets(String segment) {
        if (segment.indexOf("(") != -1 && segment.indexOf(")") != -1) {
            final String brk1 = segment.substring(0, segment.indexOf("("));
            String brk2 = segment.substring(segment.indexOf("("), segment.indexOf(")"));
            final String brk3 = segment.substring(segment.indexOf(")") + 1, segment.length());
            brk2 = brk2.replace(';', '&');
            brk2 = brk2.replace('#', '|');
            segment = brk1 + brk2 + ")" + this.replaceBrackets(brk3);
        }
        return segment;
    }
    
    private boolean isWord(final String word) {
        return word.indexOf(",") == -1 && word.indexOf(";") == -1 && word.indexOf(".") == -1 && word.indexOf("(") == -1 && word.indexOf(")") == -1 && word.indexOf("'") == -1 && word.indexOf("*") == -1;
    }
    
    public List getLyrics() {
        if (this.module != null && this.module.isInitialized()) {
            this.voice.progressBarStart("Generating, please wait...");
            this.voice.resetLyrics();
            this.voice.verboseLineFeed();
            this.voice.sing("*******************************************");
            this.voice.sing("*             " + this.fill(this.parseGrammar("TITLE")) + "             *");
            this.voice.sing("*******************************************");
            this.parseLayout();
            this.voice.sing("*******************************************");
            this.voice.progressBarEnd("done.");
        }
        else {
            this.voice.sysout("Error: Battlecry has not been initialized.");
        }
        return this.voice.getLyrics();
    }
    
    public void getModuleInfo() {
        if (this.module != null && this.module.isInitialized()) {
            this.voice.sysout("Module Name: " + this.module.getInfo().getProperty("NAME"));
            this.voice.sysout("Author: " + this.module.getInfo().getProperty("AUTHOR"));
            this.voice.sysout("Version: " + this.module.getInfo().getProperty("VERSION"));
            this.voice.sysout("Comment: " + this.module.getInfo().getProperty("COMMENT"));
        }
        else {
            this.voice.sysout("Error: Battlecry has not been initialized.");
        }
    }
}
