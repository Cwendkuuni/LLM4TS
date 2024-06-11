package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals(1, sourceFile.getLineOffset(1));
        assertEquals(11, sourceFile.getLineOffset(11));
        assertThrows(IllegalArgumentException.class, () -> sourceFile.getLineOffset(0));
        assertThrows(IllegalArgumentException.class, () -> sourceFile.getLineOffset(12));
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "line1\nline2\nline3";
        try {
            sourceFile.setCode(code);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals(3, sourceFile.getNumLines());
    }

    @Test
    public void testGetCode() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        String expectedCode = "function test() {\n  // test\n}";
        sourceFile.setCode(expectedCode);
        String actualCode = sourceFile.getCode();
        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCodeReader() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        String expectedCode = "function test() {\n  // test\n}";
        sourceFile.setCode(expectedCode);
        try (Reader reader = sourceFile.getCodeReader()) {
            StringBuilder stringBuilder = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            String actualCode = stringBuilder.toString();
            assertEquals(expectedCode, actualCode);
        }
    }

    @Test
    public void testFindLineOffsets() {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "line1\nline2\nline3";
        try {
            sourceFile.setCode(code);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        int[] lineOffsets = sourceFile.lineOffsets;
        assertEquals(3, lineOffsets.length);
        assertEquals(1, lineOffsets[0]);
        assertEquals(7, lineOffsets[1]);
        assertEquals(13, lineOffsets[2]);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "line1\nline2\nline3";
        try {
            sourceFile.setCode(code);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        Region region = sourceFile.getRegion(2);
        assertEquals(1, region.getStartLineNumber());
        assertEquals(2, region.getEndLineNumber());
        assertEquals("line2", region.getSource());
    }

    @Test
    public void testGetName() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertFalse(sourceFile.isExtern());
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "line1\nline2\nline3";
        try {
            sourceFile.setCode(code);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("line1", sourceFile.getLine(1));
        assertEquals("line2", sourceFile.getLine(2));
        assertEquals("line3", sourceFile.getLine(3));
        assertNull(sourceFile.getLine(4));
    }

    @Test
    public void testGetRegionWithCache() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "line1\nline2\nline3";
        sourceFile.setCode(code);
        Region region = sourceFile.getRegion(2);
        assertEquals(1, region.getStartLineNumber());
        assertEquals(2, region.getEndLineNumber());
        assertEquals("line2", region.getSource());

        // Verify that the cache is used when calling getRegion again.
        Region region2 = sourceFile.getRegion(2);
        assertEquals(region, region2);
    }

    @Test
    public void testGetRegionWithoutCache() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "line1\nline2\nline3";
        sourceFile.setCode(code);
        Region region = sourceFile.getRegion(2);
        assertEquals(1, region.getStartLineNumber());
        assertEquals(2, region.getEndLineNumber());
        assertEquals("line2", region.getSource());

        // Verify that a new region is returned when calling getRegion again.
        Region region2 = sourceFile.getRegion(2);
        assertNotEquals(region, region2);
    }
}