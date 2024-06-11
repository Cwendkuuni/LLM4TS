package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.SourceFile;

public class SourceFileTest {

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals(0, sourceFile.getLineOffset(1));
        assertEquals(3, sourceFile.getLineOffset(3));
        assertEquals(11, sourceFile.getLineOffset(11));
        assertEquals(15, sourceFile.getLineOffset(11, 5));
        assertEquals(19, sourceFile.getLineOffset(11, 9));
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals(10, sourceFile.getNumLines());
    }

    @Test
    public void testFindLineOffsets() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.findLineOffsets();
        int[] lineOffsets = sourceFile.lineOffsets;
        assertArrayEquals(new int[]{0, 1, 3, 9, 11, 15, 18, 19, 22, 25}, lineOffsets);
    }

    @Test
    public void testGetCode() throws IOException {
        String code = "function foo() {\n" +
                "  //this is a test\n" +
                "}";
        SourceFile sourceFile = new SourceFile("test.js") {
            @Override
            public String getCode() throws IOException {
                return code;
            }
        };
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testGetCodeReader() throws IOException {
        String code = "function foo() {\n" +
                "  //this is a test\n" +
                "}";
        SourceFile sourceFile = new SourceFile("test.js") {
            @Override
            public Reader getCodeReader() throws IOException {
                return new StringReader(code);
            }
        };
        assertEquals(code, CharStreams.toString(sourceFile.getCodeReader()));
    }

    @Test
    public void testGetOriginalPath() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("test.js", sourceFile.getOriginalPath());
    }

    @Test
    public void testSetOriginalPath() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setOriginalPath("test2.js");
        assertEquals("test2.js", sourceFile.getOriginalPath());
    }

    @Test
    public void testGetLine() {
        String code = "function foo() {\n" +
                "  //this is line 1\n" +
                "  //this is line 2\n" +
                "  //this is line 3\n" +
                "}";
        SourceFile sourceFile = new SourceFile("test.js") {
            @Override
                public String getCode() throws IOException {
                return code;
            }
        };
        assertEquals("//this is line 1\n", sourceFile.getLine(1));
        assertEquals("//this is line 2\n", sourceFile.getLine(2));
        assertEquals("//this is line 3\n", sourceFile.getLine(3));
        assertNull(sourceFile.getLine(4));
    }

    @Test
    public void testGetRegion() {
        String code = "function foo() {\n" +
                "  //this is line 1\n" +
                "  //this is line 2\n" +
                "  //this is line 3\n" +
                "}";
        SourceFile sourceFile = new SourceFile("test.js") {
            @Override
            public String getCode() throws IOException {
                return code;
            }
        };
        Region region = sourceFile.getRegion(3);
        assertEquals(2, region.getStartLine());
        assertEquals(3, region.getEndLine());
        assertEquals("  //this is line 2\n  //this is line 3\n", region.getCode());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertFalse(sourceFile.isExtern());
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testFromFile() {
        String filePath = "test.js";
        SourceFile sourceFile = SourceFile.fromFile(filePath);
        assertEquals(filePath, sourceFile.getName());
        assertNull(sourceFile.getOriginalPath());
        assertFalse(sourceFile.hasSourceInMemory());
        assertNull(sourceFile.getCode());
    }

    @Test
    public void testFromFileWithCharset() throws IOException {
        String filePath = "test.js";
        String code = "function foo() {\n" +
                "  //this is a test\n" +
                "}";
        SourceFile sourceFile = SourceFile.fromFile(filePath, Charsets.UTF_8);
        assertEquals(filePath, sourceFile.getName());
        assertNull(sourceFile.getOriginalPath());
        assertTrue(sourceFile.hasSourceInMemory());
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testFromCode() {
        String fileName = "test.js";
        String code = "function foo() {\n" +
                "  //this is a test\n" +
                "}";
        SourceFile sourceFile = SourceFile.fromCode(fileName, code);
        assertEquals(fileName, sourceFile.getName());
        assertNull(sourceFile.getOriginalPath());
        assertTrue(sourceFile.hasSourceInMemory());
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testFromCodeWithOriginalPath() {
        String fileName = "test.js";
        String originalPath = "test2.js";
        String code = "function foo() {\n" +
                "  //this is a test\n" +
                "}";
        SourceFile sourceFile = SourceFile.fromCode(fileName, originalPath, code);
        assertEquals(fileName, sourceFile.getName());
        assertEquals(originalPath, sourceFile.getOriginalPath());
        assertTrue(sourceFile.hasSourceInMemory());
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testFromReader() throws IOException {
        String fileName = "test.js";
        String code = "function foo() {\n" +
                "  //this is a test\n" +
                "}";
        SourceFile sourceFile = SourceFile.fromReader(fileName, new StringReader(code));
        assertEquals(fileName, sourceFile.getName());
        assertNull(sourceFile.getOriginalPath());
        assertTrue(sourceFile.hasSourceInMemory());
        assertEquals(code, sourceFile.getCode());
    }

    @Test
    public void testFromGenerator() {
        String fileName = "test.js";
        SourceFileGenerator generator = mock(SourceFileGenerator.class);
        when(generator.getCode()).thenReturn("function foo() {\n" +
                "  //this is a test\n" +
                "}");
        SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
        assertEquals(fileName, sourceFile.getName());
        assertNull(sourceFile.getOriginalPath());
        assertTrue(sourceFile.hasSourceInMemory());
        assertEquals("function foo() {\n" +
                "  //this is a test\n" +
                "}", sourceFile.getCode());
    }
}

interface SourceFileGenerator {
    String getCode();
}