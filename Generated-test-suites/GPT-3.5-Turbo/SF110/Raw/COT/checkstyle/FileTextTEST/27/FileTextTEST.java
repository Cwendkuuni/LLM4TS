
package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import java.io.*;

public class FileTextTest {

    private final String charsetName = "UTF-8";
    private final File file = new File("test.txt");
    private final String[] lines = {
        "This is line 1",
        "This is line 2",
        "This is line 3"
    };
    private final String fullText = "This is line 1\nThis is line 2\nThis is line 3";
    private final FileText fileText = new FileText(file, charsetName);

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        Assert.assertEquals(file, fileText.getFile());
        Assert.assertEquals(charsetName, fileText.getCharset().name());
        Assert.assertEquals(fullText, fileText.getFullText().toString());
        Assert.assertArrayEquals(lines, fileText.toLinesArray());
        Assert.assertEquals(lines[0], fileText.get(0));
        Assert.assertEquals(lines[1], fileText.get(1));
        Assert.assertEquals(lines[2], fileText.get(2));
        Assert.assertEquals(lines.length, fileText.size());
    }

    @Test
    public void testConstructorWithFileText() {
        FileText copy = new FileText(fileText);
        
        Assert.assertEquals(file, copy.getFile());
        Assert.assertEquals(charsetName, copy.getCharset().name());
        Assert.assertEquals(fullText, copy.getFullText().toString());
        Assert.assertArrayEquals(lines, copy.toLinesArray());
        Assert.assertEquals(lines[0], copy.get(0));
        Assert.assertEquals(lines[1], copy.get(1));
        Assert.assertEquals(lines[2], copy.get(2));
        Assert.assertEquals(lines.length, copy.size());
    }

    @Test
    public void testFileTextFromLines() {
        FileText fileTextFromLines = FileText.fromLines(file, Arrays.asList(lines));
        
        Assert.assertEquals(file, fileTextFromLines.getFile());
        Assert.assertNull(fileTextFromLines.getCharset());
        Assert.assertEquals(fullText, fileTextFromLines.getFullText().toString());
        Assert.assertArrayEquals(lines, fileTextFromLines.toLinesArray());
        Assert.assertEquals(lines[0], fileTextFromLines.get(0));
        Assert.assertEquals(lines[1], fileTextFromLines.get(1));
        Assert.assertEquals(lines[2], fileTextFromLines.get(2));
        Assert.assertEquals(lines.length, fileTextFromLines.size());
    }

    @Test
    public void testLineColumn() {
        LineColumn lc = fileText.lineColumn(10);
        
        Assert.assertEquals(1, lc.getLine());
        Assert.assertEquals(10, lc.getColumn());
    }
}
