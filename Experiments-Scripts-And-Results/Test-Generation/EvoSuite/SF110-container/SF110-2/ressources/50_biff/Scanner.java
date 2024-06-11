import java.util.*;
import java.io.*;

public class Scanner
{
    public ArrayList lexems;
    ArrayList vars;
    ArrayList var_types;
    ArrayList var_vals;
    private int pos;
    private int place;
    private int main_begin;
    
    public Scanner(final String filename) throws Exception {
        this.lexems = new ArrayList();
        this.vars = new ArrayList();
        this.var_types = new ArrayList();
        this.var_vals = new ArrayList();
        this.pos = 0;
        this.place = 0;
        this.main_begin = 0;
        final File file = new File(filename);
        final FileInputStream filein = new FileInputStream(file);
        this.lexems = new ArrayList();
        StringBuffer value = new StringBuffer();
        byte type = 0;
        int line = 1;
        int column = 1;
        int began = 1;
        int sline = 1;
        int bite = filein.read();
        while (bite != -1) {
            if (bite == 34) {
                if (type != 0) {
                    this.lexems.add(new Lexem(value, type, line, began));
                    value = new StringBuffer();
                }
                if (type != 5) {
                    type = 5;
                    began = column;
                    sline = line;
                }
                else {
                    type = 0;
                }
            }
            else if (type == 5) {
                value.append((char)bite);
                if (bite == 13) {
                    ++line;
                    column = -1;
                }
            }
            else if (bite >= 48 && bite <= 57) {
                if (type == 0) {
                    type = 1;
                    began = column;
                }
                value.append((char)bite);
            }
            else if ((bite >= 65 && bite <= 90) || (bite >= 97 && bite <= 122)) {
                if (type == 0) {
                    type = 3;
                    began = column;
                    value.append((char)bite);
                }
                else {
                    if (type != 3) {
                        type = 7;
                        break;
                    }
                    value.append((char)bite);
                }
            }
            else if ((bite >= 40 && bite <= 45) || bite == 47 || (bite >= 58 && bite <= 62)) {
                if (type != 0) {
                    this.lexems.add(new Lexem(value, type, line, began));
                    type = 0;
                    value = new StringBuffer();
                }
                this.lexems.add(new Lexem(bite, 4, line, column));
            }
            else if (bite == 46) {
                if (type == 0) {
                    type = 8;
                    break;
                }
                if (type == 3) {
                    if (!value.toString().toUpperCase().equals("END")) {
                        type = 8;
                        break;
                    }
                    this.lexems.add(new Lexem("END.", (byte)3, line, began));
                    type = 0;
                    value = new StringBuffer();
                }
                else {
                    if (type != 1) {
                        type = 8;
                        break;
                    }
                    type = 2;
                    value.append('.');
                }
            }
            else {
                if (bite != 9 && bite != 32 && bite != 13 && bite != 10) {
                    type = 9;
                    break;
                }
                if (type != 0) {
                    this.lexems.add(new Lexem(value, type, line, began));
                    type = 0;
                    value = new StringBuffer();
                }
                if (bite == 13) {
                    ++line;
                    column = -1;
                }
            }
            bite = filein.read();
            if (bite == -1 && type == 5) {
                type = 6;
            }
            else if (bite == -1 && type != 0) {
                this.lexems.add(new Lexem(value, type, line, began));
                type = 0;
                value = new StringBuffer();
            }
            ++column;
        }
        if (type == 6) {
            System.out.println("ERROR: String not closed ! line: " + sline + ", column: " + began);
        }
        else if (type == 7) {
            System.out.println("ERROR: letter joined with number ! line: " + line + ", column: " + column);
        }
        else if (type == 8) {
            System.out.println("ERROR: dot in wrong place ! line: " + line + ", column: " + column);
        }
        else if (type == 9) {
            System.out.println("ERROR: illegal character !: " + (char)bite + " line: " + line + ", column: " + column);
        }
        else {
            final ArrayList AdvL = this.advScanning(this.lexems);
            final String tmp_result = this.checkStructure(AdvL);
            System.out.println(tmp_result);
            if (tmp_result.equals("No errors !")) {
                System.out.println("===================");
                this.initializeVars();
                this.pos = this.main_begin;
                final ArrayList ExecL = this.interBody(AdvL);
                this.execute(ExecL);
            }
        }
    }
    
    public void printLexems(final ArrayList lex) {
        System.out.println("\nLEXEMS: " + lex.size());
        System.out.println("VALUE   TYPE   LINE   COLUMN");
        final Object[] lexems = lex.toArray();
        for (int i = 0; i < lexems.length; ++i) {
            final Lexem lexem = (Lexem)lexems[i];
            System.out.println(lexem.value + "   " + lexem.type + "   " + lexem.line + "   " + lexem.column);
        }
        System.out.println();
    }
    
    public void printAdvLexems(final ArrayList AdvLex) {
        System.out.println("\nLEXEMS: " + AdvLex.size());
        System.out.println("VALUE   CODE   LCATEGORY   HCATEGORY   LINE   COLUMN");
        final Object[] AdvLexems = AdvLex.toArray();
        for (int i = 0; i < AdvLexems.length; ++i) {
            final AdvLexem advlexem = (AdvLexem)AdvLexems[i];
            System.out.println(advlexem.value + "   " + advlexem.code + "   " + advlexem.lcategory + "   " + advlexem.hcategory + "   " + advlexem.line + "   " + advlexem.column);
        }
        System.out.println();
    }
    
    public ArrayList advScanning(final ArrayList lexems) {
        final ArrayList AdvLexems = new ArrayList();
        String value = "";
        final Codes codes = new Codes();
        codes.fillLists();
        Lexem lexem = null;
        byte code = 0;
        byte lcategory = 0;
        byte hcategory = 0;
        for (int size = lexems.size(), i = 0; i < size; ++i) {
            lexem = lexems.get(i);
            if (lexem.type == 5) {
                AdvLexems.add(new AdvLexem(lexem.value, (byte)5, (byte)5, (byte)5, lexem.line, lexem.column));
            }
            else if (lexem.type == 3) {
                hcategory = 3;
                code = codes.getKeywordCode(lexem.value);
                if (code == 61) {
                    lcategory = 61;
                }
                else if (code >= 21 && code <= 22) {
                    lcategory = 63;
                }
                else if (code >= 25 && code <= 27) {
                    lcategory = 62;
                }
                else {
                    lcategory = 60;
                }
                AdvLexems.add(new AdvLexem(lexem.value, code, lcategory, hcategory, lexem.line, lexem.column));
            }
            else if (lexem.type == 4) {
                value = lexem.value;
                hcategory = 4;
                code = codes.getSignCode(lexem.value);
                if (code >= 40 && code <= 46) {
                    lcategory = 66;
                    if (lexem.value.equals(":") && i + 1 < size && lexems.get(i + 1).value.equals("=")) {
                        code = 43;
                        value = ":=";
                        ++i;
                    }
                }
                else if (code >= 34 && code <= 39) {
                    lcategory = 65;
                    if (lexem.value.equals("<")) {
                        if (i + 1 < size) {
                            if (lexems.get(i + 1).value.equals("=")) {
                                code = 39;
                                value = "<=";
                                ++i;
                            }
                            if (lexems.get(i + 1).value.equals(">")) {
                                code = 35;
                                value = "<>";
                                ++i;
                            }
                        }
                        else if (lexem.value.equals(">") && i + 1 < size && lexems.get(i + 1).value.equals("=")) {
                            code = 37;
                            value = ">=";
                            ++i;
                        }
                    }
                }
                else if (code >= 30 && code <= 33) {
                    lcategory = 64;
                }
                AdvLexems.add(new AdvLexem(value, code, lcategory, hcategory, lexem.line, lexem.column));
            }
            else {
                AdvLexems.add(new AdvLexem(lexem.value, lexem.type, (byte)67, (byte)3, lexem.line, lexem.column));
            }
        }
        return AdvLexems;
    }
    
    private String errorMess(final String expected, final AdvLexem found) {
        return "expected: " + expected + "\n   found: " + found.value + "\n   line: " + found.line + "\n   column: " + found.column;
    }
    
    private String errorMess(final String expected) {
        return "expected: " + expected + "   nothing found";
    }
    
    private String checkC(final String s, final byte code, final ArrayList AL) {
        if (this.pos >= AL.size()) {
            return this.errorMess(s);
        }
        final AdvLexem al = AL.get(this.pos);
        if (al.code != code) {
            return this.errorMess(s, al);
        }
        return "";
    }
    
    private String checkC(final String s, final byte code, final ArrayList AL, final int which) {
        if (which >= AL.size()) {
            return this.errorMess(s);
        }
        final AdvLexem al = AL.get(which);
        if (al.code != code) {
            return this.errorMess(s, al);
        }
        return "";
    }
    
    private String checkL(final String s, final byte code, final ArrayList AL) {
        if (this.pos >= AL.size()) {
            return this.errorMess(s);
        }
        final AdvLexem al = AL.get(this.pos);
        if (al.lcategory != code) {
            return this.errorMess(s, al);
        }
        return "";
    }
    
    public String checkStructure(final ArrayList AL) {
        final Codes codes = new Codes();
        codes.fillLists();
        String result = "";
        final int size = AL.size();
        result = this.checkC("PROGRAM", (byte)10, AL);
        if (!result.equals("")) {
            return result;
        }
        ++this.pos;
        result = this.checkC("VARIABLE", (byte)61, AL);
        if (!result.equals("")) {
            return result;
        }
        ++this.pos;
        result = this.checkC("SEMICOLON", (byte)41, AL);
        if (!result.equals("")) {
            return result;
        }
        ++this.pos;
        result = this.checkC("VAR", (byte)14, AL);
        if (!result.equals("")) {
            result = this.checkC("VAR or BEGIN", (byte)11, AL);
            if (!result.equals("")) {
                return result;
            }
        }
        if (AL.get(this.pos).value.equals("VAR")) {
            ++this.pos;
            result = this.checkVAR(AL);
            if (!result.equals("")) {
                return result;
            }
        }
        if (AL.size() - 1 <= this.pos) {
            return "expected: END. at the end of program";
        }
        result = this.checkC("END.", (byte)13, AL, AL.size() - 1);
        if (!result.equals("")) {
            return result;
        }
        for (int i = this.pos + 1; i < AL.size() - 2; ++i) {
            final AdvLexem al = AL.get(i);
            if (al.code == 61 && !this.vars.contains(al.value)) {
                return "cannot resolve symbol variable " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
            }
        }
        ++this.pos;
        this.main_begin = this.pos;
        result = this.checkBody(AL);
        if (!result.equals("")) {
            return result;
        }
        return "No errors !";
    }
    
    private String checkVAR(final ArrayList AL) {
        byte var_count = 0;
        String result = "";
        AdvLexem al = null;
        boolean finish_b = false;
        boolean finish_c = false;
        while (!finish_b) {
            result = this.checkC("BEGIN", (byte)11, AL);
            if (!result.equals("")) {
                result = this.checkC("VARIABLE", (byte)61, AL);
                if (!result.equals("")) {
                    return result;
                }
                al = AL.get(this.pos);
                if (AL.get(1).value.equals(al.value)) {
                    return "VARIABLE is already used as a neme of the program\n   line: " + al.line + "\n   column: " + al.column;
                }
                if (this.vars.contains(al.value)) {
                    return "VARIABLE already declared: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
                }
                this.vars.add(al.value);
                ++var_count;
                finish_c = false;
                while (!finish_c) {
                    ++this.pos;
                    result = this.checkC("COMMA", (byte)42, AL);
                    if (result.equals("")) {
                        ++this.pos;
                        result = this.checkC("VARIABLE", (byte)61, AL);
                        if (!result.equals("")) {
                            return result;
                        }
                        al = AL.get(this.pos);
                        if (AL.get(1).value.equals(al.value)) {
                            return "VARIABLE is already used as a neme of the program\n   line: " + al.line + "\n   column: " + al.column;
                        }
                        if (this.vars.contains(al.value)) {
                            return "VARIABLE already declared: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
                        }
                        this.vars.add(al.value);
                        ++var_count;
                    }
                    else {
                        finish_c = true;
                    }
                }
                result = this.checkC("COLON", (byte)40, AL);
                if (!result.equals("")) {
                    return result;
                }
                ++this.pos;
                result = this.checkL("VARIABLE TYPE", (byte)62, AL);
                if (!result.equals("")) {
                    return result;
                }
                final byte code = AL.get(this.pos).code;
                for (byte i = 0; i < var_count; ++i) {
                    this.var_types.add(new Integer(code));
                }
                var_count = 0;
                ++this.pos;
                result = this.checkC("SEMICOLON", (byte)41, AL);
                if (!result.equals("")) {
                    return result;
                }
                ++this.pos;
            }
            else {
                finish_b = true;
            }
        }
        return "";
    }
    
    private String checkIASSOC(final ArrayList AL) {
        String result = "";
        AdvLexem al = null;
        byte b_count = 0;
        boolean finish_b = false;
        while (!finish_b) {
            result = this.checkC("VALUE", (byte)44, AL);
            if (!result.equals("")) {
                finish_b = true;
            }
            else {
                ++b_count;
                ++this.pos;
            }
        }
        result = this.checkC("VALUE", (byte)31, AL);
        if (result.equals("")) {
            ++this.pos;
            result = this.checkC("INTEGER NUMBER", (byte)1, AL);
            if (!result.equals("")) {
                return result;
            }
            al = AL.get(this.pos);
            AL.set(this.pos - 1, new AdvLexem("-" + al.value, al.code, al.lcategory, al.hcategory, al.line, al.column));
            AL.remove(this.pos);
        }
        else {
            result = this.checkC("INTEGER VALUE", (byte)1, AL);
            if (!result.equals("")) {
                result = this.checkC("INTEGER VARIABLE", (byte)61, AL);
                if (!result.equals("")) {
                    return result;
                }
                al = AL.get(this.pos);
                final int tmp = this.vars.indexOf(al.value);
                if (!this.var_types.get(tmp).equals(new Integer(25))) {
                    al = AL.get(this.pos);
                    return "INTEGER VARIABLE expected \n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
                }
                ++this.pos;
            }
            else {
                ++this.pos;
            }
        }
        finish_b = false;
        while (!finish_b) {
            result = this.checkC("OPERATOR", (byte)45, AL);
            if (!result.equals("")) {
                finish_b = true;
            }
            else {
                if (b_count <= 0) {
                    al = AL.get(this.pos);
                    return "RIGHT BRACKET not opened\n   line: " + al.line + "\n   column: " + al.column;
                }
                --b_count;
                ++this.pos;
            }
        }
        for (boolean finish_o = false; !finish_o; finish_o = true) {
            result = this.checkL("ARITHMETIC OPERATOR", (byte)64, AL);
            if (!(!result.equals(""))) {
                ++this.pos;
                finish_b = false;
                while (!finish_b) {
                    result = this.checkC("VALUE", (byte)44, AL);
                    if (!result.equals("")) {
                        finish_b = true;
                    }
                    else {
                        ++b_count;
                        ++this.pos;
                    }
                }
                result = this.checkC("VALUE", (byte)31, AL);
                if (result.equals("")) {
                    ++this.pos;
                    result = this.checkC("INTEGER NUMBER", (byte)1, AL);
                    if (!result.equals("")) {
                        return result;
                    }
                    al = AL.get(this.pos);
                    AL.set(this.pos - 1, new AdvLexem("-" + al.value, al.code, al.lcategory, al.hcategory, al.line, al.column));
                    AL.remove(this.pos);
                }
                else {
                    result = this.checkC("INTEGER VALUE", (byte)1, AL);
                    if (!result.equals("")) {
                        result = this.checkC("INTEGER VARIABLE", (byte)61, AL);
                        if (!result.equals("")) {
                            return result;
                        }
                        al = AL.get(this.pos);
                        final int tmp2 = this.vars.indexOf(al.value);
                        if (!this.var_types.get(tmp2).equals(new Integer(25))) {
                            al = AL.get(this.pos);
                            return "INTEGER VARIABLE expected \n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
                        }
                        ++this.pos;
                    }
                    else {
                        ++this.pos;
                    }
                }
                finish_b = false;
                while (!finish_b) {
                    result = this.checkC("OPERATOR", (byte)45, AL);
                    if (!result.equals("")) {
                        finish_b = true;
                    }
                    else {
                        if (b_count <= 0) {
                            al = AL.get(this.pos);
                            return "RIGHT BRACKET not opened\n   line: " + al.line + "\n   column: " + al.column;
                        }
                        --b_count;
                        ++this.pos;
                    }
                }
            }
        }
        return "";
    }
    
    private String checkRASSOC(final ArrayList AL) {
        String result = "";
        AdvLexem al = null;
        byte b_count = 0;
        boolean finish_b = false;
        while (!finish_b) {
            result = this.checkC("VALUE", (byte)44, AL);
            if (!result.equals("")) {
                finish_b = true;
            }
            else {
                ++b_count;
                ++this.pos;
            }
        }
        result = this.checkC("VALUE", (byte)31, AL);
        if (result.equals("")) {
            ++this.pos;
            result = this.checkC("INTEGER OR REAL NUMBER", (byte)1, AL);
            if (!result.equals("") && !this.checkC("", (byte)2, AL).equals("")) {
                return result;
            }
            al = AL.get(this.pos);
            AL.set(this.pos - 1, new AdvLexem("-" + al.value, al.code, al.lcategory, al.hcategory, al.line, al.column));
            AL.remove(this.pos);
        }
        else {
            result = this.checkC("", (byte)1, AL);
            if (!result.equals("") && !this.checkC("", (byte)2, AL).equals("")) {
                result = this.checkC("INTEGER OR REAL VARIABLE", (byte)61, AL);
                if (!result.equals("")) {
                    return result;
                }
                al = AL.get(this.pos);
                final int tmp = this.vars.indexOf(al.value);
                final int type = this.var_types.get(tmp);
                if (type != 25 && type != 26) {
                    al = AL.get(this.pos);
                    return "INTEGER OR REAL VARIABLE expected \n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
                }
                ++this.pos;
            }
            else {
                ++this.pos;
            }
        }
        finish_b = false;
        while (!finish_b) {
            result = this.checkC("OPERATOR", (byte)45, AL);
            if (!result.equals("")) {
                finish_b = true;
            }
            else {
                if (b_count <= 0) {
                    al = AL.get(this.pos);
                    return "RIGHT BRACKET not opened\n   line: " + al.line + "\n   column: " + al.column;
                }
                --b_count;
                ++this.pos;
            }
        }
        boolean finish_o = false;
        while (!finish_o) {
            result = this.checkL("closing BRACKET", (byte)64, AL);
            if (!result.equals("")) {
                finish_o = true;
                if (b_count > 0) {
                    return result;
                }
                continue;
            }
            else {
                ++this.pos;
                finish_b = false;
                while (!finish_b) {
                    result = this.checkC("VALUE", (byte)44, AL);
                    if (!result.equals("")) {
                        finish_b = true;
                    }
                    else {
                        ++b_count;
                        ++this.pos;
                    }
                }
                result = this.checkC("VALUE", (byte)31, AL);
                if (result.equals("")) {
                    ++this.pos;
                    result = this.checkC("INTEGER OR REAL NUMBER", (byte)1, AL);
                    if (!result.equals("") && !this.checkC("", (byte)2, AL).equals("")) {
                        return result;
                    }
                    al = AL.get(this.pos);
                    AL.set(this.pos - 1, new AdvLexem("-" + al.value, al.code, al.lcategory, al.hcategory, al.line, al.column));
                    AL.remove(this.pos);
                }
                else {
                    result = this.checkC("", (byte)1, AL);
                    if (!result.equals("") && !this.checkC("", (byte)2, AL).equals("")) {
                        result = this.checkC("INTEGER OR REAL VARIABLE", (byte)61, AL);
                        if (!result.equals("")) {
                            return result;
                        }
                        al = AL.get(this.pos);
                        final int tmp2 = this.vars.indexOf(al.value);
                        final int type2 = this.var_types.get(tmp2);
                        if (type2 != 25 && type2 != 26) {
                            al = AL.get(this.pos);
                            return "INTEGER OR REAL VARIABLE expected \n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
                        }
                        ++this.pos;
                    }
                    else {
                        ++this.pos;
                    }
                }
                finish_b = false;
                while (!finish_b) {
                    result = this.checkC("OPERATOR", (byte)45, AL);
                    if (!result.equals("")) {
                        finish_b = true;
                    }
                    else {
                        if (b_count <= 0) {
                            al = AL.get(this.pos);
                            return "RIGHT BRACKET not opened\n   line: " + al.line + "\n   column: " + al.column;
                        }
                        --b_count;
                        ++this.pos;
                    }
                }
            }
        }
        return "";
    }
    
    private String checkLEXPR(final ArrayList AL) {
        String result = "";
        AdvLexem al = null;
        byte b_count = 0;
        boolean finish_o = false;
        boolean finish_n = false;
        boolean finish_b = false;
        while (!finish_n) {
            finish_b = false;
            while (!finish_b) {
                result = this.checkC("VALUE", (byte)44, AL);
                if (!result.equals("")) {
                    finish_b = true;
                }
                else {
                    ++b_count;
                    ++this.pos;
                }
            }
            result = this.checkC("NOT", (byte)20, AL);
            if (!result.equals("")) {
                finish_n = true;
            }
            else {
                ++this.pos;
            }
        }
        result = this.checkLEXPRNElem(AL);
        if (result.equals("")) {
            result = this.checkL("RELATION OPERATOR", (byte)65, AL);
            if (!result.equals("")) {
                return result;
            }
            ++this.pos;
            result = this.checkLEXPRNElem(AL);
            if (!result.equals("")) {
                return result;
            }
        }
        else {
            result = this.checkLEXPRLElem(AL);
            if (!result.equals("")) {
                return result;
            }
        }
        finish_b = false;
        while (!finish_b) {
            result = this.checkC("OPERATOR", (byte)45, AL);
            if (!result.equals("")) {
                finish_b = true;
            }
            else {
                if (b_count <= 0) {
                    al = AL.get(this.pos);
                    return "RIGHT BRACKET not opened\n   line: " + al.line + "\n   column: " + al.column;
                }
                --b_count;
                ++this.pos;
            }
        }
        finish_o = false;
        while (!finish_o) {
            result = this.checkL("closing BRACKET", (byte)63, AL);
            if (!result.equals("")) {
                finish_o = true;
                if (b_count > 0) {
                    return result;
                }
                continue;
            }
            else {
                ++this.pos;
                finish_n = false;
                while (!finish_n) {
                    finish_b = false;
                    while (!finish_b) {
                        result = this.checkC("VALUE", (byte)44, AL);
                        if (!result.equals("")) {
                            finish_b = true;
                        }
                        else {
                            ++b_count;
                            ++this.pos;
                        }
                    }
                    result = this.checkC("NOT", (byte)20, AL);
                    if (!result.equals("")) {
                        finish_n = true;
                    }
                    else {
                        ++this.pos;
                    }
                }
                result = this.checkLEXPRNElem(AL);
                if (result.equals("")) {
                    result = this.checkL("RELATION OPERATOR", (byte)65, AL);
                    if (!result.equals("")) {
                        return result;
                    }
                    ++this.pos;
                    result = this.checkLEXPRNElem(AL);
                    if (!result.equals("")) {
                        return result;
                    }
                }
                else {
                    result = this.checkLEXPRLElem(AL);
                    if (!result.equals("")) {
                        return result;
                    }
                }
                finish_b = false;
                while (!finish_b) {
                    result = this.checkC("OPERATOR", (byte)45, AL);
                    if (!result.equals("")) {
                        finish_b = true;
                    }
                    else {
                        if (b_count <= 0) {
                            al = AL.get(this.pos);
                            return "RIGHT BRACKET not opened\n   line: " + al.line + "\n   column: " + al.column;
                        }
                        --b_count;
                        ++this.pos;
                    }
                }
            }
        }
        return "";
    }
    
    private String checkWRITE(final ArrayList AL) {
        String result = "";
        final AdvLexem al = null;
        result = this.checkC("LEFT BRACKET", (byte)44, AL);
        if (!result.equals("")) {
            return result;
        }
        ++this.pos;
        result = this.checkC("STRING or VARIABLE", (byte)5, AL);
        if (!result.equals("") && !this.checkC("", (byte)61, AL).equals("")) {
            return result;
        }
        ++this.pos;
        boolean finish_c = false;
        while (!finish_c) {
            result = this.checkC("COMMA", (byte)42, AL);
            if (!result.equals("")) {
                finish_c = true;
            }
            else {
                ++this.pos;
                result = this.checkC("STRING or VARIABLE", (byte)5, AL);
                if (!result.equals("") && !this.checkC("", (byte)61, AL).equals("")) {
                    return result;
                }
                ++this.pos;
            }
        }
        result = this.checkC("RIGHT BRACKET", (byte)45, AL);
        if (!result.equals("")) {
            return result;
        }
        ++this.pos;
        return "";
    }
    
    private String checkREAD(final ArrayList AL) {
        String result = "";
        final AdvLexem al = null;
        result = this.checkC("LEFT BRACKET", (byte)44, AL);
        if (!result.equals("")) {
            return result;
        }
        ++this.pos;
        result = this.checkC("VARIABLE", (byte)61, AL);
        if (!result.equals("")) {
            return result;
        }
        ++this.pos;
        result = this.checkC("RIGHT BRACKET", (byte)45, AL);
        if (!result.equals("")) {
            return result;
        }
        ++this.pos;
        return "";
    }
    
    private String checkBody(final ArrayList AL) {
        String result = "";
        AdvLexem al = null;
        final int begins = 0;
        while (true) {
            al = AL.get(this.pos);
            if (al.code == 61) {
                ++this.pos;
                result = this.checkC("ASSOCIATE MARK", (byte)43, AL);
                if (!result.equals("")) {
                    return result;
                }
                ++this.pos;
                final int tmp = this.vars.indexOf(al.value);
                final int type = this.var_types.get(tmp);
                if (type == 25) {
                    result = this.checkIASSOC(AL);
                    if (!result.equals("")) {
                        return result;
                    }
                }
                else if (type == 26) {
                    result = this.checkRASSOC(AL);
                    if (!result.equals("")) {
                        return result;
                    }
                }
                else if (type == 27) {
                    result = this.checkLEXPR(AL);
                    if (!result.equals("")) {
                        return result;
                    }
                }
                result = this.checkEnding(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 24) {
                ++this.pos;
                result = this.checkWRITE(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkEnding(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 23) {
                ++this.pos;
                result = this.checkREAD(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkEnding(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 18) {
                ++this.pos;
                result = this.checkLEXPR(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkC("keyword DO", (byte)19, AL);
                if (!result.equals("")) {
                    return result;
                }
                ++this.pos;
                result = this.checkOne(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 15) {
                ++this.pos;
                result = this.checkLEXPR(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkC("keyword THEN", (byte)16, AL);
                if (!result.equals("")) {
                    return result;
                }
                ++this.pos;
                result = this.checkOne(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkC("keyword ELSE", (byte)17, AL);
                if (!result.equals("")) {
                    continue;
                }
                ++this.pos;
                result = this.checkOne(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 11) {
                AL.remove(this.pos);
                result = this.checkBlock(AL);
                if (!result.equals("")) {
                    return result;
                }
                --this.pos;
                result = this.checkC("SEMICOLON", (byte)41, AL);
                if (result.equals("")) {
                    AL.remove(this.pos);
                    --this.pos;
                }
                AL.remove(this.pos);
            }
            else {
                if (al.code == 12) {
                    return "END without coresponding BEGIN\n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
                }
                if (al.code == 13) {
                    return "";
                }
                return "Not a statement\n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
            }
        }
    }
    
    private String checkBlock(final ArrayList AL) {
        String result = "";
        AdvLexem al = null;
        final int begins = 0;
        while (true) {
            al = AL.get(this.pos);
            if (al.code == 61) {
                ++this.pos;
                result = this.checkC("ASSOCIATE MARK", (byte)43, AL);
                if (!result.equals("")) {
                    return result;
                }
                ++this.pos;
                final int tmp = this.vars.indexOf(al.value);
                final int type = this.var_types.get(tmp);
                if (type == 25) {
                    result = this.checkIASSOC(AL);
                    if (!result.equals("")) {
                        return result;
                    }
                }
                else if (type == 26) {
                    result = this.checkRASSOC(AL);
                    if (!result.equals("")) {
                        return result;
                    }
                }
                else if (type == 27) {
                    result = this.checkLEXPR(AL);
                    if (!result.equals("")) {
                        return result;
                    }
                }
                result = this.checkEnding(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 24) {
                ++this.pos;
                result = this.checkWRITE(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkEnding(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 23) {
                ++this.pos;
                result = this.checkREAD(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkEnding(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 18) {
                ++this.pos;
                result = this.checkLEXPR(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkC("keyword DO", (byte)19, AL);
                if (!result.equals("")) {
                    return result;
                }
                ++this.pos;
                result = this.checkOne(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 15) {
                ++this.pos;
                result = this.checkLEXPR(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkC("keyword THEN", (byte)16, AL);
                if (!result.equals("")) {
                    return result;
                }
                ++this.pos;
                result = this.checkOne(AL);
                if (!result.equals("")) {
                    return result;
                }
                result = this.checkC("keyword ELSE", (byte)17, AL);
                if (!result.equals("")) {
                    continue;
                }
                ++this.pos;
                result = this.checkOne(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 11) {
                ++this.pos;
                result = this.checkBlock(AL);
                if (!result.equals("")) {
                    return result;
                }
                continue;
            }
            else if (al.code == 12) {
                ++this.pos;
                result = this.checkEnding(AL);
                if (!result.equals("")) {
                    return result;
                }
                return "";
            }
            else {
                if (al.code == 13) {
                    return "END expected\n   line: " + al.line + "\n   column: " + al.column;
                }
                return "Not a statement\n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
            }
        }
    }
    
    private String checkOne(final ArrayList AL) {
        String result = "";
        AdvLexem al = null;
        final int begins = 0;
        al = AL.get(this.pos);
        if (al.code == 61) {
            ++this.pos;
            result = this.checkC("ASSOCIATE MARK", (byte)43, AL);
            if (!result.equals("")) {
                return result;
            }
            ++this.pos;
            final int tmp = this.vars.indexOf(al.value);
            final int type = this.var_types.get(tmp);
            if (type == 25) {
                result = this.checkIASSOC(AL);
                if (!result.equals("")) {
                    return result;
                }
            }
            else if (type == 26) {
                result = this.checkRASSOC(AL);
                if (!result.equals("")) {
                    return result;
                }
            }
            else if (type == 27) {
                result = this.checkLEXPR(AL);
                if (!result.equals("")) {
                    return result;
                }
            }
            return this.checkEnding(AL);
        }
        else if (al.code == 24) {
            ++this.pos;
            result = this.checkWRITE(AL);
            if (!result.equals("")) {
                return result;
            }
            return this.checkEnding(AL);
        }
        else if (al.code == 23) {
            ++this.pos;
            result = this.checkREAD(AL);
            if (!result.equals("")) {
                return result;
            }
            return this.checkEnding(AL);
        }
        else if (al.code == 18) {
            ++this.pos;
            result = this.checkLEXPR(AL);
            if (!result.equals("")) {
                return result;
            }
            result = this.checkC("keyword DO", (byte)19, AL);
            if (!result.equals("")) {
                return result;
            }
            ++this.pos;
            return this.checkOne(AL);
        }
        else if (al.code == 15) {
            ++this.pos;
            result = this.checkLEXPR(AL);
            if (!result.equals("")) {
                return result;
            }
            result = this.checkC("keyword THEN", (byte)16, AL);
            if (!result.equals("")) {
                return result;
            }
            ++this.pos;
            result = this.checkOne(AL);
            if (!result.equals("")) {
                return result;
            }
            result = this.checkC("keyword ELSE", (byte)17, AL);
            if (result.equals("")) {
                ++this.pos;
                return this.checkOne(AL);
            }
            return "";
        }
        else {
            if (al.code == 11) {
                ++this.pos;
                return this.checkBlock(AL);
            }
            if (al.code == 12) {
                return "END without coresponding BEGIN\n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
            }
            if (al.code == 13 || al.code == 17) {
                return "INSTRUCTION expected\n   line: " + al.line + "\n   column: " + al.column;
            }
            return "Not a statement\n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
        }
    }
    
    private String checkEnding(final ArrayList AL) {
        String result = "";
        result = this.checkC("SEMICOLON", (byte)41, AL);
        if (!result.equals("")) {
            result = this.checkC("SEMICOLON", (byte)12, AL);
            if (!result.equals("")) {
                result = this.checkC("SEMICOLON", (byte)17, AL);
                if (!result.equals("")) {
                    result = this.checkC("SEMICOLON", (byte)13, AL);
                    if (!result.equals("")) {
                        return result;
                    }
                }
            }
        }
        else {
            ++this.pos;
        }
        return "";
    }
    
    private String checkLEXPRNElem(final ArrayList AL) {
        String result = "";
        AdvLexem al = null;
        result = this.checkC("VALUE", (byte)31, AL);
        if (result.equals("")) {
            ++this.pos;
            result = this.checkC("INTEGER OR REAL NUMBER", (byte)1, AL);
            if (result.equals("") || this.checkC("", (byte)2, AL).equals("")) {
                al = AL.get(this.pos);
                AL.set(this.pos - 1, new AdvLexem("-" + al.value, al.code, al.lcategory, al.hcategory, al.line, al.column));
                AL.remove(this.pos);
                return "";
            }
            return result;
        }
        else {
            result = this.checkC("", (byte)1, AL);
            if (result.equals("") || this.checkC("", (byte)2, AL).equals("")) {
                ++this.pos;
                return "";
            }
            result = this.checkC("INTEGER OR REAL VARIABLE", (byte)61, AL);
            if (!result.equals("")) {
                return result;
            }
            al = AL.get(this.pos);
            final int tmp = this.vars.indexOf(al.value);
            final int type = this.var_types.get(tmp);
            if (type != 25 && type != 26) {
                al = AL.get(this.pos);
                return "INTEGER OR REAL VARIABLE expected \n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
            }
            ++this.pos;
            return "";
        }
    }
    
    private String checkLEXPRLElem(final ArrayList AL) {
        String result = "";
        AdvLexem al = null;
        result = this.checkC("", (byte)28, AL);
        if (result.equals("") || this.checkC("", (byte)29, AL).equals("")) {
            ++this.pos;
            return "";
        }
        result = this.checkC("LOGICAL VALUE", (byte)61, AL);
        if (!result.equals("")) {
            return result;
        }
        al = AL.get(this.pos);
        final int tmp = this.vars.indexOf(al.value);
        final int type = this.var_types.get(tmp);
        if (type != 27) {
            al = AL.get(this.pos);
            return "expected: LOGICAL VALUE\n   found: " + al.value + "\n   line: " + al.line + "\n   column: " + al.column;
        }
        ++this.pos;
        return "";
    }
    
    private ArrayList interBody(final ArrayList AL) {
        final ArrayList baze = new ArrayList();
        String itype = "";
        String variable = "";
        int start = 0;
        int finish = 0;
        ArrayList iftrue = new ArrayList();
        ArrayList ifelse = new ArrayList();
        AdvLexem al = null;
        while (true) {
            al = AL.get(this.pos);
            if (al.code == 61) {
                variable = al.value;
                ++this.pos;
                ++this.pos;
                start = this.pos;
                final int tmp = this.vars.indexOf(al.value);
                final int type = this.var_types.get(tmp);
                if (type == 25) {
                    this.checkIASSOC(AL);
                    finish = this.pos - 1;
                    itype = "IASSOC";
                }
                else if (type == 26) {
                    this.checkRASSOC(AL);
                    finish = this.pos - 1;
                    itype = "RASSOC";
                }
                else if (type == 27) {
                    this.checkLEXPR(AL);
                    finish = this.pos - 1;
                    itype = "BASSOC";
                }
                this.checkEnding(AL);
            }
            else if (al.code == 24) {
                itype = "WRITE";
                ++this.pos;
                start = this.pos;
                this.checkWRITE(AL);
                finish = this.pos - 1;
                this.checkEnding(AL);
            }
            else if (al.code == 23) {
                itype = "READ";
                ++this.pos;
                start = this.pos;
                this.checkREAD(AL);
                finish = this.pos - 1;
                this.checkEnding(AL);
            }
            else if (al.code == 18) {
                itype = "WHILE";
                ++this.pos;
                start = this.pos;
                this.checkLEXPR(AL);
                finish = this.pos - 1;
                ++this.pos;
                iftrue = this.interOne(AL);
            }
            else if (al.code == 15) {
                itype = "IF";
                ++this.pos;
                start = this.pos;
                this.checkLEXPR(AL);
                finish = this.pos - 1;
                ++this.pos;
                iftrue = this.interOne(AL);
                if (this.checkC("keyword ELSE", (byte)17, AL).equals("")) {
                    ++this.pos;
                    ifelse = this.interOne(AL);
                }
            }
            else {
                if (al.code == 11) {
                    ++this.pos;
                    return this.interBlock(AL);
                }
                if (al.code == 13) {
                    ++this.pos;
                    this.checkEnding(AL);
                    return baze;
                }
            }
            baze.add(new ExecLexem(itype, variable, AL, start, finish, iftrue, ifelse));
            iftrue = new ArrayList();
            ifelse = new ArrayList();
            variable = "";
        }
    }
    
    private ArrayList interBlock(final ArrayList AL) {
        final ArrayList baze = new ArrayList();
        String itype = "";
        String variable = "";
        int start = 0;
        int finish = 0;
        ArrayList iftrue = new ArrayList();
        ArrayList ifelse = new ArrayList();
        AdvLexem al = null;
        while (true) {
            al = AL.get(this.pos);
            if (al.code == 61) {
                variable = al.value;
                ++this.pos;
                ++this.pos;
                start = this.pos;
                final int tmp = this.vars.indexOf(al.value);
                final int type = this.var_types.get(tmp);
                if (type == 25) {
                    this.checkIASSOC(AL);
                    finish = this.pos - 1;
                    itype = "IASSOC";
                }
                else if (type == 26) {
                    this.checkRASSOC(AL);
                    finish = this.pos - 1;
                    itype = "RASSOC";
                }
                else if (type == 27) {
                    this.checkLEXPR(AL);
                    finish = this.pos - 1;
                    itype = "BASSOC";
                }
                this.checkEnding(AL);
            }
            else if (al.code == 24) {
                itype = "WRITE";
                ++this.pos;
                start = this.pos;
                this.checkWRITE(AL);
                finish = this.pos - 1;
                this.checkEnding(AL);
            }
            else if (al.code == 23) {
                itype = "READ";
                ++this.pos;
                start = this.pos;
                this.checkREAD(AL);
                finish = this.pos - 1;
                this.checkEnding(AL);
            }
            else if (al.code == 18) {
                itype = "WHILE";
                ++this.pos;
                start = this.pos;
                this.checkLEXPR(AL);
                finish = this.pos - 1;
                ++this.pos;
                iftrue = this.interOne(AL);
            }
            else if (al.code == 15) {
                itype = "IF";
                ++this.pos;
                start = this.pos;
                this.checkLEXPR(AL);
                finish = this.pos - 1;
                ++this.pos;
                iftrue = this.interOne(AL);
                if (this.checkC("keyword ELSE", (byte)17, AL).equals("")) {
                    ++this.pos;
                    ifelse = this.interOne(AL);
                }
            }
            else {
                if (al.code == 11) {
                    ++this.pos;
                    return this.interBlock(AL);
                }
                if (al.code == 12) {
                    ++this.pos;
                    this.checkEnding(AL);
                    return baze;
                }
            }
            baze.add(new ExecLexem(itype, variable, AL, start, finish, iftrue, ifelse));
            iftrue = new ArrayList();
            ifelse = new ArrayList();
            variable = "";
        }
    }
    
    private ArrayList interOne(final ArrayList AL) {
        final ArrayList baze = new ArrayList();
        String itype = "";
        String variable = "";
        int start = 0;
        int finish = 0;
        ArrayList iftrue = new ArrayList();
        ArrayList ifelse = new ArrayList();
        AdvLexem al = null;
        al = AL.get(this.pos);
        if (al.code == 61) {
            variable = al.value;
            ++this.pos;
            ++this.pos;
            start = this.pos;
            final int tmp = this.vars.indexOf(al.value);
            final int type = this.var_types.get(tmp);
            if (type == 25) {
                this.checkIASSOC(AL);
                finish = this.pos - 1;
                itype = "IASSOC";
            }
            else if (type == 26) {
                this.checkRASSOC(AL);
                finish = this.pos - 1;
                itype = "RASSOC";
            }
            else if (type == 27) {
                this.checkLEXPR(AL);
                finish = this.pos - 1;
                itype = "BASSOC";
            }
            this.checkEnding(AL);
        }
        else if (al.code == 24) {
            itype = "WRITE";
            ++this.pos;
            start = this.pos;
            this.checkWRITE(AL);
            finish = this.pos - 1;
            this.checkEnding(AL);
        }
        else if (al.code == 23) {
            itype = "READ";
            ++this.pos;
            start = this.pos;
            this.checkREAD(AL);
            finish = this.pos - 1;
            this.checkEnding(AL);
        }
        else if (al.code == 18) {
            itype = "WHILE";
            ++this.pos;
            start = this.pos;
            this.checkLEXPR(AL);
            finish = this.pos - 1;
            ++this.pos;
            iftrue = this.interOne(AL);
        }
        else if (al.code == 15) {
            itype = "IF";
            ++this.pos;
            start = this.pos;
            this.checkLEXPR(AL);
            finish = this.pos - 1;
            ++this.pos;
            iftrue = this.interOne(AL);
            if (this.checkC("keyword ELSE", (byte)17, AL).equals("")) {
                ++this.pos;
                ifelse = this.interOne(AL);
            }
        }
        else if (al.code == 11) {
            ++this.pos;
            return this.interBlock(AL);
        }
        baze.add(new ExecLexem(itype, variable, AL, start, finish, iftrue, ifelse));
        return baze;
    }
    
    private void initializeVars() {
        for (int i = 0; i < this.var_types.size(); ++i) {
            if (this.var_types.get(i) == 25) {
                this.var_vals.add(new Integer(0));
            }
            else if (this.var_types.get(i) == 26) {
                this.var_vals.add(new Float(0.0f));
            }
            else {
                this.var_vals.add(new Boolean(false));
            }
        }
    }
    
    private int evalInt(final AdvLexem[] lex) {
        final ArrayList args = new ArrayList();
        final ArrayList oper = new ArrayList();
        while (this.place < lex.length && lex[this.place].code != 45) {
            if (lex[this.place].code == 1) {
                args.add(new Integer(lex[this.place].value));
            }
            else if (lex[this.place].code == 61) {
                final int tmp = this.vars.indexOf(lex[this.place].value);
                args.add(this.var_vals.get(tmp));
            }
            else if (lex[this.place].code == 44) {
                ++this.place;
                args.add(new Integer(this.evalInt(lex)));
            }
            else if (lex[this.place].lcategory == 64) {
                oper.add(lex[this.place].value);
            }
            ++this.place;
        }
        for (int i = 0; i < oper.size(); ++i) {
            if (oper.get(i).equals("*")) {
                final int tmp2 = args.get(i);
                final int tmp3 = args.get(i + 1);
                args.set(i, new Integer(tmp2 * tmp3));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
            else if (oper.get(i).equals("/")) {
                final int tmp2 = args.get(i);
                final int tmp3 = args.get(i + 1);
                args.set(i, new Integer(tmp2 / tmp3));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
        }
        for (int i = 0; i < oper.size(); ++i) {
            if (oper.get(i).equals("+")) {
                final int tmp2 = args.get(i);
                final int tmp3 = args.get(i + 1);
                args.set(i, new Integer(tmp2 + tmp3));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
            else if (oper.get(i).equals("-")) {
                final int tmp2 = args.get(i);
                final int tmp3 = args.get(i + 1);
                args.set(i, new Integer(tmp2 - tmp3));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
        }
        return args.get(0);
    }
    
    private float evalReal(final AdvLexem[] lex) {
        final ArrayList args = new ArrayList();
        final ArrayList oper = new ArrayList();
        while (this.place < lex.length && lex[this.place].code != 45) {
            if (lex[this.place].lcategory == 67) {
                args.add(new Float(lex[this.place].value));
            }
            else if (lex[this.place].code == 61) {
                final int tmp = this.vars.indexOf(lex[this.place].value);
                final int type = this.var_types.get(tmp);
                if (type == 25) {
                    args.add(new Float(this.var_vals.get(tmp)));
                }
                else {
                    args.add(this.var_vals.get(tmp));
                }
            }
            else if (lex[this.place].code == 44) {
                ++this.place;
                args.add(new Float(this.evalReal(lex)));
            }
            else if (lex[this.place].lcategory == 64) {
                oper.add(lex[this.place].value);
            }
            ++this.place;
        }
        for (int i = 0; i < oper.size(); ++i) {
            if (oper.get(i).equals("*")) {
                final float tmp2 = args.get(i);
                final float tmp3 = args.get(i + 1);
                args.set(i, new Float(tmp2 * tmp3));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
            else if (oper.get(i).equals("/")) {
                final float tmp2 = args.get(i);
                final float tmp3 = args.get(i + 1);
                args.set(i, new Float(tmp2 / tmp3));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
        }
        for (int i = 0; i < oper.size(); ++i) {
            if (oper.get(i).equals("+")) {
                final float tmp2 = args.get(i);
                final float tmp3 = args.get(i + 1);
                args.set(i, new Float(tmp2 + tmp3));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
            else if (oper.get(i).equals("-")) {
                final float tmp2 = args.get(i);
                final float tmp3 = args.get(i + 1);
                args.set(i, new Float(tmp2 - tmp3));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
        }
        return args.get(0);
    }
    
    private boolean evalBool(final AdvLexem[] lex) {
        final ArrayList args = new ArrayList();
        final ArrayList oper = new ArrayList();
        final ArrayList nots = new ArrayList();
        int pointer = 0;
        while (this.place < lex.length && lex[this.place].code != 45) {
            if (lex[this.place].code == 61) {
                final int tmp = this.vars.indexOf(lex[this.place].value);
                final int type = this.var_types.get(tmp);
                if (type == 27) {
                    pointer = 1;
                }
                else {
                    pointer = 2;
                }
            }
            if (lex[this.place].code == 28 || lex[this.place].code == 29) {
                args.add(new Boolean(lex[this.place].value));
            }
            else if (pointer == 1) {
                final int tmp = this.vars.indexOf(lex[this.place].value);
                args.add(this.var_vals.get(tmp));
                pointer = 0;
            }
            else if (lex[this.place].code == 44) {
                ++this.place;
                args.add(new Boolean(this.evalBool(lex)));
            }
            else if (lex[this.place].code == 21 || lex[this.place].code == 22) {
                oper.add(lex[this.place].value);
            }
            else if (lex[this.place].code == 20) {
                nots.add(new Integer(args.size()));
            }
            else {
                float tmp2 = 0.0f;
                float tmp3 = 0.0f;
                int rel = 0;
                if (lex[this.place].lcategory == 67) {
                    tmp2 = new Float(lex[this.place].value);
                }
                else if (pointer == 2) {
                    pointer = 0;
                    final int tmp4 = this.vars.indexOf(lex[this.place].value);
                    final int type2 = this.var_types.get(tmp4);
                    if (type2 == 25) {
                        tmp2 = this.var_vals.get(tmp4);
                    }
                    else {
                        tmp2 = this.var_vals.get(tmp4);
                    }
                }
                ++this.place;
                rel = lex[this.place].code;
                ++this.place;
                if (lex[this.place].lcategory == 67) {
                    tmp3 = new Float(lex[this.place].value);
                }
                else if (lex[this.place].code == 61) {
                    final int tmp4 = this.vars.indexOf(lex[this.place].value);
                    final int type2 = this.var_types.get(tmp4);
                    if (type2 == 25) {
                        tmp3 = this.var_vals.get(tmp4);
                    }
                    else {
                        tmp3 = this.var_vals.get(tmp4);
                    }
                }
                boolean bool = false;
                if (rel == 34) {
                    bool = (tmp2 == tmp3);
                }
                else if (rel == 35) {
                    bool = (tmp2 != tmp3);
                }
                else if (rel == 36) {
                    bool = (tmp2 > tmp3);
                }
                else if (rel == 37) {
                    bool = (tmp2 >= tmp3);
                }
                else if (rel == 38) {
                    bool = (tmp2 < tmp3);
                }
                else if (rel == 39) {
                    bool = (tmp2 <= tmp3);
                }
                args.add(new Boolean(bool));
            }
            ++this.place;
        }
        for (int i = 0; i < nots.size(); ++i) {
            final int neg = nots.get(i);
            final boolean temp = args.get(neg);
            args.set(neg, new Boolean(!temp));
        }
        for (int i = 0; i < oper.size(); ++i) {
            if (oper.get(i).equals("AND")) {
                final boolean tmp5 = args.get(i);
                final boolean tmp6 = args.get(i + 1);
                args.set(i, new Boolean(tmp5 && tmp6));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
        }
        for (int i = 0; i < oper.size(); ++i) {
            if (oper.get(i).equals("OR")) {
                final boolean tmp5 = args.get(i);
                final boolean tmp6 = args.get(i + 1);
                args.set(i, new Boolean(tmp5 || tmp6));
                args.remove(i + 1);
                oper.remove(i);
                --i;
            }
        }
        return args.get(0);
    }
    
    private void execute(final ArrayList al) {
        for (int i = 0; i < al.size(); ++i) {
            this.execLexem(al.get(i));
        }
    }
    
    private void execLexem(final ExecLexem el) {
        if (el.type.equals("IASSOC")) {
            this.execInt(el);
        }
        else if (el.type.equals("RASSOC")) {
            this.execReal(el);
        }
        else if (el.type.equals("BASSOC")) {
            this.execBool(el);
        }
        else if (el.type.equals("READ")) {
            this.execRead(el);
        }
        else if (el.type.equals("WRITE")) {
            this.execWrite(el);
        }
        else if (el.type.equals("WHILE")) {
            this.execWhile(el);
        }
        else if (el.type.equals("IF")) {
            this.execIf(el);
        }
    }
    
    private void execInt(final ExecLexem el) {
        final int tmp = this.vars.indexOf(el.variable);
        this.place = 0;
        this.var_vals.set(tmp, new Integer(this.evalInt(el.lexems)));
    }
    
    private void execReal(final ExecLexem el) {
        final int tmp = this.vars.indexOf(el.variable);
        this.place = 0;
        this.var_vals.set(tmp, new Float(this.evalReal(el.lexems)));
    }
    
    private void execBool(final ExecLexem el) {
        final int tmp = this.vars.indexOf(el.variable);
        this.place = 0;
        this.var_vals.set(tmp, new Boolean(this.evalBool(el.lexems)));
    }
    
    private void execWhile(final ExecLexem el) {
        this.place = 0;
        while (this.evalBool(el.lexems)) {
            this.execute(el.iftrue);
            this.place = 0;
        }
    }
    
    private void execIf(final ExecLexem el) {
        this.place = 0;
        if (this.evalBool(el.lexems)) {
            this.execute(el.iftrue);
        }
        else {
            this.execute(el.ifelse);
        }
    }
    
    private void execRead(final ExecLexem el) {
        final int tmp = this.vars.indexOf(el.lexems[1].value);
        final int type = this.var_types.get(tmp);
        String s = "";
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            s = in.readLine();
        }
        catch (Exception ex) {
            System.out.println("Error during input operatiron !");
        }
        if (type == 25) {
            this.var_vals.set(tmp, new Integer(s));
        }
        else if (type == 26) {
            this.var_vals.set(tmp, new Float(s));
        }
        else {
            this.var_vals.set(tmp, new Boolean(s));
        }
    }
    
    private void execWrite(final ExecLexem el) {
        for (int i = 1; i < el.lexems.length; i += 2) {
            if (el.lexems[i].code == 5) {
                System.out.print(el.lexems[i].value);
            }
            else {
                final int tmp = this.vars.indexOf(el.lexems[i].value);
                final int type = this.var_types.get(tmp);
                String s = "";
                if (type == 25) {
                    s = this.var_vals.get(tmp).toString();
                }
                else if (type == 26) {
                    s = this.var_vals.get(tmp).toString();
                }
                else {
                    s = this.var_vals.get(tmp).toString();
                }
                System.out.print(s);
            }
        }
        System.out.println();
    }
}
