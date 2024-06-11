import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testConstructor() {
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
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        int offset = sourceFile.getLineOffset(1);
        assertEquals(0, offset);
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");
        int lines = sourceFile.getNumLines();
        assertEquals(0, lines);
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test.js");
        String line = sourceFile.getLine(1);
        assertNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test.js");
        Region region = sourceFile.getRegion(1);
        assertNull(region);
    }

    @Test
    public void testGetCode() {
        SourceFile sourceFile = new SourceFile("test.js");
        String code = null;
        try {
            code = sourceFile.getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(code);
    }

    @Test
    public void testGetOriginalPath() {
        SourceFile sourceFile = new SourceFile("test.js");
        String originalPath = sourceFile.getOriginalPath();
        assertEquals("test.js", originalPath);
    }

    @Test
    public void testSetOriginalPath() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setOriginalPath("path/test.js");
        String originalPath = sourceFile.getOriginalPath();
        assertEquals("path/test.js", originalPath);
    }

    @Test
    public void testClearCachedSource() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.clearCachedSource();
        assertEquals(null, sourceFile.getCode());
    }
}