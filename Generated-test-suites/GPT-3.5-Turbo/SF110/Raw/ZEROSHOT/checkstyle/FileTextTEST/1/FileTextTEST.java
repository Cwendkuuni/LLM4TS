package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import java.nio.charset.*;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws Exception {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        fileText = FileText.fromLines(file, lines);
    }

    @Test
    public void testGetFile() {
        File expectedFile = new File("test.txt");
        assertEquals(expectedFile, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        Charset expectedCharset = null;
        assertEquals(expectedCharset, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        CharSequence expectedFullText = "Line 1\nLine 2\nLine 3\n";
        assertEquals(expectedFullText, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        String[] expectedLines = {"Line 1", "Line 2", "Line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        int pos = 7;
        LineColumn expectedLineColumn = new LineColumn(1, 6);
        assertEquals(expectedLineColumn, fileText.lineColumn(pos));
    }

    @Test
    public void testGet() {
        String expectedLine = "Line 2";
        assertEquals(expectedLine, fileText.get(1));
    }

    @Test
    public void testSize() {
        int expectedSize = 3;
        assertEquals(expectedSize, fileText.size());
    }
  
    @Test
    public void testConstructFileText1() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
        assertEquals(new LineColumn(1, 6), fileText.lineColumn(7));
        assertEquals("Line 2", fileText.get(1));
        assertEquals(3, fileText.size());
    }
  
    @Test(expected = IllegalStateException.class)
    public void testConstructFileText2() throws IOException {
        File file = new File("test.txt");
        String invalidCharsetName = "InvalidCharset";
        new FileText(file, invalidCharsetName);
    }
  
    @Test(expected = FileNotFoundException.class)
    public void testConstructFileText3() throws IOException {
        File nonExistingFile = new File("non_existing_file.txt");
        String charsetName = "UTF-8";
        new FileText(nonExistingFile, charsetName);
    }
  
    @Test
    public void testConstructorCopy() {
        FileText copy = new FileText(fileText);
        assertEquals(fileText.getFile(), copy.getFile());
        assertEquals(fileText.getCharset(), copy.getCharset());
        assertEquals(fileText.getFullText(), copy.getFullText());
        assertArrayEquals(fileText.toLinesArray(), copy.toLinesArray());
        assertEquals(fileText.lineColumn(7), copy.lineColumn(7));
        assertEquals(fileText.get(1), copy.get(1));
        assertEquals(fileText.size(), copy.size());
    }
  
    @Test
    public void testFromFileText() {
        List<String> lines = Arrays.asList("Line 4", "Line 5");
        FileText fromLinesFileText = FileText.fromLines(fileText.getFile(), lines);
        assertEquals(fileText.getFile(), fromLinesFileText.getFile());
        assertEquals(fileText.getCharset(), fromLinesFileText.getCharset());
        assertEquals("Line 4\nLine 5\n", fromLinesFileText.getFullText().toString());
        assertArrayEquals(new String[]{"Line 4", "Line 5"}, fromLinesFileText.toLinesArray());
        assertEquals(new LineColumn(1, 5), fromLinesFileText.lineColumn(6));
        assertEquals("Line 4", fromLinesFileText.get(0));
        assertEquals(2, fromLinesFileText.size());
        assertSame(fromLinesFileText, FileText.fromLines(fileText.getFile(), fromLinesFileText));
        assertNotSame(fromLinesFileText, FileText.fromLines(fileText.getFile(), new ArrayList<>()));
    }
  
    @Test
    public void testReadFile() throws IOException {
        File file = new File("test.txt");
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        String expectedText = "Line 1\nLine 2\nLine 3\n";
        assertEquals(expectedText, FileText.readFile(file, decoder));
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testReadFileThrowsFileNotFoundException() throws IOException {
        File nonExistingFile = new File("non_existing_file.txt");
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        FileText.readFile(nonExistingFile, decoder);
    }
    
    @Test
    public void testFindLineBreaks() {
        int[] expectedLineBreaks = {0, 7, 14, 15};
        assertArrayEquals(expectedLineBreaks, fileText.findLineBreaks());
        
        File emptyFile = new File("empty.txt");
        FileText emptyFileText = new FileText(emptyFile, "UTF-8");
        int[] expectedEmptyLineBreaks = {0};
        assertArrayEquals(expectedEmptyLineBreaks, emptyFileText.findLineBreaks());
    }
    
    @Test
    public void testLineColumnNotFound() {
        int pos = 8;
        LineColumn expectedLineColumn = new LineColumn(2, 2);
        assertEquals(expectedLineColumn, fileText.lineColumn(pos));
        
        int posNotFound = 100;
        LineColumn expectedLineColumnNotFound = new LineColumn(4, 100);
        assertEquals(expectedLineColumnNotFound, fileText.lineColumn(posNotFound));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        fileText.get(10);
    }
    
    @Test
    public void testEmptyFileText() {
        File emptyFile = new File("empty.txt");
        FileText emptyFileText = new FileText(emptyFile, "UTF-8");
        assertNotNull(emptyFileText.getFile());
        assertNull(emptyFileText.getCharset());
        assertEquals("", emptyFileText.getFullText().toString());
        assertArrayEquals(new String[]{}, emptyFileText.toLinesArray());
        assertEquals(new LineColumn(1, 0), emptyFileText.lineColumn(0));
        assertEquals("", emptyFileText.get(0));
        assertEquals(0, emptyFileText.size());
    }
  
    @After
    public void tearDown() throws Exception {
        fileText = null;
    }

}