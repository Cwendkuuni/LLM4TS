
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileTextTest {
    
    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstructorWithUnsupportedCharsetName() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "unsupportedCharset";
        
        FileText fileText = new FileText(file, charsetName);
    }
    
    @Test
    public void testConstructorWithFileText() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        
        FileText copiedFileText = new FileText(originalFileText);
        
        assertNotNull(copiedFileText);
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
        assertNotSame(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
    }
    
    @Test
    public void testConstructorWithFileAndLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line1\nline2\nline3\n", fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }
    
    @Test
    public void testReadFile() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        String expectedFullText = "This is a test file.\nIt has multiple lines.\nFor testing purposes.";
        String actualFullText = fileText.getFullText().toString();
        
        assertEquals(expectedFullText, actualFullText);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testReadFileWithNonExistingFile() throws IOException {
        File file = new File("nonExistingFile.txt");
        String charsetName = "UTF-8";
        
        FileText fileText = new FileText(file, charsetName);
    }
    
    @Test
    public void testGetLineColumn() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        LineColumn lineColumn = fileText.lineColumn(10);
        
        assertEquals(2, lineColumn.getLine());
        assertEquals(5, lineColumn.getColumn());
    }
    
    @Test
    public void testGetLine() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        String line = fileText.get(1);
        
        assertEquals("It has multiple lines.", line);
    }
    
    @Test
    public void testGetSize() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        int size = fileText.size();
        
        assertEquals(3, size);
    }
}
