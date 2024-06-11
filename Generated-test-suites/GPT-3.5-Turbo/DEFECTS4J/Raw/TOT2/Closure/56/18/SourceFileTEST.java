import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testConstructor() {
        SourceFile sourceFile = new SourceFile("testFile");
        assertNotNull(sourceFile);
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("testFile");
        int offset = sourceFile.getLineOffset(1);
        assertEquals(0, offset);
    }

    @Test
    public void testGetLineOffset_InvalidLineNumber() {
        SourceFile sourceFile = new SourceFile("testFile");
        assertThrows(IllegalArgumentException.class, () -> {
            sourceFile.getLineOffset(0);
        });
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("testFile");
        int numLines = sourceFile.getNumLines();
        assertEquals(1, numLines);
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("testFile");
        String line = sourceFile.getLine(1);
        assertNotNull(line);
    }

    @Test
    public void testGetLine_InvalidLineNumber() {
        SourceFile sourceFile = new SourceFile("testFile");
        String line = sourceFile.getLine(2);
        assertNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("testFile");
        Region region = sourceFile.getRegion(1);
        assertNotNull(region);
    }

    @Test
    public void testGetRegion_InvalidLineNumber() {
        SourceFile sourceFile = new SourceFile("testFile");
        Region region = sourceFile.getRegion(2);
        assertNull(region);
    }
  
    @Test
    public void testToString() {
        SourceFile sourceFile = new SourceFile("testFile");
        String name = sourceFile.toString();
        assertEquals("testFile", name);
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("testFile");
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern() {
        SourceFile sourceFile = new SourceFile("testFile");
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testClearCachedSource() {
        SourceFile sourceFile = new SourceFile("testFile");
        sourceFile.clearCachedSource();
        assertNotNull(sourceFile);
    }
}