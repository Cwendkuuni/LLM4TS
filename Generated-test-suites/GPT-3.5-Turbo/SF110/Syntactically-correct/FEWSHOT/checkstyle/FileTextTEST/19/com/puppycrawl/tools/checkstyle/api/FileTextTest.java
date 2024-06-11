
package com.puppycrawl.tools.checkstyle.api;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileTextTest {

    @Test
    void testConstructorFromFile() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(file, fileText.getFile());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        assertEquals("line1\nline2\nline3", fileText.getFullText());
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, fileText.toLinesArray());
        assertEquals(3, fileText.size());
        assertEquals("line1", fileText.get(0));
        assertEquals("line2", fileText.get(1));
        assertEquals("line3", fileText.get(2));
    }
    
    @Test
    void testConstructorFromFile_InvalidCharset() {
        File file = new File("testFile.txt");
        assertThrows(IllegalStateException.class, () -> new FileText(file, "INVALID_CHARSET"));
    }

    @Test
    void testConstructorFromFile_NonexistentFile() {
        File file = new File("nonExistentFile.txt");
        assertThrows(FileNotFoundException.class, () -> new FileText(file, "UTF-8"));
    }
    
    @Test
    void testConstructorFromLines() {
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(new File("testFile.txt"), lines);
        
        assertEquals(new File("testFile.txt"), fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line1\nline2\nline3", fileText.getFullText());
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, fileText.toLinesArray());
        assertEquals(3, fileText.size());
        assertEquals("line1", fileText.get(0));
        assertEquals("line2", fileText.get(1));
        assertEquals("line3", fileText.get(2));
    }
    
    @Test
    void testLineColumn() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 2), fileText.lineColumn(1));
        assertEquals(new LineColumn(2, 1), fileText.lineColumn(6));
        assertEquals(new LineColumn(3, 6), fileText.lineColumn(14));
    }
    
    @Test
    void testLineColumn_PosBeforeFirstLine() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(new LineColumn(0, 0), fileText.lineColumn(-1));
        assertEquals(new LineColumn(0, 0), fileText.lineColumn(0));
    }
    
    @Test
    void testLineColumn_PosAfterLastLine() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(new LineColumn(4, 0), fileText.lineColumn(16));
        assertEquals(new LineColumn(4, 0), fileText.lineColumn(Integer.MAX_VALUE));
    }
    
    @Test
    void testGet_IllegalLineNo() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        FileText fileText = new FileText(file, "UTF-8");
        
        assertThrows(IndexOutOfBoundsException.class, () -> fileText.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> fileText.get(3));
    }
    
    @Test
    void testSize() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(3, fileText.size());
    }

}
