import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class SourceFileTest {

    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js");
    }

    @Test
    public void testConstructor() {
        assertNotNull(sourceFile);
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testGetCode() {
        try {
            sourceFile.setCode("console.log('Hello World');");
            assertEquals("console.log('Hello World');", sourceFile.getCode());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetLine() {
        sourceFile.setCode("line 1\nline 2\nline 3\nline 4\nline 5\n");
        assertEquals("line 3", sourceFile.getLine(3));
    }

    @Test
    public void testGetLineWithInvalidLine() {
        sourceFile.setCode("line 1\nline 2\nline 3\nline 4\nline 5\n");
        assertNull(sourceFile.getLine(6));
    }

    @Test
    public void testGetRegion() {
        sourceFile.setCode("line 1\nline 2\nline 3\nline 4\nline 5\n");
        Region region = sourceFile.getRegion(3);
        assertNotNull(region);
        assertEquals("line 1\nline 2\nline 3\nline 4\nline 5", region.getContent());
    }

    @Test
    public void testGetRegionWithInvalidLine() {
        sourceFile.setCode("line 1\nline 2\nline 3\nline 4\nline 5\n");
        assertNull(sourceFile.getRegion(6));
    }

    @Test
    public void testGetNumLines() {
        sourceFile.setCode("line 1\nline 2\nline 3\nline 4\nline 5\n");
        assertEquals(5, sourceFile.getNumLines());
    }

}