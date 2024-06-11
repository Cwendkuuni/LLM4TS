import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testConstructor() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertNotNull(sourceFile);
        assertEquals("TestFile", sourceFile.getName());
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertEquals(0, sourceFile.getLineOffset(1));
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertEquals(1, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertNull(sourceFile.getLine(2));
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertNull(sourceFile.getRegion(2));
    }

    @Test
    public void testFromCode() {
        SourceFile sourceFile = SourceFile.fromCode("TestFile", "some code");
        assertNotNull(sourceFile);
        assertEquals("TestFile", sourceFile.getName());
    }

    @Test
    public void testFromFile() {
        SourceFile sourceFile = SourceFile.fromFile("test.txt");
        assertNotNull(sourceFile);
        assertEquals("test.txt", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testOriginalPath() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertNull(sourceFile.getOriginalPath());
        sourceFile.setOriginalPath("OriginalPath");
        assertEquals("OriginalPath", sourceFile.getOriginalPath());
    }
}