package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FileTextTest {
    @Test
    public void testFileTextConstructor() {
        // Sample file and charsetName
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        
        // Initialize FileText object
        FileText fileText = new FileText(file, charsetName);
        
        // Verify that the file, charset, fullText, and lines are correctly set
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        // Test an example line
        assertEquals("This is line 1.", fileText.get(0));
        // Test the number of lines
        assertEquals(3, fileText.size());
        // Test the full text
        assertEquals("This is line 1.\nThis is line 2.\nThis is line 3.", fileText.getFullText().toString());
    }

    @Test
    public void testFileTextCopyConstructor() {
        // Sample FileText object
        FileText original = new FileText(new File("testFile.txt"), "UTF-8");
        
        // Initialize a copy of the FileText object
        FileText copy = new FileText(original);
        
        // Verify that the copy has the same file, charset, fullText, and lines as the original
        assertEquals(original.getFile(), copy.getFile());
        assertEquals(original.getCharset(), copy.getCharset());
        assertEquals(original.getFullText(), copy.getFullText());
        assertEquals(original.size(), copy.size());
        
        // Verify that the lines are deep copied
        assertEquals(original.get(0), copy.get(0));
        assertEquals(original.get(1), copy.get(1));
        assertEquals(original.get(2), copy.get(2));
    }
    
    @Test
    public void testFileTextFromLines() {
        // Sample file and lines
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("This is line 1.");
        lines.add("This is line 2.");
        lines.add("This is line 3.");
        
        // Initialize FileText object from lines
        FileText fileText = FileText.fromLines(file, lines);
        
        // Verify that the file, charset, fullText, and lines are correctly set
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        // Test an example line
        assertEquals("This is line 1.", fileText.get(0));
        // Test the number of lines
        assertEquals(3, fileText.size());
        // Test the full text
        assertEquals("This is line 1.\nThis is line 2.\nThis is line 3.", fileText.getFullText().toString());
    }
    
    @Test
    public void testLineColumn() {
        // Sample FileText object
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        
        // Test lineColumn method for zero-based index
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(16));
        assertEquals(new LineColumn(3, 17), fileText.lineColumn(37));
    }
}