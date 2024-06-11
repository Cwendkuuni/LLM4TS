package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);
    }

    @Test
    public void testGetFile() {
        File file = fileText.getFile();
        assertNotNull(file);
    }

    @Test
    public void testGetCharset() {
        Charset charset = fileText.getCharset();
        assertNotNull(charset);
    }

    @Test
    public void testGetFullText() {
        CharSequence fullText = fileText.getFullText();
        assertNotNull(fullText);
    }

    @Test
    public void testToLinesArray() {
        String[] linesArray = fileText.toLinesArray();
        assertNotNull(linesArray);
    }

    @Test
    public void testLineColumn() {
        int pos = 10;
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertNotNull(lineColumn);
    }

    @Test
    public void testGet() {
        int lineNo = 0;
        String line = fileText.get(lineNo);
        assertNotNull(line);
    }

    @Test
    public void testSize() {
        int size = fileText.size();
        assertTrue(size >= 0);
    }
    
    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
    }
}