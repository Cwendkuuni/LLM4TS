package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SourceFileTest {

    @Test
    public void testFindLineOffsets() throws IOException {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        int[] lineOffsets = sourceFile.getLineOffsets();

        assertEquals(3, lineOffsets.length);
        assertEquals(0, lineOffsets[0]);
        assertEquals(5, lineOffsets[1]);
        assertEquals(11, lineOffsets[2]);
    }

    @Test
    public void testGetLine() throws IOException {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        assertEquals("line1", sourceFile.getLine(1));
        assertEquals("line2", sourceFile.getLine(2));
        assertEquals("line3", sourceFile.getLine(3));
        assertNull(sourceFile.getLine(4));
    }

    @Test
    public void testGetRegion() throws IOException {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        Region region = sourceFile.getRegion(3);

        assertEquals(1, region.getStartLineNumber());
        assertEquals(3, region.getEndLineNumber());
        assertEquals("line2\nline3", region.getSource());
    }

    @Test
    public void testGetCode() throws IOException {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testGetCodeNoCache() throws IOException {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        assertEquals(code, sourceFile.getCodeNoCache());
    }

    @Test
    public void testGetOriginalPath() {
        SourceFile sourceFile = new Preloaded("testFile", "testFileContent");

        assertEquals("testFile", sourceFile.getOriginalPath());
    }

    @Test
    public void testSetOriginalPath() {
        SourceFile sourceFile = new Preloaded("testFile", "testFileContent");

        sourceFile.setOriginalPath("testOriginalPath");

        assertEquals("testOriginalPath", sourceFile.getOriginalPath());
    }

    @Test
    public void testHasSourceInMemory() throws IOException {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        assertTrue(sourceFile.hasSourceInMemory());

        code = null;
        sourceFile.clearCachedSource();

        assertFalse(sourceFile.hasSourceInMemory());
    }

    @Test
    public void testClearCachedSource() throws IOException {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        assertEquals(code, sourceFile.getCode());

        sourceFile.clearCachedSource();

        assertNull(sourceFile.getCode());
    }

    @Test
    public void testName() {
        SourceFile sourceFile = new Preloaded("testFile", "testFileContent");

        assertEquals("testFile", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new Preloaded("testFile", "testFileContent");

        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern() {
        SourceFile sourceFile = new Preloaded("testFile", "testFileContent");

        sourceFile.setIsExtern(true);

        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testGetLineOffset() {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        assertEquals(0, sourceFile.getLineOffset(1));
        assertEquals(5, sourceFile.getLineOffset(2));
        assertEquals(11, sourceFile.getLineOffset(3));
    }

    @Test
    public void testGetNumLines() {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        assertEquals(3, sourceFile.getNumLines());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffsetInvalidLineNumber() {
        String code = "line1\nline2\nline3";
        SourceFile sourceFile = new Preloaded("testFile", code);

        sourceFile.getLineOffset(4);
    }

    @Test
    public void testFromFile() throws IOException {
        String filePath = "src/test/java/com/google/javascript/jscomp/SourceFileTest.java";
        SourceFile sourceFile = SourceFile.fromFile(filePath);

        assertEquals(filePath, sourceFile.getName());
        assertTrue(sourceFile.hasSourceInMemory());
    }

    @Test
    public void testFromInputStream() throws IOException {
        String code = "line1\nline2\nline3";
        InputStream inputStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
        SourceFile sourceFile = SourceFile.fromInputStream("testFile", inputStream);

        assertEquals("testFile", sourceFile.getName());
        assertTrue(sourceFile.hasSourceInMemory());
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testFromReader() throws IOException {
        String code = "line1\nline2\nline3";
        Reader reader = new InputStreamReader(new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        SourceFile sourceFile = SourceFile.fromReader("testFile", reader);

        assertEquals("testFile", sourceFile.getName());
        assertTrue(sourceFile.hasSourceInMemory());
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testFromGenerator() throws IOException {
        String code = "line1\nline2\nline3";
        SourceFileGenerator generator = new SourceFileGenerator() {
            @Override
            public String getCode() {
                return code;
            }
        };
        SourceFile sourceFile = SourceFile.fromGenerator("testFile", generator);

        assertEquals("testFile", sourceFile.getName());
        assertTrue(sourceFile.hasSourceInMemory());
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testOnDisk() throws IOException {
        String filePath = "src/test/java/com/google/javascript/jscomp/SourceFileTest.java";
        SourceFile sourceFile = SourceFile.fromFile(filePath);

        assertSame(sourceFile, SourceFile.fromFile(filePath));
    }

    @Test
    public void testFromFileWithCharset() throws IOException {
        String filePath = "src/test/java/com/google/javascript/jscomp/SourceFileTest.java";
        Charset charset = StandardCharsets.UTF_8;
        SourceFile sourceFile = SourceFile.fromFile(filePath, charset);

        assertEquals(filePath, sourceFile.getName());
        assertTrue(sourceFile.hasSourceInMemory());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFromFileNotFound() throws IOException {
        String filePath = "does_not_exist.java";
        SourceFile sourceFile = SourceFile.fromFile(filePath);
    }
}

class SourceFileGenerator implements SourceFile.Generator {
    @Override
    public String getCode() {
        return "line1\nline2\nline3";
    }
}