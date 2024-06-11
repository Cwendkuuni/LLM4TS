import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullFileName() {
        SourceFile sourceFile = new SourceFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_emptyFileName() {
        SourceFile sourceFile = new SourceFile("");
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        int lineOffset = sourceFile.getLineOffset(1);
        assertEquals(0, lineOffset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffset_invalidLineNumber() {
        SourceFile sourceFile = new SourceFile("test.js");
        int lineOffset = sourceFile.getLineOffset(0);
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");
        int numLines = sourceFile.getNumLines();
        assertEquals(1, numLines);
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test.js");
        String line = sourceFile.getLine(1);
        assertEquals("", line);
    }

    @Test
    public void testGetLine_nullLine() {
        SourceFile sourceFile = new SourceFile("test.js");
        String line = sourceFile.getLine(2);
        assertNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test.js");
        Region region = sourceFile.getRegion(1);
        assertNotNull(region);
    }

    @Test
    public void testGetRegion_nullRegion() {
        SourceFile sourceFile = new SourceFile("test.js");
        Region region = sourceFile.getRegion(2);
        assertNull(region);
    }
}