import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import java.io.IOException;

public class SourceFileTest {

    @Test
    public void testSourceFileConstructor() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("test.js", sourceFile.getName());
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals(0, sourceFile.getLineOffset(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffsetInvalidLineNumber() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.getLineOffset(0);
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals(1, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertNull(sourceFile.getLine(2));
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertNull(sourceFile.getRegion(2));
    }

    @Test
    public void testFromCode() {
        SourceFile sourceFile = SourceFile.fromCode("test.js", "var a = 10;");
        assertNotNull(sourceFile);
    }

    @Test
    public void testFromInputStream() throws IOException {
        SourceFile sourceFile = SourceFile.fromInputStream("test.js", "var a = 10;");
        assertNotNull(sourceFile);
    }

}