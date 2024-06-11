
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {
    
    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertNotNull(fileText.findLineBreaks());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstructorWithUnsupportedCharset() throws IOException {
        File file = new File("test.txt");
        new FileText(file, "InvalidCharset");
    }
    
    @Test
    public void testCopyConstructor() throws IOException {
        File file = new File("test.txt");
        FileText original = new FileText(file, "UTF-8");
        FileText fileText = new FileText(original);
        
        assertEquals(original.getFile(), fileText.getFile());
        assertEquals(original.getCharset(), fileText.getCharset());
        assertEquals(original.getFullText(), fileText.getFullText());
        assertEquals(original.toLinesArray(), fileText.toLinesArray());
        assertEquals(original.findLineBreaks(), fileText.findLineBreaks());
    }
    
    @Test
    public void testConstructorWithFileAndLinesList() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>(Arrays.asList("line1", "line2", "line3"));
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testReadFileWithNonExistingFile() throws IOException {
        File file = new File("nonexisting.txt");
        FileText.readFile(file, "UTF-8");
    }
    
    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>(Arrays.asList("line1", "line2", "line3"));
        FileText fileText = FileText.fromLines(file, lines);
        
        LineColumn lineColumn = fileText.lineColumn(7);
        assertEquals(2, lineColumn.getLineNo());
        assertEquals(1, lineColumn.getColumnNo());
    }
    
    @Test
    public void testGet() throws IOException {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>(Arrays.asList("line1", "line2", "line3"));
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals("line2", fileText.get(1));
    }
    
    @Test
    public void testSize() throws IOException {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>(Arrays.asList("line1", "line2", "line3"));
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals(3, fileText.size());
    }
}
