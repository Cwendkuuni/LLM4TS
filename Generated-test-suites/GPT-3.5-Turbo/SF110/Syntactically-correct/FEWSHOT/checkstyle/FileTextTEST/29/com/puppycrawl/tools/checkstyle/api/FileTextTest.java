package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;
import java.nio.charset.*;
import java.io.*;

public class FileTextTest {
    
    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File textFile = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(textFile, charsetName);
        
        assertEquals(textFile, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertEquals("", fileText.getFullText());
        assertArrayEquals(new String[]{}, fileText.toLinesArray());
        assertEquals(0, fileText.size());
    }
    
    @Test
    public void testConstructorWithFileText() throws IOException {
        File textFile = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(textFile, charsetName);
        FileText fileText = new FileText(originalFileText);
        
        assertEquals(originalFileText.getFile(), fileText.getFile());
        assertEquals(originalFileText.getCharset(), fileText.getCharset());
        assertEquals(originalFileText.getFullText(), fileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), fileText.toLinesArray());
        assertEquals(originalFileText.size(), fileText.size());
    }
    
    @Test
    public void testStaticMethodFromLines() {
        File textFile = new File("test.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(textFile, lines);
        
        assertEquals(textFile, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line1\nline2\nline3\n", fileText.getFullText());
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }
    
    @Test
    public void testGetLineColumn() throws IOException {
        File textFile = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(textFile, charsetName);
        
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(1));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(2));
    }
    
    @Test
    public void testGet() throws IOException {
        File textFile = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(textFile, charsetName);
        
        assertEquals("", fileText.get(0));
        assertNull(fileText.get(1));
    }
    
    @Test
    public void testSize() throws IOException {
        File textFile = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(textFile, charsetName);
        
        assertEquals(1, fileText.size());
    }
}