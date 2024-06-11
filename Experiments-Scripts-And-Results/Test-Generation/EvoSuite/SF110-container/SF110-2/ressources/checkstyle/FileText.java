package com.puppycrawl.tools.checkstyle.api;

import java.nio.charset.*;
import com.google.common.io.*;
import java.util.regex.*;
import java.util.*;
import java.io.*;

public final class FileText extends AbstractList<String>
{
    private static final int READ_BUFFER_SIZE = 1024;
    private static final Pattern LINE_TERMINATOR;
    private final File file;
    private final Charset charset;
    private final String fullText;
    private final String[] lines;
    private int[] lineBreaks;
    
    public FileText(final File file, final String charsetName) throws IOException {
        this.file = file;
        CharsetDecoder decoder;
        try {
            this.charset = Charset.forName(charsetName);
            decoder = this.charset.newDecoder();
            decoder.onMalformedInput(CodingErrorAction.REPLACE);
            decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        }
        catch (UnsupportedCharsetException ex) {
            final String message = "Unsupported charset: " + charsetName;
            throw new IllegalStateException(message, ex);
        }
        this.fullText = readFile(file, decoder);
        final ArrayList<String> textLines = new ArrayList<String>();
        final BufferedReader reader = new BufferedReader(new StringReader(this.fullText));
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            textLines.add(line);
        }
        this.lines = textLines.toArray(new String[textLines.size()]);
    }
    
    public FileText(final FileText fileText) {
        this.file = fileText.file;
        this.charset = fileText.charset;
        this.fullText = fileText.fullText;
        this.lines = fileText.lines.clone();
        if (fileText.lineBreaks == null) {
            this.lineBreaks = null;
        }
        else {
            this.lineBreaks = fileText.lineBreaks.clone();
        }
    }
    
    private FileText(final File file, final List<String> lines) {
        final StringBuilder buf = new StringBuilder();
        for (final String line : lines) {
            buf.append(line).append('\n');
        }
        buf.trimToSize();
        this.file = file;
        this.charset = null;
        this.fullText = buf.toString();
        this.lines = lines.toArray(new String[lines.size()]);
    }
    
    private static String readFile(final File inputFile, final CharsetDecoder decoder) throws IOException {
        if (!inputFile.exists()) {
            throw new FileNotFoundException(inputFile.getPath() + " (No such file or directory)");
        }
        final StringBuilder buf = new StringBuilder();
        final FileInputStream stream = new FileInputStream(inputFile);
        final Reader reader = new InputStreamReader(stream, decoder);
        try {
            final char[] chars = new char[1024];
            while (true) {
                final int len = reader.read(chars);
                if (len < 0) {
                    break;
                }
                buf.append(chars, 0, len);
            }
        }
        finally {
            Closeables.closeQuietly(reader);
        }
        return buf.toString();
    }
    
    public static FileText fromLines(final File file, final List<String> lines) {
        if (lines instanceof FileText) {
            return (FileText)lines;
        }
        return new FileText(file, lines);
    }
    
    public File getFile() {
        return this.file;
    }
    
    public Charset getCharset() {
        return this.charset;
    }
    
    public CharSequence getFullText() {
        return this.fullText;
    }
    
    public String[] toLinesArray() {
        return this.lines.clone();
    }
    
    private int[] findLineBreaks() {
        if (this.lineBreaks == null) {
            final int[] lineBreakPositions = new int[this.size() + 1];
            lineBreakPositions[0] = 0;
            int lineNo = 1;
            final Matcher matcher = FileText.LINE_TERMINATOR.matcher(this.fullText);
            while (matcher.find()) {
                lineBreakPositions[lineNo] = matcher.end();
                ++lineNo;
            }
            if (lineNo < lineBreakPositions.length) {
                lineBreakPositions[lineNo] = this.fullText.length();
            }
            this.lineBreaks = lineBreakPositions;
        }
        return this.lineBreaks;
    }
    
    public LineColumn lineColumn(final int pos) {
        final int[] lineBreakPositions = this.findLineBreaks();
        int lineNo = Arrays.binarySearch(lineBreakPositions, pos);
        if (lineNo < 0) {
            lineNo = -lineNo - 2;
        }
        final int startOfLine = lineBreakPositions[lineNo];
        final int columnNo = pos - startOfLine;
        return new LineColumn(lineNo + 1, columnNo);
    }
    
    @Override
    public String get(final int lineNo) {
        return this.lines[lineNo];
    }
    
    @Override
    public int size() {
        return this.lines.length;
    }
    
    static {
        LINE_TERMINATOR = Pattern.compile("\\n|\\r\\n?");
    }
}
