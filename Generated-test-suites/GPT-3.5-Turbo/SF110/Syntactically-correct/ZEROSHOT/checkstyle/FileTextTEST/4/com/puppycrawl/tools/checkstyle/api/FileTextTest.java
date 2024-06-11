
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

    private FileText fileText;
    
    @Before
    public void setUp() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        fileText = FileText.fromLines(file, lines);
    }

    @Test
    public void testGetFile() {
        File expectedFile = new File("testFile.txt");
        assertEquals(expectedFile, fileText.getFile());
    }
    
    @Test
    public void testGetCharset() {
        assertNull(fileText.getCharset());
    }
    
    @Test
    public void testGetFullText() {
        String expectedFullText = "Line 1\nLine 2\nLine 3\n";
        assertEquals(expectedFullText, fileText.getFullText().toString());
    }
    
    @Test
    public void testToLinesArray() {
        String[] expectedLines = {"Line 1", "Line 2", "Line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        int pos = 10;
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(1, lineColumn.getLine());
        assertEquals(10, lineColumn.getColumn());
    }
    
    @Test
    public void testGet() {
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }

}
