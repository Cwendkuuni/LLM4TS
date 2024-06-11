package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        String code = "function test() {\n" +
                "  var x = 5;\n" +
                "  return x;\n" +
                "}";
        try {
            sourceFile.setCode(code);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedLine1 = "function test() {\n";
        String expectedLine2 = "  var x = 5;\n";
        String expectedLine3 = "  return x;\n";

        String actualLine1 = sourceFile.getLine(1);
        String actualLine2 = sourceFile.getLine(2);
        String actualLine3 = sourceFile.getLine(3);

        assertEquals(expectedLine1, actualLine1);
        assertEquals(expectedLine2, actualLine2);
        assertEquals(expectedLine3, actualLine3);

        // Test edge case where line number is greater than the total number of lines
        String actualLine4 = sourceFile.getLine(4);
        assertNull(actualLine4);

        // Test when an IO exception occurs
        try {
            String emptyCode = "";
            sourceFile.setCode(emptyCode);
            String actualLine5 = sourceFile.getLine(2);
            assertNull(actualLine5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        String code = "function test() {\n" +
                "  var x = 5;\n" +
                "  return x;\n" +
                "}";
        try {
            sourceFile.setCode(code);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int lineNumber = 2;
        Region expectedRegion = new Region(1, 2, "  var x = 5;");

        Region actualRegion = sourceFile.getRegion(lineNumber);

        assertEquals(expectedRegion.getStartLine(), actualRegion.getStartLine());
        assertEquals(expectedRegion.getEndLine(), actualRegion.getEndLine());
        assertEquals(expectedRegion.getCode(), actualRegion.getCode());

        // Test edge case where line number is not in the file
        Region actualRegion2 = sourceFile.getRegion(5);
        assertNull(actualRegion2);

        // Test when an IO exception occurs
        String emptyCode = "";
        try {
            sourceFile.setCode(emptyCode);
            Region actualRegion3 = sourceFile.getRegion(2);
            assertNull(actualRegion3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile1 = new SourceFile("testFile1.js");
        SourceFile sourceFile2 = new SourceFile("testFile2.js");

        sourceFile1.setIsExtern(true);
        assertTrue(sourceFile1.isExtern());
        assertFalse(sourceFile2.isExtern());
    }
}