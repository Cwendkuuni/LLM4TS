
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithExistingFile() throws IOException {
        // Typical use case
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        Assert.assertEquals(file, fileText.getFile());
        Assert.assertEquals(Charset.forName(charsetName), fileText.getCharset());

        // Edge case: Empty file
        file = new File("empty.txt");

        fileText = new FileText(file, charsetName);

        Assert.assertEquals(file, fileText.getFile());
        Assert.assertEquals(Charset.forName(charsetName), fileText.getCharset());

        // Error scenario: Unsupported charset
        file = new File("test.txt");
        charsetName = "UnknownCharset";

        try {
            fileText = new FileText(file, charsetName);
            Assert.fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Unsupported charset: " + charsetName, e.getMessage());
        }
    }

    @Test
    public void testFileTextConstructorWithFileText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        FileText original = new FileText(file, charsetName);
        FileText fileText = new FileText(original);

        Assert.assertEquals(original.getFile(), fileText.getFile());
        Assert.assertEquals(original.getCharset(), fileText.getCharset());
        Assert.assertEquals(original.getFullText(), fileText.getFullText());

        // Edge case: Null lineBreaks
        original = new FileText(file, charsetName);
        original.lineBreaks = null;
        fileText = new FileText(original);

        Assert.assertEquals(original.getFile(), fileText.getFile());
        Assert.assertEquals(original.getCharset(), fileText.getCharset());
        Assert.assertEquals(original.getFullText(), fileText.getFullText());
    }

    @Test
    public void testFileTextConstructorWithLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>(Arrays.asList("line1", "line2", "line3"));
        String fullText = "line1\nline2\nline3";

        FileText fileText = new FileText(file, lines);

        Assert.assertEquals(file, fileText.getFile());
        Assert.assertNull(fileText.getCharset());
        Assert.assertEquals(fullText, fileText.getFullText());

        Assert.assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testReadFile() throws IOException {
        File inputFile = new File("test.txt");
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

        String expectedText = "line1\nline2\nline3";

        String actualText = FileText.readFile(inputFile, decoder);

        Assert.assertEquals(expectedText, actualText);

        // Error scenario: Non-existing file
        inputFile = new File("nonexisting.txt");

        try {
            actualText = FileText.readFile(inputFile, decoder);
            Assert.fail("Should have thrown FileNotFoundException");
        } catch (FileNotFoundException e) {
            Assert.assertEquals(inputFile.getPath() + " (No such file or directory)", e.getMessage());
        }
    }

    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>(Arrays.asList("line1", "line2", "line3"));

        FileText fileText = FileText.fromLines(file, lines);

        Assert.assertEquals(file, fileText.getFile());
        Assert.assertNull(fileText.getCharset());
        Assert.assertEquals("line1\nline2\nline3", fileText.getFullText());

        Assert.assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());

        // Edge case: Already a FileText instance
        FileText original = new FileText(file, lines);

        fileText = FileText.fromLines(file, original);

        Assert.assertEquals(original, fileText);
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        Assert.assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        Assert.assertEquals(charset, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        String fullText = "line1\nline2\nline3";
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        Assert.assertEquals(fullText, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        String[] lines = {"line1", "line2", "line3"};
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        Assert.assertArrayEquals(lines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        int[] lineBreaks = fileText.findLineBreaks();

        int[] expectedLineBreaks = {0, 6, 12};

        Assert.assertArrayEquals(expectedLineBreaks, lineBreaks);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        LineColumn lineColumn = fileText.lineColumn(8);

        Assert.assertEquals(2, lineColumn.getLineNo());
        Assert.assertEquals(2, lineColumn.getColumnNo());
    }

    @Test
    public void testGet() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        String line = fileText.get(1);

        Assert.assertEquals("line2", line);
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        Assert.assertEquals(3, fileText.size());
    }
}

