package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testGetLine() {
        SourceFile sf = new SourceFile("test.js");
        sf.setCode("line 1\nline 2\nline 3");

        assertEquals("line 1", sf.getLine(1));
        assertEquals("line 2", sf.getLine(2));
        assertEquals("line 3", sf.getLine(3));
        assertNull(sf.getLine(4));
        assertNull(sf.getLine(-1));
    }

    @Test
    public void testGetRegion() {
        SourceFile sf = new SourceFile("test.js");
        sf.setCode("line 1\nline 2\nline 3\nline 4");

        Region region = sf.getRegion(3);
        assertEquals(3, region.getStartLine());
        assertEquals(3, region.getEndLine());
        assertEquals("line 3\nline 4", region.getCode());

        region = sf.getRegion(1);
        assertEquals(1, region.getStartLine());
        assertEquals(2, region.getEndLine());
        assertEquals("line 1", region.getCode());

        region = sf.getRegion(5);
        assertNull(region);

        region = sf.getRegion(0);
        assertNull(region);
    }

    @Test
    public void testFromFile() throws IOException {
        SourceFile sf = SourceFile.fromFile("test.txt");
        assertTrue(sf.hasSourceInMemory());
        assertEquals("test content", sf.getCode());

        sf = SourceFile.fromFile("nonexistentfile.txt");
        assertFalse(sf.hasSourceInMemory());
        assertNull(sf.getCode());
    }

    @Test
    public void testFromCode() {
        String code = "function test() { return 42; }";
        SourceFile sf = SourceFile.fromCode("test.js", code);
        assertEquals("test.js", sf.getName());
        assertEquals(code, sf.getCode());
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sf = new SourceFile("test.js");
        sf.setCode("function test() { return 42; }");

        assertEquals(1, sf.getLineOffset(1));
        assertEquals(14, sf.getLineOffset(2));
        assertEquals(14 + 2, sf.getLineOffset(3));
        assertEquals(53, sf.getLineOffset(5));
        assertEquals(53 + 2, sf.getLineOffset(6));
    }

    @Test
    public void testGetNumLines() {
        SourceFile sf = new SourceFile("test.js");
        sf.setCode("function test() { return 42; }");
        assertEquals(6, sf.getNumLines());
    }
}