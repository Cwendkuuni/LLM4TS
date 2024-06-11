package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.SourceFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class SourceFileTest {

    @Test
    public void testFromFile() throws IOException {
        String fileName = "test.js";
        File file = new File(fileName);
        Charset charset = Charsets.UTF_8;

        SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
        String code = sourceFile.getCode();
        assertEquals(Files.toString(file, charset), code);
    }

    @Test
    public void testFromCode() {
        String fileName = "test.js";
        String code = "function test() {\n" +
                "  print(\"Hello, World!\");\n" +
                "}";

        SourceFile sourceFile = SourceFile.fromCode(fileName, code);
        assertEquals(fileName, sourceFile.getName());
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testGetLine() {
        String code = "function test() {\n" +
                "  print(\"Line 1: Hello, World!\");\n" +
                "  print(\"Line 2: This is a test.\");\n" +
                "}";

        SourceFile sourceFile = new Preloaded("test.js", code);
        String line1 = sourceFile.getLine(1);
        String line2 = sourceFile.getLine(2);

        assertEquals("Line 1: Hello, World!", line1);
        assertEquals("This is a test.", line2);
    }

    @Test
    public void testGetRegion() {
        String code = "function test() {\n" +
                "  print(\"Line 1: Hello, World!\");\n" +
                "  print(\"Line 2: This is a test.\");\n" +
                "}";

        SourceFile sourceFile = new Preloaded("test.js", code);
        Region region = sourceFile.getRegion(2);

        assertEquals(3, region.getEndLine());
        assertEquals("  print(\"This is a test.\");\n", region.getCode());
    }

    @Test
    public void testFindLineOffsets() {
        String code = "function test() {\n" +
                "  print(\"Line 1: Hello, World!\");\n" +
                "  print(\"Line 2: This is a test.\");\n" +
                "}";

        SourceFile sourceFile = new Preloaded("test.js", code);

        int numLines = sourceFile.getNumLines();
        int line1Offset = sourceFile.getLineOffset(1);

        assertEquals(3, numLines);
        assertEquals(30, line1Offset);
    }

    @Test
    public void testClearCachedSource() {
        String code = "function test() {\n" +
                "  print(\"Hello, World!\");\n" +
                "}";

        SourceFile sourceFile = new Preloaded("test.js", code);

        String originalCode = sourceFile.getCode();
        sourceFile.clearCachedSource();

        String newCode = sourceFile.getCode();
        assertNotSame(originalCode, newCode);
    }
}

class Region {
    private final int startLine;
    private final int endLine;
    private final String code;

    Region(int startLine, int endLine, String code) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.code = code;
    }

    int getStartLine() {
        return startLine;
    }

    int getEndLine() {
        return endLine;
    }

    String getCode() {
        return code;
    }
}