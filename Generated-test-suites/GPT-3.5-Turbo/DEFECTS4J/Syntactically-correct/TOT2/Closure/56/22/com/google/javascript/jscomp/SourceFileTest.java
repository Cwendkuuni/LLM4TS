import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testGetCode() {
        String code = "Sample code for testing";
        SourceFile sourceFile = new SourceFile("TestFile");
        sourceFile.setCode(code);
        try {
            assertEquals(code, sourceFile.getCode());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetLine() {
        String code = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        SourceFile sourceFile = new SourceFile("TestFile");
        sourceFile.setCode(code);
        assertEquals("Line 3", sourceFile.getLine(3));
    }

    @Test
    public void testGetRegion() {
        String code = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        SourceFile sourceFile = new SourceFile("TestFile");
        sourceFile.setCode(code);
        Region region = sourceFile.getRegion(3);
        assertNotNull(region);
        assertEquals(3, region.getStartLine());
        assertEquals(7, region.getEndLine());
        assertEquals("Line 2\nLine 3\nLine 4\n", region.getContent());
    }

    @Test
    public void testGetOriginalPath() {
        String originalPath = "Path/to/original/file";
        SourceFile sourceFile = new SourceFile("TestFile");
        sourceFile.setOriginalPath(originalPath);
        assertEquals(originalPath, sourceFile.getOriginalPath());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertFalse(sourceFile.isExtern());
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    // Add more test cases for other methods as needed

}