package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.SourceFile;
import org.apache.commons.cli.SourceFile.Generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

public class SourceFileTest {

    @Test
    public void testGetCode() throws IOException {
        String code = "function foo() {\n";
        SourceFile file = SourceFile.fromCode("Test.js", code);
        assertEquals(code, file.getCode());

        // Test fromFile
        File testFile = new File("Test.js");
        Files.write(testFile, code.getBytes(Charsets.UTF_8));
        file = SourceFile.fromFile("Test.js");
        assertEquals(code, file.getCode());
        testFile.delete();

        // Test fromInputStream
        InputStream inputStream = new ByteArrayInputStream(code.getBytes(Charsets.UTF_8));
        file = SourceFile.fromInputStream("Test.js", inputStream);
        assertEquals(code, file.getCode());

        // Test fromReader
        Reader reader = new InputStreamReader(new ByteArrayInputStream(code.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
        file = SourceFile.fromReader("Test.js", reader);
        assertEquals(code, file.getCode());

        // Test fromGenerator
        Generator generator = () -> code;
        file = SourceFile.fromGenerator("Test.js", generator);
        assertEquals(code, file.getCode());
    }

    @Test
    public void testGetRegion() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        SourceFile file = SourceFile.fromCode("Test.js", code);

        Region region = file.getRegion(5);
        assertEquals("  var x = 5;\n", region.getCode());
        assertEquals(3, region.getLength());
        assertEquals(4, region.getStartLineNumber());
        assertEquals(4, region.getEndLineNumber());

        region = file.getRegion(11);
        assertNull(region);
    }

    @Test
    public void testLineOffset() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        SourceFile file = SourceFile.fromCode("Test.js", code);

        assertEquals(0, file.getLineOffset(1));
        assertEquals(16, file.getLineOffset(2));
        assertEquals(32, file.getLineOffset(3));
        assertEquals(48, file.getLineOffset(4));

        try {
            file.getLineOffset(5);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            file.getLineOffset(0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetLine() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        SourceFile file = SourceFile.fromCode("Test.js", code);

        String line1 = file.getLine(1);
        assertEquals("function foo() {", line1);

        String line2 = file.getLine(2);
        assertEquals("  var x = 5;", line2);

        String line3 = file.getLine(3);
        assertEquals("  var y = 10;", line3);

        String line4 = file.getLine(4);
        assertEquals("  return x + y;", line4);

        String line5 = file.getLine(5);
        assertNull(line5);

        try {
            file.getLine(0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNumLines() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}" +
                "function bar() {\n" +
                "  var a = 3;\n" +
                "  var b = 7;\n" +
                "  return a + b;\n" +
                "}";

        SourceFile file = SourceFile.fromCode("Test.js", code);

        assertEquals(6, file.getNumLines());
    }

    @Test
    public void testClearCachedSource() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        SourceFile file = SourceFile.fromCode("Test.js", code);

        String originalCode = file.getCode();

        file.clearCachedSource();

        String newCode = file.getCode();

        assertNotEquals(originalCode, newCode);
    }

    @Test
    public void testSetOriginalPath() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        SourceFile file = SourceFile.fromCode("Test.js", code);

        file.setOriginalPath("TestOriginal.js");

        assertEquals("TestOriginal.js", file.getOriginalPath());
    }

    @Test
    public void testIsExtern() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        SourceFile file = SourceFile.fromCode("Test.js", code);

        assertFalse(file.isExtern());

        file.setIsExtern(true);

        assertTrue(file.isExtern());
    }

    @Test
    public void testToString() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        SourceFile file = SourceFile.fromCode("Test.js", code);

        assertEquals("Test.js", file.toString());
    }

    @Test
    public void testFromFileWithCharset() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        File testFile = new File("Test.js");
        Files.write(testFile, code.getBytes(Charsets.UTF_8));

        SourceFile file = SourceFile.fromFile("Test.js", Charsets.UTF_8);

        assertEquals(code, file.getCode());

        testFile.delete();
    }

    @Test
    public void testFromFileWithCharsetNull() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        File testFile = new File("Test.js");
        Files.write(testFile, code.getBytes(Charsets.UTF_8));

        SourceFile file = SourceFile.fromFile("Test.js");

        assertEquals(code, file.getCode());

        testFile.delete();
    }

    @Test
    public void testFindLineOffsets() throws IOException {
        String code = "function foo() {\n" +
                "  var x = 5;\n" +
                "  var y = 10;\n" +
                "  return x + y;\n" +
                "}";

        SourceFile file = SourceFile.fromCode("Test.js", code);

        int[] lineOffsets = file.getLineOffsets();

        assertEquals(6, lineOffsets.length);
        assertEquals(0, lineOffsets[0]);
        assertEquals(16, lineOffsets[1]);
        assertEquals(32, lineOffsets[2]);
        assertEquals(48, lineOffsets[3]);
        assertEquals(64, lineOffsets[4]);
        assertEquals(80, lineOffsets[5]);
    }
}

class SimpleRegion implements Region {
    private final int startLineNumber;
    private final int endLineNumber;
    private final String code;

    SimpleRegion(int startLineNumber, int endLineNumber, String code) {
        this.startLineNumber = startLineNumber;
        this.endLineNumber = endLineNumber;
        this.code = code;
    }

    @Override
    public int getStartLineNumber() {
        return startLineNumber;
    }

    @Override
    public int getEndLineNumber() {
        return endLineNumber;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getLength() {
        return code.length();
    }
}