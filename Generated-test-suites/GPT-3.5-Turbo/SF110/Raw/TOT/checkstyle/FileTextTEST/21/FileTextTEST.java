package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testConstructor_withFileAndCharsetName_Valid() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertNotNull(fileText.findLineBreaks());
        assertNotNull(fileText.lineColumn(0));
        assertEquals(0, fileText.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_withFileAndUnsupportedCharsetName_ThrowsIllegalStateException() throws IOException {
        File file = new File("test.txt");
        String charsetName = "XYZ";

        new FileText(file, charsetName);
    }

    @Test
    public void testCopyConstructor_Valid() {
        FileText original = new FileText(new File("test.txt"), "UTF-8");

        FileText copy = new FileText(original);

        assertEquals(original.getFile(), copy.getFile());
        assertEquals(original.getCharset(), copy.getCharset());
        assertEquals(original.getFullText(), copy.getFullText());
        assertArrayEquals(original.toLinesArray(), copy.toLinesArray());
        assertArrayEquals(original.findLineBreaks(), copy.findLineBreaks());
        assertEquals(original.lineColumn(0), copy.lineColumn(0));
        assertEquals(original.size(), copy.size());
    }

    @Test
    public void testStaticMethod_fromLines_withFileTextInstance_ReturnsSameInstance() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = new FileText(file, lines);

        FileText result = FileText.fromLines(file, fileText);

        assertSame(fileText, result);
    }

    @Test
    public void testStaticMethod_fromLines_withOtherList_ReturnsNewInstance() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");

        FileText result = FileText.fromLines(file, lines);

        assertNotSame(lines, result);
        assertEquals(lines, Arrays.asList(result.toLinesArray()));
    }

    @Test
    public void testGetFile_ReturnsCorrectFile() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        File result = fileText.getFile();

        assertEquals(file, result);
    }

    @Test
    public void testGetCharset_ReturnsCorrectCharset() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        Charset result = fileText.getCharset();

        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testGetFullText_ReturnsCorrectFullText() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        CharSequence result = fileText.getFullText();

        assertEquals(fileText.toLinesArray().toString(), result.toString());
    }

    @Test
    public void testToLinesArray_ReturnsCopyOfLinesArray() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        String[] result = fileText.toLinesArray();

        assertNotSame(fileText.toLinesArray(), result);
        assertArrayEquals(fileText.toLinesArray(), result);
    }

    @Test
    public void testFindLineBreaks_ReturnsLineBreakPositions() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        int[] result = fileText.findLineBreaks();

        assertEquals(fileText.size() + 1, result.length);
        assertEquals(0, result[0]);
    }

    @Test
    public void testLineColumn_ValidPosition_ReturnsCorrectLineColumnObject() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        LineColumn result = fileText.lineColumn(0);

        assertEquals(1, result.getLine());
        assertEquals(0, result.getColumn());
    }

    @Test
    public void testGet_ValidLineNumber_ReturnsCorrectLine() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = new FileText(file, lines);

        String result = fileText.get(1);

        assertEquals("line 2", result);
    }

    @Test
    public void testSize_EmptyFile_ReturnsZero() {
        File file = new File("empty.txt");
        FileText fileText = new FileText(file, "UTF-8");

        int result = fileText.size();

        assertEquals(0, result);
    }

    @Test
    public void testSize_NonEmptyFile_ReturnsCorrectSize() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = new FileText(file, lines);

        int result = fileText.size();

        assertEquals(2, result);
    }

    @Test
    public void testLINE_TERMINATOR_PatternMatchesNewLine() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        assertEquals(1, fileText.LINE_TERMINATOR.matcher("\n").find());
    }

    @Test
    public void testLINE_TERMINATOR_PatternMatchesCarriageReturnNewLine() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        assertEquals(1, fileText.LINE_TERMINATOR.matcher("\r\n").find());
    }

    @Test
    public void testLINE_TERMINATOR_PatternMatchesCarriageReturn() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        assertEquals(1, fileText.LINE_TERMINATOR.matcher("\r").find());
    }

    @Test
    public void testLINE_TERMINATOR_PatternDoesNotMatchOtherString() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        assertEquals(0, fileText.LINE_TERMINATOR.matcher("Other string").find());
    }

}