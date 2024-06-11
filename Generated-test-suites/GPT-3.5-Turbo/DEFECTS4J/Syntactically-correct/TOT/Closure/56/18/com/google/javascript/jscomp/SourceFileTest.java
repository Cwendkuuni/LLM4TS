import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    private SourceFile sourceFile;

    @Before
    public void setup() {
        sourceFile = new SourceFile("test.java");
    }

    @Test
    public void testGetCode() {
        try {
            String code = sourceFile.getCode();
            assertEquals("", code);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCodeReader() {
        try {
            Reader codeReader = sourceFile.getCodeReader();
            assertNotNull(codeReader);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetOriginalPath() {
        String originalPath = sourceFile.getOriginalPath();
        assertEquals("test.java", originalPath);
    }

    @Test
    public void testSetOriginalPath() {
        sourceFile.setOriginalPath("newTest.java");
        String originalPath = sourceFile.getOriginalPath();
        assertEquals("newTest.java", originalPath);
    }

    @Test
    public void testClearCachedSource() {
        sourceFile.setCode("test code");
        sourceFile.clearCachedSource();
        try {
            String code = sourceFile.getCode();
            assertEquals("", code);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetName() {
        String name = sourceFile.getName();
        assertEquals("test.java", name);
    }

    @Test
    public void testIsExtern() {
        boolean isExtern = sourceFile.isExtern();
        assertFalse(isExtern);
    }

    @Test
    public void testSetIsExtern() {
        sourceFile.setIsExtern(true);
        boolean isExtern = sourceFile.isExtern();
        assertTrue(isExtern);
    }

    @Test
    public void testGetLineOffset() {
        try {
            int lineOffset = sourceFile.getLineOffset(1);
            assertEquals(0, lineOffset);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetNumLines() {
        int numLines = sourceFile.getNumLines();
        assertEquals(1, numLines);
    }

    @Test
    public void testGetLine() {
        String line = sourceFile.getLine(1);
        assertNull(line);
    }

    @Test
    public void testGetRegion() {
        Region region = sourceFile.getRegion(1);
        assertNull(region);
    }
}