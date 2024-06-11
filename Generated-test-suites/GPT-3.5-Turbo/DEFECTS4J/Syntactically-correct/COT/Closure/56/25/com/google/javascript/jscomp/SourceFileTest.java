
import org.junit.*;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testConstructor() {
        SourceFile sourceFile = new SourceFile("test.js");
        assertEquals("test.js", sourceFile.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFileName() {
        SourceFile sourceFile = new SourceFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyFileName() {
        SourceFile sourceFile = new SourceFile("");
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("test.js");
        int lineOffset = sourceFile.getLineOffset(1);
        assertEquals(0, lineOffset);
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("test.js");
        int numLines = sourceFile.getNumLines();
        assertEquals(1, numLines);
    }

    @Test
    public void testGetLine() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        String line = sourceFile.getLine(1);
        assertEquals("", line);
    }

    @Test
    public void testGetRegion() throws IOException {
        SourceFile sourceFile = new SourceFile("test.js");
        Region region = sourceFile.getRegion(1);
        assertEquals("", region.getText());
    }

    @Test
    public void testFromFile() {
        SourceFile sourceFile = SourceFile.fromFile("test.js");
        assertEquals("test.js", sourceFile.getName());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFromFileNonExistentFile() {
        SourceFile sourceFile = SourceFile.fromFile("nonexistent.js");
    }

    @Test
    public void testFromCode() {
        SourceFile sourceFile = SourceFile.fromCode("test.js", "var test = true;");
        assertEquals("var test = true;", sourceFile.getCode());
    }

    @Test
    public void testFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.js");
        SourceFile sourceFile = SourceFile.fromInputStream("test.js", inputStream);
        assertEquals("var test = true;", sourceFile.getCode());
    }

    @Test
    public void testFromReader() throws IOException {
        Reader reader = new StringReader("var test = true;");
        SourceFile sourceFile = SourceFile.fromReader("test.js", reader);
        assertEquals("var test = true;", sourceFile.getCode());
    }

    @Test
    public void testFromGenerator() {
        SourceFile.Generator generator = new SourceFile.Generator() {
            @Override
            public String getCode() {
                return "var test = true;";
            }
        };
        SourceFile sourceFile = SourceFile.fromGenerator("test.js", generator);
        assertEquals("var test = true;", sourceFile.getCode());
    }

}
