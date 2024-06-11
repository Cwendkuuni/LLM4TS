import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class SourceFileTest {

    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js");
    }

    @Test
    public void testSourceFileConstructor() {
        assertNotNull(sourceFile);
    }

    @Test
    public void testGetLineOffset() {
        assertEquals(0, sourceFile.getLineOffset(1));
    }

    @Test
    public void testGetNumLines() {
        assertEquals(1, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() {
        assertEquals("public interface Generator {", sourceFile.getLine(1));
    }

    @Test
    public void testGetRegion() {
        Region region = sourceFile.getRegion(1);
        assertNotNull(region);
        assertEquals(1, region.getStartLine());
        assertEquals(1, region.getEndLine());
        assertEquals("public interface Generator {", region.getCode());
    }

    @Test
    public void testGetCode() {
        try {
            String code = sourceFile.getCode();
            assertNotNull(code);
            assertTrue(code.contains("public interface Generator {"));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetCodeReader() {
        try {
            Reader codeReader = sourceFile.getCodeReader();
            assertNotNull(codeReader);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetName() {
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern() {
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testGetOriginalPath() {
        assertNull(sourceFile.getOriginalPath());
    }

    @Test
    public void testSetOriginalPath() {
        sourceFile.setOriginalPath("original/test.js");
        assertEquals("original/test.js", sourceFile.getOriginalPath());
    }

    @Test
    public void testClearCachedSource() {
        sourceFile.clearCachedSource();
        assertFalse(sourceFile.hasSourceInMemory());
    }
}