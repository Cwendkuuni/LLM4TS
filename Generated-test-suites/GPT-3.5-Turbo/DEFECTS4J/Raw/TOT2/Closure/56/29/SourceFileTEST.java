import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testGetCode() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("", sourceFile.getCode());
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals(0, sourceFile.getLineOffset(1));
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
    public void testFromFileWithCharset() {
        SourceFile sourceFile = SourceFile.fromFile("test.txt", Charset.forName("UTF-8"));
        assertEquals("test.txt", sourceFile.getName());
    }

    @Test
    public void testFromInputStream() {
        // Test for IOException
        // Will fail if IOException is not caught and handled properly
        try {
            SourceFile sourceFile = SourceFile.fromInputStream("test.txt", "test".getBytes());
            assertNotNull(sourceFile);
        } catch (IOException e) {
            fail("IOException not handled");
        }
    }

    @Test
    public void testFromReader() {
        // Test for IOException
        // Will fail if IOException is not caught and handled properly
        try {
            SourceFile sourceFile = SourceFile.fromReader("test.txt", new StringReader("test"));
            assertNotNull(sourceFile);
        } catch (IOException e) {
            fail("IOException not handled");
        }
    }

}