package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

import static org.junit.Assert.*;

public class FileTextTest {
    
    @Test
    public void testConstructor_withValidCharset_InitiatesObject() {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        
        // Act
        FileText fileText = new FileText(file, charsetName);
        
        // Assert
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(0, fileText.toLinesArray().length);
    }
    
    @Test(expected = IOException.class)
    public void testConstructor_withInvalidCharset_ThrowsIOException() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "InvalidCharset";
        
        // Act
        FileText fileText = new FileText(file, charsetName);
    }
    
    @Test
    public void testConstructor_CopyConstructor_CopiesObject() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        
        // Act
        FileText copiedFileText = new FileText(originalFileText);
        
        // Assert
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
    }
    
    @Test
    public void testFromFileText_withFileTextInstance_ReturnsSameInstance() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        
        // Act
        FileText fileText = FileText.fromLines(file, originalFileText);
        
        // Assert
        assertEquals(originalFileText, fileText);
    }
    
    @Test
    public void testFromFileText_withNonFileTextInstance_CreatesNewInstance() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        
        // Act
        FileText fileText = FileText.fromLines(file, lines);
        
        // Assert
        assertNotNull(fileText);
        assertNotEquals(lines, fileText);
        assertNotNull(fileText.getFile());
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals(lines.size(), fileText.size());
    }
    
    @Test
    public void testGetFile_ReturnsCorrectFile() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        // Act
        File returnedFile = fileText.getFile();
        
        // Assert
        assertEquals(file, returnedFile);
    }
    
    @Test
    public void testGetCharset_ReturnsCorrectCharset() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        // Act
        Charset returnedCharset = fileText.getCharset();
        
        // Assert
        assertEquals(Charset.forName(charsetName), returnedCharset);
    }
    
    @Test
    public void testGetFullText_ReturnsCorrectFullText() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        // Act
        String returnedFullText = fileText.getFullText().toString();
        
        // Assert
        assertEquals(fileText.toLinesArray().length, returnedFullText.split("\\r?\\n").length);
    }
    
    @Test
    public void testToLinesArray_ReturnsCorrectLinesArray() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        // Act
        String[] linesArray = fileText.toLinesArray();
        
        // Assert
        assertEquals(fileText.size(), linesArray.length);
        assertEquals(fileText.get(0), linesArray[0]);
        assertEquals(fileText.get(fileText.size() - 1), linesArray[linesArray.length - 1]);
    }
    
    @Test
    public void testFindLineBreaks_WithoutLineBreaks_ReturnsLineBreaksAtNewLines() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        
        // Act
        int[] lineBreaks = fileText.findLineBreaks();
        
        // Assert
        assertEquals(lines.size() + 1, lineBreaks.length);
        assertEquals(0, lineBreaks[0]);
        assertEquals(lines.get(0).length() + 1, lineBreaks[1]);
        assertEquals(lines.get(0).length() + lines.get(1).length() + 2, lineBreaks[2]);
        assertEquals(fileText.getFullText().length(), lineBreaks[3]);
    }
    
    @Test
    public void testLineColumn_withValidPosition_ReturnsCorrectLineAndColumn() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        int position = 10;
        
        // Act
        FileText.LineColumn lineColumn = fileText.lineColumn(position);
        
        // Assert
        assertEquals(2, lineColumn.getLine());
        assertEquals(position - fileText.findLineBreaks()[1] + 1, lineColumn.getColumn());
    }
    
    @Test
    public void testGet_withValidLineNo_ReturnsCorrectLine() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        int lineNo = 2;
        
        // Act
        String line = fileText.get(lineNo);
        
        // Assert
        assertEquals(lines.get(lineNo), line);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_withInvalidLineNo_ThrowsIndexOutOfBoundsException() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        int lineNo = 3;
        
        // Act
        String line = fileText.get(lineNo);
    }
    
    @Test
    public void testSize_ReturnsCorrectSize() throws IOException {
        // Arrange
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        
        // Act
        int size = fileText.size();
        
        // Assert
        assertEquals(lines.size(), size);
    }
}