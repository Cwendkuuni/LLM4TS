import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyFileName() {
        SourceFile sourceFile = new SourceFile("");
    }

    @Test
    public void testConstructorWithValidFileName() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertFalse(sourceFile.isExtern());
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testGetLineOffset() throws IOException {
        String code = "var x = 10;\nvar y = 20;\nvar z = x + y;";
        SourceFile sourceFile = SourceFile.fromCode("test.js", code);
        assertEquals(0, sourceFile.getLineOffset(1));
        assertEquals(13, sourceFile.getLineOffset(2));
        assertEquals(26, sourceFile.getLineOffset(3));
    }

    @Test
    public void testGetNumLines() throws IOException {
        String code = "var x = 10;\nvar y = 20;\nvar z = x + y;";
        SourceFile sourceFile = SourceFile.fromCode("test.js", code);
        assertEquals(3, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() throws IOException {
        String code = "var x = 10;\nvar y = 20;\nvar z = x + y;";
        SourceFile sourceFile = SourceFile.fromCode("test.js", code);
        assertEquals("var y = 20;", sourceFile.getLine(2));
        assertNull(sourceFile.getLine(4));
    }

    @Test
    public void testGetRegion() throws IOException {
        String code = "var x = 10;\nvar y = 20;\nvar z = x + y;";
        SourceFile sourceFile = SourceFile.fromCode("test.js", code);
        SourceFile.Region region = sourceFile.getRegion(2);
        assertNotNull(region);
        assertEquals(1, region.getStartLine());
        assertEquals(3, region.getEndLine());
        assertEquals("var x = 10;\nvar y = 20;\nvar z = x + y;", region.getCode());
    }

    @Test
    public void testToString() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("test.js", sourceFile.toString());
    }
}