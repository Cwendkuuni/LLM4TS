import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testConstructor() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        assertNotNull(sourceFile);
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        assertEquals(0, sourceFile.getLineOffset(1));
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        assertEquals(1, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        assertNull(sourceFile.getLine(2));
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        assertNull(sourceFile.getRegion(2));
    }

    @Test
    public void testGetName() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        assertEquals("testFile.js", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetOriginalPath() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        sourceFile.setOriginalPath("originalPath");
        assertEquals("originalPath", sourceFile.getOriginalPath());
    }

    @Test
    public void testClearCachedSource() {
        SourceFile sourceFile = new SourceFile("testFile.js");
        sourceFile.clearCachedSource();
        assertFalse(sourceFile.hasSourceInMemory());
    }

    @Test
    public void testFromCode() {
        SourceFile sourceFile = SourceFile.fromCode("testFile.js", "sample code");
        assertNotNull(sourceFile);
    }

    @Test
    public void testFromInputStream() {
        // Provide inputStream content here if necessary
        // SourceFile sourceFile = SourceFile.fromInputStream("testFile.js", inputStream);
        // assertNotNull(sourceFile);
    }

    @Test
    public void testFromReader() {
        // Provide Reader content here if necessary
        // SourceFile sourceFile = SourceFile.fromReader("testFile.js", reader);
        // assertNotNull(sourceFile);
    }

}