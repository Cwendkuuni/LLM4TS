import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class SourceFileTest {

    @Test
    public void testSourceFileConstructor() {
        SourceFile sourceFile = new SourceFile("test");
        assertEquals("test", sourceFile.getName());
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test");
        int offset = sourceFile.getLineOffset(1);
        assertEquals(0, offset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffsetWithInvalidLineNumber() {
        SourceFile sourceFile = new SourceFile("test");
        sourceFile.getLineOffset(0);
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test");
        int numLines = sourceFile.getNumLines();
        assertEquals(1, numLines);
    }

    @Test
    public void testGetCode() {
        SourceFile sourceFile = new SourceFile("test");
        Exception exception = null;
        try {
            String code = sourceFile.getCode();
            assertNull(code);
        } catch (IOException e) {
            exception = e;
        }
        assertNull(exception);
    }

    @Test
    public void testGetCodeReader() {
        SourceFile sourceFile = new SourceFile("test");
        Exception exception = null;
        try {
            sourceFile.getCodeReader();
        } catch (IOException e) {
            exception = e;
        }
        assertNull(exception);
    }

    @Test
    public void testToString() {
        SourceFile sourceFile = new SourceFile("test");
        assertEquals("test", sourceFile.toString());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("test");
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern() {
        SourceFile sourceFile = new SourceFile("test");
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test");
        String line = sourceFile.getLine(1);
        assertNotNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test");
        SourceFile.Region region = sourceFile.getRegion(1);
        assertNotNull(region);
    }

    @Test
    public void testClearCachedSource() {
        SourceFile sourceFile = new SourceFile("test");
        sourceFile.clearCachedSource();
        assertNull(sourceFile.getCodeNoCache());
    }

    @Test
    public void testFromCode() {
        SourceFile sourceFile = SourceFile.fromCode("test", "code");
        assertEquals("test", sourceFile.getName());
    }

    @Test
    public void testFromInputStream() {
        Exception exception = null;
        SourceFile sourceFile = null;
        try {
            sourceFile = SourceFile.fromInputStream("test", "code".getBytes());
        } catch (IOException e) {
            exception = e;
        }
        assertNull(exception);
        assertNotNull(sourceFile);
    }

    @Test
    public void testFromReader() {
        Exception exception = null;
        SourceFile sourceFile = null;
        try {
            sourceFile = SourceFile.fromReader("test", new StringReader("code"));
        } catch (IOException e) {
            exception = e;
        }
        assertNull(exception);
        assertNotNull(sourceFile);
    }

}