package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.google.common.base.Charsets;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

public class SourceFileTest {

    @Test
    public void testGetLineOffset() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        SourceFile sourceFileWithCode = new SourceFile("test.js") {
            @Override
            public String getCode() throws IOException {
                return "function test() {}";
            }
        };

        // Test getting line offsets for a source file with no code.
        assertNull(sourceFile.getCode());
        assertNull(sourceFile.getCodeReader());
        assertEquals(0, sourceFile.getLineOffset(1));
        assertEquals(1, sourceFile.getNumLines());

        // Test getting line offsets for a source file with code.
        assertSame(sourceFileWithCode, sourceFileWithCode.getCodeReader());
        assertEquals("function test() {}\n", sourceFileWithCode.getCode());
        assertEquals(0, sourceFileWithCode.getLineOffset(1));
        assertEquals(2, sourceFileWithCode.getLineOffset(2));
        assertEquals(3, sourceFileWithCode.getLineOffset(3));
        assertEquals(4, sourceFileWithCode.getLineOffset(4));
        assertEquals(5, sourceFileWithCode.getNumLines());
    }

    @Test
    public void testGetCode() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js") {
            @Override
            public String getCode() throws IOException {
                return "function test() {}";
            }
        };

        assertEquals("function test() {}\n", sourceFile.getCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFileName() {
        new SourceFile(null);
    }

    @Test
    public void testGetRegion() throws IOException {
        String testCode = "function test() {\n" +
                "    var a = 1;\n" +
                "    var b = 2;\n" +
                "    var c = a + b;\n" +
                "    return c;\n" +
                "}";

        SourceFile sourceFile = new SourceFile("test.js") {
            @Override
            public String getCode() throws IOException {
                return testCode;
            }
        };

        // Test getting a region for a line number within the code.
        Region region1 = sourceFile.getRegion(5);
        assertEquals(3, region1.getLength());
        assertEquals("    var c = a + b;\n", region1.getContents());
        assertEquals(3, region1.getStartLineNumber());
        assertEquals(5, region1.getEndLineNumber());

        // Test getting a region for a line number outside the code.
        Region region2 = sourceFile.getRegion(10);
        assertNull(region2);

        // Test getting a region for a line number at the start of the code.
        Region region3 = sourceFile.getRegion(1);
        assertEquals(1, region3.getLength());
        assertEquals("function test() {\n", region3.getContents());
        assertEquals(1, region3.getStartLineNumber());
        assertEquals(1, region3.getEndLineNumber());
    }

    @Test
    public void testToString() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js") {
            @Override
            public String getCode() throws IOException {
                return "function test() {}";
            }
        };

        assertEquals("test.js", sourceFile.toString());
    }

    @Test
    public void testFromFile() throws IOException {
        String testFilePath = "test.js";
        String testCode = "function test() {}";

        SourceFile sourceFileFromFile = SourceFile.fromFile(testFilePath);

        // Test getting line offsets for a source file loaded from disk.
        assertNull(sourceFileFromFile.getCode());
        assertSame(new FileReader(new File(testFilePath)), sourceFileFromFile.getCodeReader());
        assertEquals(0, sourceFileFromFile.getLineOffset(1));
        assertEquals(1, sourceFileFromFile.getNumLines());

        // Test getting the code for a source file loaded from disk.
        assertEquals(testCode, sourceFileFromFile.getCode());

        // Test getting a region for a line number within the code.
        Region region = sourceFileFromFile.getRegion(2);
        assertEquals(0, region.getLength());
        assertEquals("", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());

        // Test getting a region for a line number outside the code.
        region = sourceFileFromFile.getRegion(3);
        assertNull(region);

        // Test getting a region for a line number at the start of the code.
        region = sourceFileFromFile.getRegion(1);
        assertEquals(1, region.getLength());
        assertEquals("function test() {}\n", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());

        // Test getting the code again to make sure it is read from disk only once.
        assertEquals(testCode, sourceFileFromFile.getCode());
    }

    @Test
    public void testFromCode() throws IOException {
        String testFilePath = "test.js";
        String testCode = "function test() {}";

        SourceFile sourceFileFromCode = SourceFile.fromCode(testFilePath, testCode);

        // Test getting line offsets for a source file with preloaded code.
        assertNull(sourceFileFromCode.getCode());
        assertSame(new StringReader(testCode), sourceFileFromCode.getCodeReader());
        assertEquals(0, sourceFileFromCode.getLineOffset(1));
        assertEquals(1, sourceFileFromCode.getNumLines());

        // Test getting the code for a source file with preloaded code.
        assertEquals(testCode, sourceFileFromCode.getCode());

        // Test getting a region for a line number within the code.
        Region region = sourceFileFromCode.getRegion(2);
        assertEquals(0, region.getLength());
        assertEquals("", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());

        // Test getting a region for a line number outside the code.
        region = sourceFileFromCode.getRegion(3);
        assertNull(region);

        // Test getting a region for a line number at the start of the code.
        region = sourceFileFromCode.getRegion(1);
        assertEquals(1, region.getLength());
        assertEquals("function test() {}\n", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());
    }

    @Test
    public void testFromInputStream() throws IOException {
        String testFilePath = "test.js";
        String testCode = "function test() {}";

        SourceFile sourceFileFromInputStream = SourceFile.fromInputStream(testFilePath, new ByteArrayInputStream(testCode.getBytes(Charsets.UTF_8)));

        // Test getting line offsets for a source file with preloaded code.
        assertNull(sourceFileFromInputStream.getCode());
        assertSame(new InputStreamReader(new ByteArrayInputStream(testCode.getBytes(Charsets.UTF_8)), Charsets.UTF_8), sourceFileFromInputStream.getCodeReader());
        assertEquals(0, sourceFileFromInputStream.getLineOffset(1));
        assertEquals(1, sourceFileFromInputStream.getNumLines());

        // Test getting the code for a source file with preloaded code.
        assertEquals(testCode, sourceFileFromInputStream.getCode());

        // Test getting a region for a line number within the code.
        Region region = sourceFileFromInputStream.getRegion(2);
        assertEquals(0, region.getLength());
        assertEquals("", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());

        // Test getting a region for a line number outside the code.
        region = sourceFileFromInputStream.getRegion(3);
        assertNull(region);

        // Test getting a region for a line number at the start of the code.
        region = sourceFileFromInputStream.getRegion(1);
        assertEquals(1, region.getLength());
        assertEquals("function test() {}\n", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());
    }

    @Test
    public void testFromReader() throws IOException {
        String testCode = "function test() {}";

        SourceFile sourceFileFromReader = SourceFile.fromReader(new File("test.js").getPath(), new StringReader(testCode));

        // Test getting line offsets for a source file with preloaded code.
        assertNull(sourceFileFromReader.getCode());
        assertSame(new StringReader(testCode), sourceFileFromReader.getCodeReader());
        assertEquals(0, sourceFileFromReader.getLineOffset(1));
        assertEquals(1, sourceFileFromReader.getNumLines());

        // Test getting the code for a source file with preloaded code.
        assertEquals(testCode, sourceFileFromReader.getCode());

        // Test getting a region for a line number within the code.
        Region region = sourceFileFromReader.getRegion(2);
        assertEquals(0, region.getLength());
        assertEquals("", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());

        // Test getting a region for a line number outside the code.
        region = sourceFileFromReader.getRegion(3);
        assertNull(region);

        // Test getting a region for a line number at the start of the code.
        region = sourceFileFromReader.getRegion(1);
        assertEquals(1, region.getLength());
        assertEquals("function test() {}\n", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());
    }

    @Test
    public void testFromGenerator() throws IOException {
        String testFilePath = "test.js";

        SourceFile sourceFileFromGenerator = SourceFile.fromGenerator(testFilePath, new Generator() {
            @Override
            public String getCode() {
                return "function test() {}";
            }
        });

        // Test getting line offsets for a source file with generated code.
        assertNull(sourceFileFromGenerator.getCode());
        assertSame(new StringReader("function test() {}"), sourceFileFromGenerator.getCodeReader());
        assertEquals(0, sourceFileFromGenerator.getLineOffset(1));
        assertEquals(1, sourceFileFromGenerator.getNumLines());

        // Test getting the code for a source file with generated code.
        assertEquals("function test() {}", sourceFileFromGenerator.getCode());

        // Test getting a region for a line number within the code.
        Region region = sourceFileFromGenerator.getRegion(2);
        assertEquals(0, region.getLength());
        assertEquals("", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());

        // Test getting a region for a line number outside the code.
        region = sourceFileFromGenerator.getRegion(3);
        assertNull(region);

        // Test getting a region for a line number at the start of the code.
        region = sourceFileFromGenerator.getRegion(1);
        assertEquals(1, region.getLength());
        assertEquals("function test() {}\n", region.getContents());
        assertEquals(1, region.getStartLineNumber());
        assertEquals(1, region.getEndLineNumber());
    }
}