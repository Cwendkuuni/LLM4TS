import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testGetLineOffset() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("TestFile");
        int offset = sourceFile.getLineOffset(1);
        assertEquals(0, offset);

        // Edge cases
        try {
            sourceFile.getLineOffset(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            sourceFile.getLineOffset(100);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("TestFile");
        String line = sourceFile.getLine(1);
        assertNotNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("TestFile");
        Region region = sourceFile.getRegion(1);
        assertNotNull(region);

        // Edge cases
        Region nullRegion = sourceFile.getRegion(100);
        assertNull(nullRegion);
    }

    @Test
    public void testGetCode() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("TestFile");
        try {
            String code = sourceFile.getCode();
            assertNotNull(code);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetName() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertEquals("TestFile", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertFalse(sourceFile.isExtern());
    }
}