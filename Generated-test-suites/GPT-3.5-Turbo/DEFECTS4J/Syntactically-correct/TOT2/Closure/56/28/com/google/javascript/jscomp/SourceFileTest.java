import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class SourceFileTest {

    @Test
    public void testGetCode() throws IOException {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertNotNull(sourceFile.getCode());
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
    public void testToString() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertEquals("TestFile", sourceFile.toString());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testClearCachedSource() {
        SourceFile sourceFile = new SourceFile("TestFile");
        sourceFile.clearCachedSource();
        assertNull(sourceFile.getCodeNoCache());
    }

    @Test
    public void testFromCode() {
        SourceFile sourceFile = SourceFile.fromCode("TestFile", "TestCode");
        assertEquals("TestFile", sourceFile.getName());
        assertEquals("TestCode", sourceFile.getCodeNoCache());
    }

    @Test
    public void testFromInputStream() throws IOException {
        SourceFile sourceFile = SourceFile.fromInputStream("TestFile", "TestCode".getBytes());
        assertEquals("TestFile", sourceFile.getName());
        assertEquals("TestCode", sourceFile.getCode());
    }

}