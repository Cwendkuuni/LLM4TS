package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstructorWithUnsupportedCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "InvalidCharset";
        
        FileText fileText = new FileText(file, charsetName);
    }
    
    @Test
    public void testConstructorWithFileText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        
        FileText fileText = new FileText(originalFileText);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(originalFileText.getCharset(), fileText.getCharset());
        assertEquals(originalFileText.getFullText(), fileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), fileText.toLinesArray());
    }
    
    @Test
    public void testConstructorWithFileAndLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<String>();
        lines.add("line 1");
        lines.add("line 2");
        lines.add("line 3");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }
    
    @Test
    public void testReadFile() throws IOException {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        String expectedContent = "Test Content";
        
        FileUtils.write(file, expectedContent, charset);
        
        FileText fileText = new FileText(file, charset.name());
        
        assertEquals(expectedContent, fileText.getFullText().toString());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testReadNonExistingFile() throws IOException {
        File file = new File("nonexisting.txt");
        String charsetName = "UTF-8";
        
        FileText fileText = new FileText(file, charsetName);
    }
    
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(file, fileText.getFile());
    }
    
    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }
    
    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText.getFullText());
    }
    
    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText.toLinesArray());
    }
    
    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText.findLineBreaks());
    }
    
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        LineColumn lineColumn = fileText.lineColumn(0);
        
        assertNotNull(lineColumn);
        assertEquals(1, lineColumn.getLine());
        assertEquals(0, lineColumn.getColumn());
    }
    
    @Test
    public void testGet() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(fileText.get(0), fileText.toLinesArray()[0]);
    }
    
    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(fileText.size(), fileText.toLinesArray().length);
    }
}