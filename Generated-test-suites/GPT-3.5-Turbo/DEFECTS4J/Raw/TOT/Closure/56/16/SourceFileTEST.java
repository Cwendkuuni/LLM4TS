import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js");
    }

    @Test
    public void testGetLineOffset() {
        // Typical case - should return correct line offset
        int lineOffset = sourceFile.getLineOffset(2);
        assertEquals(0, lineOffset);

        // Edge case - first line, should return offset 0
        lineOffset = sourceFile.getLineOffset(1);
        assertEquals(0, lineOffset);

        // Edge case - last line, should return offset 1 less than code length
        lineOffset = sourceFile.getLineOffset(25);
        assertEquals(sourceFile.getCode().length() - 1, lineOffset);

        // Error scenario - line number out of bound, should throw IllegalArgumentException
        try {
            sourceFile.getLineOffset(30);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testGetNumLines() {
        // Typical case - should return correct number of lines in the code
        int numLines = sourceFile.getNumLines();
        assertEquals(25, numLines);

        // Edge case - empty code, should return 1 line
        sourceFile.setCode("");
        numLines = sourceFile.getNumLines();
        assertEquals(1, numLines);
    }

    @Test
    public void testGetLine() {
        // Typical case - should return correct line of code
        String line = sourceFile.getLine(5);
        assertEquals("private String code = null;", line);

        // Edge case - first line, should return first line of code
        line = sourceFile.getLine(1);
        assertEquals("/*", line);

        // Edge case - last line, should return last line of code
        line = sourceFile.getLine(25);
        assertEquals("}", line);

        // Error scenario - line number out of bound, should return null
        line = sourceFile.getLine(30);
        assertNull(line);
    }

    @Test
    public void testGetRegion() {
        // Typical case - should return correct region of code
        SourceFile.Region region = sourceFile.getRegion(5);
        assertEquals(3, region.getStartLine());
        assertEquals(7, region.getEndLine());
        assertEquals("private String code = null;\n\n  /**", region.getCode());

        // Edge case - first line, should return first 5 lines of code
        region = sourceFile.getRegion(1);
        assertEquals(1, region.getStartLine());
        assertEquals(5, region.getEndLine());
        assertEquals("/*\n * Copyright 2009 The", region.getCode());

        // Edge case - last line, should return last 5 lines of code
        region = sourceFile.getRegion(25);
        assertEquals(21, region.getStartLine());
        assertEquals(25, region.getEndLine());
        assertEquals("  public void clearCachedSource() {\n    super.setCode(null);\n  }\n\n  boolean hasSourceInMemory() {\n    return code != null;\n  }", region.getCode());

        // Error scenario - line number out of bound, should return null
        region = sourceFile.getRegion(30);
        assertNull(region);
    }
}