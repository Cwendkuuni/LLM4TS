package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import org.junit.Test;

public class FileTextTest {
    
    // Testing constructor with valid file and charsetName
    @Test
    public void testConstructor() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        
        String expectedText = "";
        try (Scanner scanner = new Scanner(file)) {
            expectedText = scanner.useDelimiter("\\A").next();
            scanner.close();
        }
        
        assertEquals(expectedText, fileText.getFullText().toString());
        
        String[] expectedLines = expectedText.split("\\r?\\n");
        assertArrayEquals(expectedLines, fileText.toLinesArray());
        assertEquals(expectedLines.length, fileText.size());
    }
    
    // Testing constructor with unsupported charsetName
    @Test(expected = IllegalStateException.class)
    public void testConstructor_UnsupportedCharset() throws IOException {
        File file = new File("test.txt");
        String charsetName = "unsupportedCharset";
        new FileText(file, charsetName);
    }
    
    // Testing constructor with non-existing file
    @Test(expected = FileNotFoundException.class)
    public void testConstructor_NoSuchFile() throws IOException {
        File file = new File("nonExistingFile.txt");
        String charsetName = "UTF-8";
        new FileText(file, charsetName);
    }

    // Testing copy constructor
    @Test
    public void testCopyConstructor() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText1 = new FileText(file, charsetName);
        FileText fileText2 = new FileText(fileText1);
        
        assertEquals(fileText1.getFile(), fileText2.getFile());
        assertEquals(fileText1.getCharset(), fileText2.getCharset());
        assertEquals(fileText1.getFullText().toString(), fileText2.getFullText().toString());
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
        assertEquals(fileText1.size(), fileText2.size());
    }
    
    // Testing fromLines method with List of Strings
    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line1");
        lines.add("line2");
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        
        String expectedText = "line1\nline2\n";
        assertEquals(expectedText, fileText.getFullText().toString());
        
        String[] expectedLines = {"line1", "line2"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
        assertEquals(expectedLines.length, fileText.size());
    }
    
    // Testing lineColumn method
    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        int pos = 10;
        FileText.LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(1, lineColumn.getLine());
        assertEquals(10, lineColumn.getColumn());
        
        pos = 50;
        lineColumn = fileText.lineColumn(pos);
        assertEquals(3, lineColumn.getLine());
        assertEquals(5, lineColumn.getColumn());
    }
    
    // Testing get and size methods
    @Test
    public void testGetAndSize() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals("line1", fileText.get(0));
        assertEquals("line2", fileText.get(1));
        assertEquals("line3", fileText.get(2));
        assertEquals(3, fileText.size());
    }
}