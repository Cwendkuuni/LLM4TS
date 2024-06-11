
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class FileTextTest {
    private static final File DUMMY_FILE = new File("dummy.txt");
    private static final String CHARSET_NAME = "UTF-8";
    
    private FileText fileText;
    
    @Before
    public void setUp() {
        List<String> lines = new ArrayList<String>();
        lines.add("line 1");
        lines.add("line 2");
        lines.add("line 3");
        fileText = FileText.fromLines(DUMMY_FILE, lines);
    }
    
    @Test
    public void testConstructor_withValidParameters() throws IOException {
        assertNotNull(fileText.getFile());
        assertEquals(DUMMY_FILE, fileText.getFile());
        assertEquals(CHARSET_NAME, fileText.getCharset().name());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText().toString());
        assertEquals(3, fileText.size());
        assertEquals("line 1", fileText.get(0));
        assertEquals("line 2", fileText.get(1));
        assertEquals("line 3", fileText.get(2));
        assertArrayEquals(new String[] {"line 1", "line 2", "line 3"}, fileText.toLinesArray());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstructor_withUnsupportedCharset() throws IOException {
        new FileText(DUMMY_FILE, "unsupportedCharset");
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testConstructor_withNonExistingFile() throws IOException {
        new FileText(new File("nonExistingFile.txt"), CHARSET_NAME);
    }
    
    @Test
    public void testConstructor_withFileTextArgument() {
        FileText copy = new FileText(fileText);
        assertSame(fileText.getFile(), copy.getFile());
        assertSame(fileText.getCharset(), copy.getCharset());
        assertEquals(fileText.getFullText(), copy.getFullText());
        assertArrayEquals(fileText.toLinesArray(), copy.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        LineColumn lineColumn = fileText.lineColumn(0);
        assertEquals(1, lineColumn.getLine());
        assertEquals(0, lineColumn.getColumn());
        
        lineColumn = fileText.lineColumn(5);
        assertEquals(1, lineColumn.getLine());
        assertEquals(5, lineColumn.getColumn());
        
        lineColumn = fileText.lineColumn(6);
        assertEquals(2, lineColumn.getLine());
        assertEquals(0, lineColumn.getColumn());
        
        lineColumn = fileText.lineColumn(12);
        assertEquals(2, lineColumn.getLine());
        assertEquals(6, lineColumn.getColumn());
        
        lineColumn = fileText.lineColumn(13);
        assertEquals(3, lineColumn.getLine());
        assertEquals(0, lineColumn.getColumn());
    }

}
