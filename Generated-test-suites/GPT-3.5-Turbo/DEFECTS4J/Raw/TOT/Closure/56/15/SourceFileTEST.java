import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testSourceFileConstructor() {
        SourceFile sourceFile = new SourceFile("testFile");
        assertNotNull(sourceFile);
        assertEquals("testFile", sourceFile.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSourceFileConstructorWithEmptyFileName() {
        new SourceFile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSourceFileConstructorWithNullFileName() {
        new SourceFile(null);
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("testFile");
        int offset = sourceFile.getLineOffset(1);
        assertEquals(0, offset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffsetWithInvalidLineNumber() {
        SourceFile sourceFile = new SourceFile("testFile");
        int offset = sourceFile.getLineOffset(0);
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("testFile");
        int numLines = sourceFile.getNumLines();
        assertEquals(1, numLines);
    }

    @Test
    public void testGetCode() throws IOException {
        SourceFile sourceFile = new SourceFile("testFile");
        String code = sourceFile.getCode();
        assertNull(code);
    }

    @Test
    public void testGetCodeReader() throws IOException {
        SourceFile sourceFile = new SourceFile("testFile");
        Reader reader = sourceFile.getCodeReader();
        assertNotNull(reader);
    }

    @Test
    public void testGetOriginalPath() {
        SourceFile sourceFile = new SourceFile("testFile");
        String originalPath = sourceFile.getOriginalPath();
        assertEquals("testFile", originalPath);
    }

    @Test
    public void testSetOriginalPath() {
        SourceFile sourceFile = new SourceFile("testFile");
        sourceFile.setOriginalPath("originalPath");
        String originalPath = sourceFile.getOriginalPath();
        assertEquals("originalPath", originalPath);
    }

    @Test
    public void testClearCachedSource() {
        SourceFile sourceFile = new SourceFile("testFile");
        sourceFile.setCode("code");
        sourceFile.clearCachedSource();
        assertNull(sourceFile.getCode());
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
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("testFile");
        String line = sourceFile.getLine(1);
        assertNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("testFile");
        Region region = sourceFile.getRegion(1);
        assertNull(region);
    }
}