
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() {
        // Typical use case
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals("file member should be set correctly", file, fileText.getFile());
            assertEquals("charset member should be set correctly", Charset.forName(charsetName), fileText.getCharset());
            
            String expectedFullText = "This is a sample text file.\n" +
                                     "It has multiple lines.\n" +
                                     "Testing different encodings.";
            assertEquals("fullText member should contain the correct content", expectedFullText, fileText.getFullText().toString());
            
            String[] expectedLines = {"This is a sample text file.", "It has multiple lines.", "Testing different encodings."};
            assertArrayEquals("lines member should contain the correct content", expectedLines, fileText.toLinesArray());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Null case
        try {
            FileText fileText = new FileText(null, null);
            
            assertNull("file member should be null", fileText.getFile());
            assertNull("charset member should be null", fileText.getCharset());
            assertNull("fullText member should be null", fileText.getFullText());
            assertNull("lines member should be null", fileText.toLinesArray());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Error scenario: Unsupported charset
        try {
            FileText fileText = new FileText(file, "InvalidCharset");
            
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Exception message should be correct", "Unsupported charset: InvalidCharset", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception type thrown: " + e.getClass().getSimpleName());
        }
        
        // Error scenario: Nonexistent file
        try {
            FileText fileText = new FileText(new File("nonexistent.txt"), charsetName);
            
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            assertEquals("Exception message should be correct", "nonexistent.txt (No such file or directory)", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception type thrown: " + e.getClass().getSimpleName());
        }
        
        // Error scenario: Malformed input
        try {
            FileText fileText = new FileText(file, "ISO-8859-1");
            
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Malformed input should throw an IOException, not an IllegalStateException like unsupported charset
        } catch (Exception e) {
            fail("Wrong exception type thrown: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testFileTextCopyConstructor() {
        // Typical use case
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText originalFileText = new FileText(file, charsetName);
            FileText copiedFileText = new FileText(originalFileText);
            
            assertNotSame("file member should be a different reference", originalFileText.getFile(), copiedFileText.getFile());
            assertNotSame("charset member should be a different reference", originalFileText.getCharset(), copiedFileText.getCharset());
            assertNotSame("fullText member should be a different reference", originalFileText.getFullText(), copiedFileText.getFullText());
            assertNotSame("lines member should be a different reference", originalFileText.toLinesArray(), copiedFileText.toLinesArray());
            
            assertEquals("file member should have the same value", originalFileText.getFile(), copiedFileText.getFile());
            assertEquals("charset member should have the same value", originalFileText.getCharset(), copiedFileText.getCharset());
            assertEquals("fullText member should have the same value", originalFileText.getFullText().toString(), copiedFileText.getFullText().toString());
            assertArrayEquals("lines member should have the same value", originalFileText.toLinesArray(), copiedFileText.toLinesArray());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Null case
        try {
            FileText fileText = new FileText(null);
            
            assertNull("file member should be null", fileText.getFile());
            assertNull("charset member should be null", fileText.getCharset());
            assertNull("fullText member should be null", fileText.getFullText());
            assertNull("lines member should be null", fileText.toLinesArray());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFileTextFromLines() {
        // Typical use case with ArrayList
        File file = new File("test.txt");
        List<String> lines = new ArrayList<String>();
        
        lines.add("This is line 1");
        lines.add("This is line 2");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertSame("file member should be the same reference", file, fileText.getFile());
        assertNull("charset member should be null", fileText.getCharset());
        assertEquals("fullText member should contain the correct content", "This is line 1\nThis is line 2\n", fileText.getFullText().toString());
        assertArrayEquals("lines member should contain the correct content", new String[]{"This is line 1", "This is line 2"}, fileText.toLinesArray());
        
        // Typical use case with FileText instance
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copiedFileText = FileText.fromLines(file, originalFileText);
        
        assertSame("file member should be the same reference", originalFileText.getFile(), copiedFileText.getFile());
        assertNull("charset member should be null", copiedFileText.getCharset());
        assertEquals("fullText member should contain the correct content", originalFileText.getFullText().toString(), copiedFileText.getFullText().toString());
        assertArrayEquals("lines member should contain the correct content", originalFileText.toLinesArray(), copiedFileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals("get() should return the same file", file, fileText.getFile());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals("getCharset() should return the same charset", Charset.forName(charsetName), fileText.getCharset());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            String expectedFullText = "This is a sample text file.\n" +
                                     "It has multiple lines.\n" +
                                     "Testing different encodings.";
            assertEquals("getFullText() should return the same fullText", expectedFullText, fileText.getFullText().toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            String[] expectedLines = {"This is a sample text file.", "It has multiple lines.", "Testing different encodings."};
            assertArrayEquals("toLinesArray() should return the same lines array", expectedLines, fileText.toLinesArray());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            int[] lineBreaks = fileText.findLineBreaks();
            
            int[] expectedLineBreaks = {0, 27, 47, 73};
            assertArrayEquals("findLineBreaks() should return the correct line breaks array", expectedLineBreaks, lineBreaks);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            // Typical use case
            LineColumn lc1 = fileText.lineColumn(15);
            assertEquals("lineColumn() should return the correct line number", 1, lc1.getLine());
            assertEquals("lineColumn() should return the correct column number", 15, lc1.getColumn());
            
            LineColumn lc2 = fileText.lineColumn(34);
            assertEquals("lineColumn() should return the correct line number", 2, lc2.getLine());
            assertEquals("lineColumn() should return the correct column number", 6, lc2.getColumn());
            
            LineColumn lc3 = fileText.lineColumn(78);
            assertEquals("lineColumn() should return the correct line number", 3, lc3.getLine());
            assertEquals("lineColumn() should return the correct column number", 14, lc3.getColumn());
            
            // Error scenario: Position outside of any line
            LineColumn lc4 = fileText.lineColumn(100);
            assertEquals("lineColumn() should return the correct line number", 3, lc4.getLine());
            assertEquals("lineColumn() should return the correct column number", 27, lc4.getColumn());
            
            // Error scenario: Position at line break
            LineColumn lc5 = fileText.lineColumn(27);
            assertEquals("lineColumn() should return the correct line number", 2, lc5.getLine());
            assertEquals("lineColumn() should return the correct column number", 0, lc5.getColumn());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGet() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals("get() should return the correct line", "This is a sample text file.", fileText.get(0));
            assertEquals("get() should return the correct line", "It has multiple lines.", fileText.get(1));
            assertEquals("get() should return the correct line", "Testing different encodings.", fileText.get(2));
            
            // Error scenario: Invalid line number
            try {
                fileText.get(3);
                
                fail("Expected IndexOutOfBoundsException to be thrown");
            } catch (IndexOutOfBoundsException e) {
                // Expected exception
            } catch (Exception e) {
                fail("Wrong exception type thrown: " + e.getClass().getSimpleName());
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals("size() should return the correct number of lines", 3, fileText.size());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}

