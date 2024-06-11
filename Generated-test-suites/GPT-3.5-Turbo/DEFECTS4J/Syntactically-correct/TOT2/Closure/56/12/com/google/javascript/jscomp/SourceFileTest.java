import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class SourceFileTest {

    @Test
    public void testSourceFileConstructor() {
        SourceFile sourceFile = new SourceFile("test.java");
        assertNotNull(sourceFile);
        assertEquals("test.java", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("test.java");
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern() {
        SourceFile sourceFile = new SourceFile("test.java");
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.java");
        int offset = sourceFile.getLineOffset(1);
        assertEquals(0, offset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffsetWithInvalidLine() {
        SourceFile sourceFile = new SourceFile("test.java");
        sourceFile.getLineOffset(0);
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.java");
        int numLines = sourceFile.getNumLines();
        assertEquals(1, numLines);
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test.java");
        String line = sourceFile.getLine(1);
        assertNotNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test.java");
        Region region = sourceFile.getRegion(1);
        assertNotNull(region);
    }

    @Test
    public void testToString() {
        SourceFile sourceFile = new SourceFile("test.java");
        assertEquals("test.java", sourceFile.toString());
    }

    @Test
    public void testClearCachedSource() {
        SourceFile sourceFile = new SourceFile("test.java");
        sourceFile.clearCachedSource();
        assertFalse(sourceFile.hasSourceInMemory());
    }

    @Test
    public void testFromCode() {
        SourceFile sourceFile = SourceFile.fromCode("test.java", "code");
        assertNotNull(sourceFile);
        assertEquals("test.java", sourceFile.getName());
    }

    @Test
    public void testFromInputStream() throws IOException {
        SourceFile sourceFile = SourceFile.fromInputStream("test.java", "inputstream");
        assertNotNull(sourceFile);
        assertEquals("test.java", sourceFile.getName());
    }

}