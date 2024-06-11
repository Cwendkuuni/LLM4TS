package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testConstructor() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());
    }

    @Test
    public void testConstructor_ThrowsExceptionForUnsupportedCharset() {
        File file = new File("test.txt");
        String charsetName = "InvalidCharset";
        
        assertThrows(IllegalStateException.class, () -> {
           new FileText(file, charsetName);
        });
    }

    @Test
    public void testConstructor_ThrowsExceptionForNonExistingFile() {
        File file = new File("non_existing.txt");
        String charsetName = "UTF-8";
        
        assertThrows(FileNotFoundException.class, () -> {
           new FileText(file, charsetName);
        });
    }

    @Test
    public void testConstructor_CopyConstructor() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText original = new FileText(file, charsetName);
        FileText copy = new FileText(original);
        
        assertEquals(original.getFile(), copy.getFile());
        assertEquals(original.getCharset(), copy.getCharset());
        assertEquals(original.getFullText(), copy.getFullText());
        assertArrayEquals(original.toLinesArray(), copy.toLinesArray());
    }

    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\n", fileText.getFullText().toString());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testGet() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals("line 1", fileText.get(0));
        assertEquals("line 2", fileText.get(1));
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals(2, fileText.size());
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = FileText.fromLines(file, lines);
        
        LineColumn lineColumn1 = fileText.lineColumn(5);
        assertEquals(1, lineColumn1.getLine());
        assertEquals(4, lineColumn1.getColumn());
        
        LineColumn lineColumn2 = fileText.lineColumn(10);
        assertEquals(2, lineColumn2.getLine());
        assertEquals(3, lineColumn2.getColumn());
    }
}