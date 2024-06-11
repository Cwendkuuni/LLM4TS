
import org.junit.Test;

import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testGetName() {
        SourceFile sourceFile = new SourceFile("Test.java");
        assertEquals("Test.java", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("Test.java");
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("This is line 1.\nThis is line 2.");
        int lineOffset = sourceFile.getLineOffset(2);
        assertEquals(18, lineOffset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffsetWithInvalidLineNumber() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("This is line 1.");
        sourceFile.getLineOffset(2);
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("This is line 1.\nThis is line 2.");
        String line = sourceFile.getLine(2);
        assertEquals("This is line 2.", line);
    }

    @Test
    public void testGetLineWhenLineNumberIsEndOfFile() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("This is line 1.");
        String line = sourceFile.getLine(2);
        assertNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("This is line 1.\nThis is line 2.\nThis is line 3.\nThis is line 4.\nThis is line 5.\nThis is line 6.");
        SourceFile.Region region = sourceFile.getRegion(3);
        assertNotNull(region);
        assertEquals(2, region.getStartLine());
        assertEquals(6, region.getEndLine());
        assertEquals("This is line 1.\nThis is line 2.\nThis is line 3.\nThis is line 4.\nThis is line 5.", region.getCode());
    }

    @Test
    public void testGetRegionWhenLineNumberIsEndOfFile() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("This is line 1.\nThis is line 2.\nThis is line 3.");
        SourceFile.Region region = sourceFile.getRegion(4);
        assertNull(region);
    }
}
