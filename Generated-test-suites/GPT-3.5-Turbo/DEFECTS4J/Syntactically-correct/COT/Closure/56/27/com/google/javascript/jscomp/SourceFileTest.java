
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {
    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals(0, sourceFile.getLineOffset(1));
        assertEquals(5, sourceFile.getLineOffset(2));
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals(2, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("line 1", sourceFile.getLine(1));
        assertEquals("line 2", sourceFile.getLine(2));
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("line 1\nline 2", sourceFile.getRegion(1).getCode());
    }

    @Test
    public void testGetName() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern() {
        SourceFile sourceFile = new SourceFile("test.js");
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testFromFile() {
        SourceFile sourceFile = SourceFile.fromFile("test.js");
        assertNotNull(sourceFile);
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testFromCode() {
        SourceFile sourceFile = SourceFile.fromCode("test.js", "console.log('hello');");
        assertNotNull(sourceFile);
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testFromInputStream() {
        InputStream inputStream = new FileInputStream("test.js");
        SourceFile sourceFile = SourceFile.fromInputStream("test.js", inputStream);
        assertNotNull(sourceFile);
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testFromReader() {
        Reader reader = new StringReader("console.log('hello');");
        SourceFile sourceFile = SourceFile.fromReader("test.js", reader);
        assertNotNull(sourceFile);
        assertEquals("test.js", sourceFile.getName());
    }

    @Test
    public void testFromGenerator() {
        SourceFile.Generator generator = new SourceFile.Generator() {
            @Override
            public String getCode() {
                return "console.log('hello');";
            }
        };
        SourceFile sourceFile = SourceFile.fromGenerator("test.js", generator);
        assertNotNull(sourceFile);
        assertEquals("test.js", sourceFile.getName());
    }
}
