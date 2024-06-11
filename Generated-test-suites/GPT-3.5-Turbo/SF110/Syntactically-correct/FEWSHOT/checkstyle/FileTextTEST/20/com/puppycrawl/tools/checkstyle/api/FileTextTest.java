
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {
    private final static File TEST_FILE = new File("testFile.txt"); // replace with actual test file path
    private final static String TEST_CHARSET = "UTF-8"; // replace with actual test charset
    
    private FileText fileText;
    
    @Before
    public void setUp() throws IOException {
        List<String> testLines = new ArrayList<>();
        testLines.add("Line 1");
        testLines.add("Line 2");
        fileText = FileText.fromLines(TEST_FILE, testLines);
    }
    
    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        FileText ft = new FileText(TEST_FILE, TEST_CHARSET);
        assertNotNull(ft);
        assertNotNull(ft.getFile());
        assertNotNull(ft.getCharset());
        assertEquals(TEST_FILE, ft.getFile());
        assertEquals(Charset.forName(TEST_CHARSET), ft.getCharset());
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText ft = new FileText(fileText);
        assertNotNull(ft);
        assertNotNull(ft.getFile());
        assertNotNull(ft.getCharset());
        assertNotNull(ft.getFullText());
        assertTrue(Arrays.equals(fileText.toLinesArray(), ft.toLinesArray()));
    }
    
    @Test
    public void testGetFile() {
        assertEquals(TEST_FILE, fileText.getFile());
    }
    
    @Test
    public void testGetCharset() {
        assertEquals(Charset.forName(TEST_CHARSET), fileText.getCharset());
    }
    
    @Test
    public void testGetFullText() {
        assertEquals("Line 1\nLine 2\n", fileText.getFullText());
    }
    
    @Test
    public void testToLinesArray() {
        String[] expectedLines = {"Line 1", "Line 2"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        assertEquals(new FileText.LineColumn(1, 5), fileText.lineColumn(5));
        assertEquals(new FileText.LineColumn(2, 0), fileText.lineColumn(7));
    }
    
    @Test
    public void testGet() {
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
    }
    
    @Test
    public void testSize() {
        assertEquals(2, fileText.size());
    }
}
