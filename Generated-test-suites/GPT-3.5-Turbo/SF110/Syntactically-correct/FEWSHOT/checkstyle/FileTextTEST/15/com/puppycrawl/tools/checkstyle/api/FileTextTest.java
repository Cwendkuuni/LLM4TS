package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnsupportedCharsetException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.io.Closeables;

public class FileTextTest {
    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertNotNull(fileText.findLineBreaks());
        
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertEquals(readFile(file, fileText.getCharset().newDecoder()), fileText.getFullText().toString());
        assertEquals(readLinesFromFile(file, fileText.getCharset()), Arrays.asList(fileText.toLinesArray()));
        assertEquals(countLinesInFile(file), fileText.size());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstructorWithUnsupportedCharset() throws IOException {
        File file = new File("test.txt");
        String charsetName = "InvalidCharset";
        new FileText(file, charsetName);
    }
    
    @Test
    public void testConstructorWithFileText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        FileText fileText = new FileText(originalFileText);
        
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertNotNull(fileText.findLineBreaks());
        
        assertEquals(originalFileText.getFile(), fileText.getFile());
        assertEquals(originalFileText.getCharset(), fileText.getCharset());
        assertEquals(originalFileText.getFullText(), fileText.getFullText());
        assertEquals(Arrays.asList(originalFileText.toLinesArray()), Arrays.asList(fileText.toLinesArray()));
        assertEquals(originalFileText.size(), fileText.size());
    }
    
    @Test
    public void testConstructorWithFileAndLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertNotNull(fileText.findLineBreaks());
        
        assertEquals(file, fileText.getFile());
        assertEquals(String.join("\n", lines), fileText.getFullText().toString());
        assertEquals(lines, Arrays.asList(fileText.toLinesArray()));
        assertEquals(lines.size(), fileText.size());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testReadFileWhenFileDoesNotExist() throws IOException {
        File file = new File("nonexistent.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText.readFile(file, charset.newDecoder());
    }
    
    @Test
    public void testReadFileWhenFileExists() throws IOException {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        String expectedContent = "line1\nline2\nline3";
        writeFileContent(file, expectedContent);
        
        String actualContent = FileText.readFile(file, charset.newDecoder());
        
        assertEquals(expectedContent, actualContent);
    }
    
    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int[] expectedLineBreaks = calculateLineBreaks(fileText.getFullText().toString());
        
        int[] actualLineBreaks = fileText.findLineBreaks();
        
        assertEquals(expectedLineBreaks.length, actualLineBreaks.length);
        for (int i = 0; i < expectedLineBreaks.length; i++) {
            assertEquals(expectedLineBreaks[i], actualLineBreaks[i]);
        }
    }
    
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        LineColumn lineColumn1 = fileText.lineColumn(0);
        LineColumn lineColumn2 = fileText.lineColumn(6);
        LineColumn lineColumn3 = fileText.lineColumn(12);
        LineColumn lineColumn4 = fileText.lineColumn(18);
        
        assertEquals(1, lineColumn1.getLineNo());
        assertEquals(0, lineColumn1.getColumnNo());
        
        assertEquals(1, lineColumn2.getLineNo());
        assertEquals(6, lineColumn2.getColumnNo());
        
        assertEquals(2, lineColumn3.getLineNo());
        assertEquals(0, lineColumn3.getColumnNo());
        
        assertEquals(2, lineColumn4.getLineNo());
        assertEquals(6, lineColumn4.getColumnNo());
    }
    
    private String readFile(File inputFile, CharsetDecoder decoder) throws IOException {
        if (!inputFile.exists()) {
            throw new FileNotFoundException(inputFile.getPath() + " (No such file or directory)");
        }
        StringBuilder buf = new StringBuilder();
        FileInputStream stream = new FileInputStream(inputFile);
        Reader reader = new InputStreamReader(stream, decoder);
        try {
            char[] chars = new char[1024];
            int len;
            while ((len = reader.read(chars)) >= 0) {
                buf.append(chars, 0, len);
            }
        } finally {
            Closeables.closeQuietly(reader);
        }
        return buf.toString();
    }
    
    private List<String> readLinesFromFile(File file, Charset charset) throws IOException {
        List<String> lines = new ArrayList<>();
        FileInputStream stream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        stream.close();
        return lines;
    }
    
    private int countLinesInFile(File file) throws IOException {
        int lineCount = 0;
        FileInputStream stream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while (reader.readLine() != null) lineCount++;
        reader.close();
        stream.close();
        return lineCount;
    }
    
    private void writeFileContent(File file, String content) throws IOException {
        StringBuilder buf = new StringBuilder(content);
        FileOutputStream stream = new FileOutputStream(file);
        
        try {
            stream.write(buf.toString().getBytes());
        } finally {
            stream.close();
        }
    }
    
    private int[] calculateLineBreaks(String text) {
        List<Integer> lineBreakPositions = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\n|\\r\\n?");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            lineBreakPositions.add(matcher.end());
        }
        lineBreakPositions.add(text.length());
        
        int[] lineBreaks = new int[lineBreakPositions.size()];
        for (int i = 0; i < lineBreaks.length; i++) {
            lineBreaks[i] = lineBreakPositions.get(i);
        }
        
        return lineBreaks;
    }
}