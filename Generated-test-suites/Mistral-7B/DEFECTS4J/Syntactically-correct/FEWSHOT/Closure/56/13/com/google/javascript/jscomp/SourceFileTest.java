package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test.js");
        String expectedCode = "function test() {\n" +
                "  var x = 1;\n" +
                "  return x;\n" +
                "}";
        try {
            sourceFile.setCode(expectedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line1 = sourceFile.getLine(1);
        assertEquals("function test() {\n", line1);

        String line3 = sourceFile.getLine(3);
        assertEquals("  var x = 1;\n", line3);

        String line5 = sourceFile.getLine(5);
        assertNull(line5);

        String line10 = sourceFile.getLine(10);
        assertNull(line10);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test.js");
        String expectedCode = "function test() {\n" +
                "  var x = 1;\n" +
                "  return x;\n" +
                "}";
        try {
            sourceFile.setCode(expectedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Region region = sourceFile.getRegion(5);
        assertEquals(3, region.getStartLineNumber());
        assertEquals(5, region.getEndLineNumber());
        assertEquals("  var x = 1;\n", region.getSource());
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        String expectedCode = "function test() {\n" +
                "  var x = 1;\n" +
                "  return x;\n" +
                "}";
        try {
            sourceFile.setCode(expectedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int lineOffset = sourceFile.getLineOffset(3);
        assertEquals(27, lineOffset);

        lineOffset = sourceFile.getLineOffset(6);
        assertNotEquals(27, lineOffset);
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");
        String expectedCode = "function test() {\n" +
                "  var x = 1;\n" +
                "  return x;\n" +
                "}";
        try {
            sourceFile.setCode(expectedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numLines = sourceFile.getNumLines();
        assertEquals(3, numLines);
    }

    @Test
    public void testGetOriginalPath() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setOriginalPath("path/to/test.js");

        String originalPath = sourceFile.getOriginalPath();
        assertEquals("path/to/test.js", originalPath);

        originalPath = sourceFile.getFileName();
        assertEquals("test.js", originalPath);
    }

    @Test
    public void testFromFile() {
        String fileName = "test.js";
        SourceFile sourceFile = null;

        try {
            sourceFile = SourceFile.fromFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(sourceFile);
        assertEquals(fileName, sourceFile.getFileName());
    }

    @Test
    public void testFromCode() {
        String fileName = "test.js";
        String code = "function test() {\n" +
                "  var x = 1;\n" +
                "  return x;\n" +
                "}";
        SourceFile sourceFile = SourceFile.fromCode(fileName, code);

        assertNotNull(sourceFile);
        assertEquals(fileName, sourceFile.getFileName());
        assertEquals(code, sourceFile.getCode());
    }
}