package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.io.Charsets;

public class SourceFileTest {

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("testSourceFile.js");
        String expectedCode = "function testFunction() {\n" +
                "  var x = 5;\n" +
                "  return x;\n" +
                "}";
        try {
            sourceFile.setCode(expectedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line1 = sourceFile.getLine(1);
        String line2 = sourceFile.getLine(2);
        String line3 = sourceFile.getLine(3);

        assertEquals("function testFunction() {\n", line1);
        assertEquals("  var x = 5;\n", line2);
        assertEquals("  return x;\n", line3);

        try {
            String line4 = sourceFile.getLine(5);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("testSourceFile.js");
        String expectedCode = "function testFunction() {\n" +
                "  var x = 5;\n" +
                "  return x;\n" +
                "}";
        try {
            sourceFile.setCode(expectedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Region region = sourceFile.getRegion(3);

        assertEquals(3, region.getStartLineNumber());
        assertEquals(3, region.getEndLineNumber());
        assertEquals("  return x;\n", region.getSource());
    }

    @Test
    public void testFromFile() throws IOException {
        String fileName = "testSourceFile.js";
        File file = new File(fileName);
        Charset charset = Charsets.UTF_8;
        SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
        String expectedCode = Files.toString(file, charset);

        assertEquals(expectedCode, sourceFile.getCode());
        assertEquals(fileName, sourceFile.getName());
    }

    @Test
    public void testFromCode() {
        String fileName = "testSourceFile.js";
        String code = "function testFunction() {\n" +
                "  var x = 5;\n" +
                "  return x;\n" +
                "}";
        SourceFile sourceFile = SourceFile.fromCode(fileName, code);

        assertEquals(code, sourceFile.getCode());
        assertEquals(fileName, sourceFile.getName());
    }
}