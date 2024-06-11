package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {
    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setCode("line 1\nline 2\nline 3");

        assertEquals("line 1", sourceFile.getLine(1));
        assertEquals("line 2", sourceFile.getLine(2));
        assertEquals("line 3", sourceFile.getLine(3));
        assertNull(sourceFile.getLine(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineWithInvalidLineNumber() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setCode("line 1\nline 2\nline 3");

        sourceFile.getLine(0);
    }

    @Test
    public void testGetCode() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "function test() {\n  // Test code\n}";
        sourceFile.setCode(code);

        assertEquals(code, sourceFile.getCode());
    }

    @Test(expected = IOException.class)
    public void testGetCodeWithIOError() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        throw new IOException("Test IO Error");
    }

    @Test
    public void testGetCodeReader() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "function test() {\n  // Test code\n}";
        sourceFile.setCode(code);

        StringReader reader = new StringReader(sourceFile.getCode());
        assertEquals(code, reader.readLine());
        reader.close();
    }

    @Test(expected = IOException.class)
    public void testGetCodeReaderWithIOError() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        throw new IOException("Test IO Error");
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setCode("line 1\nline 2\nline 3");

        assertEquals(0, sourceFile.getLineOffset(1));
        assertEquals(sourceFile.getCode().length(), sourceFile.getLineOffset(3));
        assertEquals(-1, sourceFile.getLineOffset(4));
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setCode("line 1\nline 2\nline 3");

        assertEquals(3, sourceFile.getNumLines());
    }

    @Test
    public void testFindLineOffsets() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setCode("line 1\nline 2\nline 3");

        int[] lineOffsets = sourceFile.getLineOffsets();
        assertEquals(3, lineOffsets.length);
        assertEquals(0, lineOffsets[0]);
        assertEquals(sourceFile.getCode().length() - 1, lineOffsets[2]);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setCode("line 1\nline 2\nline 3");

        Region region = sourceFile.getRegion(2);
        assertEquals(1, region.getStartLine());
        assertEquals(2, region.getEndLine());
        assertEquals("line 2\n", region.getSourceCode());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setIsExtern(true);

        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern() {
        SourceFile sourceFile = new SourceFile("test.js");

        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());

        sourceFile.setIsExtern(false);
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testGetOriginalPath() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setOriginalPath("original/test.js");

        assertEquals("original/test.js", sourceFile.getOriginalPath());
    }

    @Test
    public void testSetOriginalPath() {
        SourceFile sourceFile = new SourceFile("test.js");

        sourceFile.setOriginalPath("original/test.js");
        assertEquals("original/test.js", sourceFile.getOriginalPath());

        sourceFile.setOriginalPath(null);
        assertNull(sourceFile.getOriginalPath());
    }

    @Test
    public void testFromFile() throws IOException {
        String fileName = "test.js";
        String code = "function test() {\n  // Test code\n}";
        File file = new File(fileName);
        Files.write(file, code.getBytes());

        SourceFile sourceFile = SourceFile.fromFile(fileName);
        assertEquals(code, sourceFile.getCode());

        Files.delete(file.toPath());
    }

    @Test
    public void testFromCode() {
        String fileName = "test.js";
        String code = "function test() {\n  // Test code\n}";

        SourceFile sourceFile = SourceFile.fromCode(fileName, code);
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testFromInputStream() throws IOException {
        String fileName = "test.js";
        String code = "function test() {\n  // Test code\n}";

        InputStream inputStream = new ByteArrayInputStream(code.getBytes());
        SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testFromReader() throws IOException {
        String fileName = "test.js";
        String code = "function test() {\n  // Test code\n}";

        Reader reader = new StringReader(code);
        SourceFile sourceFile = SourceFile.fromReader(fileName, reader);
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testFromGenerator() {
        String fileName = "test.js";
        String code = "function test() {\n  // Test code\n}";

        Generator generator = () -> code;
        SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testHasSourceInMemory() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setCode("function test() {\n  // Test code\n}");

        assertTrue(sourceFile.hasSourceInMemory());
    }

    @Test
    public void testClearCachedSource() {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = "function test() {\n  // Test code\n}";
        sourceFile.setCode(code);

        sourceFile.clearCachedSource();
        assertNull(sourceFile.getCode());
    }
}