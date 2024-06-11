package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import static org.junit.Assert.*;

public class FileTextTest {
    private FileText fileText;
    private File file;
    private String charsetName;
    
    @Before
    public void setUp() throws IOException {
        // Create a temporary file for testing
        file = File.createTempFile("testFile", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("This is line 1\n");
        writer.write("This is line 2\n");
        writer.write("This is line 3\n");
        writer.close();
        
        charsetName = "UTF-8"; // Setting charset as UTF-8 for testing
        fileText = new FileText(file, charsetName);
    }
    
    @Test
    public void testConstructorWithFileAndCharsetName() {
        // Check that the fileText object is not null
        assertNotNull(fileText);
        
        // Check that the returned file is the same as the original file
        assertEquals(file, fileText.getFile());
        
        // Check that the returned charset is the same as the provided charsetName
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        
        // Check that the returned full text is the same as the content of the original file
        assertEquals("This is line 1\nThis is line 2\nThis is line 3\n", fileText.getFullText().toString());
        
        // Check that the returned lines array is correct
        String[] expectedLines = { "This is line 1", "This is line 2", "This is line 3" };
        assertArrayEquals(expectedLines, fileText.toLinesArray());
        
        // Check that the size of the lines array is correct
        assertEquals(3, fileText.size());
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText copiedFileText = new FileText(fileText);
        
        // Check that the copied fileText object is not null
        assertNotNull(copiedFileText);
        
        // Check that the copied fileText object is equal to the original fileText object
        assertEquals(fileText, copiedFileText);
        
        // Check that the copied line array is not the same object reference as the original line array
        assertNotSame(fileText.toLinesArray(), copiedFileText.toLinesArray());
    }
    
    @Test
    public void testConstructorWithFileAndLines() {
        List<String> lines = new ArrayList<>();
        lines.add("This is line 1");
        lines.add("This is line 2");
        lines.add("This is line 3");
        
        FileText createdFileText = FileText.fromLines(file, lines);
        
        // Check that the created fileText object is not null
        assertNotNull(createdFileText);
        
        // Check that the created fileText's file is the same as the provided file
        assertEquals(file, createdFileText.getFile());
        
        // Check that the created fileText's charset is null
        assertNull(createdFileText.getCharset());
        
        // Check that the created fileText's fullText is correct
        assertEquals("This is line 1\nThis is line 2\nThis is line 3\n", createdFileText.getFullText().toString());
        
        // Check that the created fileText's lines array is correct
        assertArrayEquals(new String[] { "This is line 1", "This is line 2", "This is line 3" }, createdFileText.toLinesArray());
        
        // Check that the size of the created fileText's lines array is correct
        assertEquals(3, createdFileText.size());
    }
    
    @Test
    public void testReadFile() throws IOException {
        // Test reading a file that exists
        String content = FileText.readFile(file, Charset.forName(charsetName).newDecoder());
        assertEquals("This is line 1\nThis is line 2\nThis is line 3\n", content);
        
        // Test reading a file that does not exist
        File nonexistentFile = new File("nonexistent.txt");
        try {
            FileText.readFile(nonexistentFile, Charset.forName(charsetName).newDecoder());
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            assertEquals(nonexistentFile.getPath() + " (No such file or directory)", e.getMessage());
        }
    }
    
    @Test
    public void testGetFile() {
        // Check that the returned file is the same as the original file
        assertEquals(file, fileText.getFile());
    }
    
    @Test
    public void testGetCharset() {
        // Check that the returned charset is the same as the provided charsetName
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }
    
    @Test
    public void testGetFullText() {
        // Check that the returned full text is the same as the content of the original file
        assertEquals("This is line 1\nThis is line 2\nThis is line 3\n", fileText.getFullText().toString());
    }
    
    @Test
    public void testToLinesArray() {
        // Check that the returned lines array is correct
        String[] expectedLines = { "This is line 1", "This is line 2", "This is line 3" };
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }
    
    @Test
    public void testFindLineBreaks() {
        // Test finding line breaks in the file text
        int[] expectedLineBreaks = { 0, 14, 30, 47 };
        assertArrayEquals(expectedLineBreaks, fileText.findLineBreaks());
    }
    
    @Test
    public void testLineColumn() {
        // Test getting line and column numbers for various positions in the file text
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 5), fileText.lineColumn(5));
        assertEquals(new LineColumn(1, 11), fileText.lineColumn(11));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(12));
        assertEquals(new LineColumn(2, 5), fileText.lineColumn(17));
        assertEquals(new LineColumn(3, 0), fileText.lineColumn(30));
        assertEquals(new LineColumn(3, 5), fileText.lineColumn(35));
        assertEquals(new LineColumn(3, 11), fileText.lineColumn(41));
    }
    
    @Test
    public void testGet() {
        // Check that the returned line at a given line number is correct
        assertEquals("This is line 1", fileText.get(0));
        assertEquals("This is line 2", fileText.get(1));
        assertEquals("This is line 3", fileText.get(2));
    }
    
    @Test
    public void testSize() {
        // Check that the returned size of the lines array is correct
        assertEquals(3, fileText.size());
    }
    
    @After
    public void tearDown() {
        // Delete the temporary file after the test
        file.delete();
    }
}