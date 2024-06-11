package com.puppycrawl.tools.checkstyle.api;

import com.puppycrawl.tools.checkstyle.grammars.*;
import java.util.regex.*;
import java.io.*;
import com.google.common.collect.*;
import java.util.*;

public final class FileContents implements CommentListener
{
    private static final String MATCH_SINGLELINE_COMMENT_PAT = "^\\s*//.*$";
    private static final Pattern MATCH_SINGLELINE_COMMENT;
    private final String fileName;
    private final FileText text;
    private final Map<Integer, TextBlock> javadocComments;
    private final Map<Integer, TextBlock> cppComments;
    private final Map<Integer, List<TextBlock>> clangComments;
    
    public FileContents(final String filename, final String... lines) {
        this.javadocComments = (Map<Integer, TextBlock>)Maps.newHashMap();
        this.cppComments = (Map<Integer, TextBlock>)Maps.newHashMap();
        this.clangComments = (Map<Integer, List<TextBlock>>)Maps.newHashMap();
        this.fileName = filename;
        this.text = FileText.fromLines(new File(filename), Arrays.asList(lines));
    }
    
    public FileContents(final FileText text) {
        this.javadocComments = (Map<Integer, TextBlock>)Maps.newHashMap();
        this.cppComments = (Map<Integer, TextBlock>)Maps.newHashMap();
        this.clangComments = (Map<Integer, List<TextBlock>>)Maps.newHashMap();
        this.fileName = text.getFile().toString();
        this.text = new FileText(text);
    }
    
    @Override
    public void reportSingleLineComment(final String type, final int startLineNo, final int startColNo) {
        this.reportCppComment(startLineNo, startColNo);
    }
    
    @Override
    public void reportBlockComment(final String type, final int startLineNo, final int startColNo, final int endLineNo, final int endColNo) {
        this.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
    }
    
    public void reportCppComment(final int startLineNo, final int startColNo) {
        final String line = this.line(startLineNo - 1);
        final String[] txt = { line.substring(startColNo) };
        final Comment comment = new Comment(txt, startColNo, startLineNo, line.length() - 1);
        this.cppComments.put(startLineNo, comment);
    }
    
    public ImmutableMap<Integer, TextBlock> getCppComments() {
        return ImmutableMap.copyOf((Map<? extends Integer, ? extends TextBlock>)this.cppComments);
    }
    
    public void reportCComment(final int startLineNo, final int startColNo, final int endLineNo, final int endColNo) {
        final String[] cComment = this.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
        final Comment comment = new Comment(cComment, startColNo, endLineNo, endColNo);
        if (this.clangComments.containsKey(startLineNo)) {
            final List<TextBlock> entries = this.clangComments.get(startLineNo);
            entries.add(comment);
        }
        else {
            final List<TextBlock> entries = (List<TextBlock>)Lists.newArrayList();
            entries.add(comment);
            this.clangComments.put(startLineNo, entries);
        }
        final String firstLine = this.line(startLineNo - 1);
        if (firstLine.contains("/**") && !firstLine.contains("/**/")) {
            this.javadocComments.put(endLineNo - 1, comment);
        }
    }
    
    public ImmutableMap<Integer, List<TextBlock>> getCComments() {
        return ImmutableMap.copyOf((Map<? extends Integer, ? extends List<TextBlock>>)this.clangComments);
    }
    
    private String[] extractCComment(final int startLineNo, final int startColNo, final int endLineNo, final int endColNo) {
        String[] returnValue;
        if (startLineNo == endLineNo) {
            returnValue = new String[] { this.line(startLineNo - 1).substring(startColNo, endColNo + 1) };
        }
        else {
            returnValue = new String[endLineNo - startLineNo + 1];
            returnValue[0] = this.line(startLineNo - 1).substring(startColNo);
            for (int i = startLineNo; i < endLineNo; ++i) {
                returnValue[i - startLineNo + 1] = this.line(i);
            }
            returnValue[returnValue.length - 1] = this.line(endLineNo - 1).substring(0, endColNo + 1);
        }
        return returnValue;
    }
    
    public TextBlock getJavadocBefore(final int lineNoBefore) {
        int lineNo;
        for (lineNo = lineNoBefore - 2; lineNo > 0 && (this.lineIsBlank(lineNo) || this.lineIsComment(lineNo)); --lineNo) {}
        return this.javadocComments.get(lineNo);
    }
    
    private String line(final int lineNo) {
        return this.text.get(lineNo);
    }
    
    public FileText getText() {
        return new FileText(this.text);
    }
    
    public String[] getLines() {
        return this.text.toLinesArray();
    }
    
    public String getLine(final int index) {
        return this.text.get(index);
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    @Deprecated
    public String getFilename() {
        return this.fileName;
    }
    
    public boolean lineIsBlank(final int lineNo) {
        return this.line(lineNo).trim().isEmpty();
    }
    
    public boolean lineIsComment(final int lineNo) {
        return FileContents.MATCH_SINGLELINE_COMMENT.matcher(this.line(lineNo)).matches();
    }
    
    public boolean hasIntersectionWithComment(final int startLineNo, final int startColNo, final int endLineNo, final int endColNo) {
        return this.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo) || this.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo);
    }
    
    public boolean inPackageInfo() {
        return this.fileName.endsWith("package-info.java");
    }
    
    private boolean hasIntersectionWithCComment(final int startLineNo, final int startColNo, final int endLineNo, final int endColNo) {
        final Collection<List<TextBlock>> values = this.clangComments.values();
        for (final List<TextBlock> row : values) {
            for (final TextBlock comment : row) {
                if (comment.intersects(startLineNo, startColNo, endLineNo, endColNo)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasIntersectionWithCppComment(final int startLineNo, final int startColNo, final int endLineNo, final int endColNo) {
        for (int lineNumber = startLineNo; lineNumber <= endLineNo; ++lineNumber) {
            final TextBlock comment = this.cppComments.get(lineNumber);
            if (comment != null && comment.intersects(startLineNo, startColNo, endLineNo, endColNo)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        MATCH_SINGLELINE_COMMENT = Pattern.compile("^\\s*//.*$");
    }
}
