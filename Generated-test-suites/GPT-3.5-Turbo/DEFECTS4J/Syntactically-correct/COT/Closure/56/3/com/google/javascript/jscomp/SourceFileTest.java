import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import static org.junit.Assert.*;

public class SourceFileTest {
    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("testFile");
    }

    @Test
    public void testGetLineOffset() {
        assertEquals(0, sourceFile.getLineOffset(1));
    }

    @Test
    public void testGetNumLines() {
        assertEquals(34, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() {
        assertEquals("import com.google.common.base.Charsets;", sourceFile.getLine(8));
        assertNull(sourceFile.getLine(100));
    }

    @Test
    public void testGetRegion() {
        Region region = sourceFile.getRegion(8);
        assertEquals(8, region.getStartLine());
        assertEquals(12, region.getEndLine());
        assertEquals("import com.google.common.base.Charsets;", region.getCode());

        assertNull(sourceFile.getRegion(100));
    }

    @Test
    public void testToString() {
        assertEquals("testFile", sourceFile.toString());
    }

    @Test
    public void testFromFile() throws IOException {
        File file = new File("testFile.txt");
        Files.write("test code", file, Charset.defaultCharset());
        SourceFile sourceFile = SourceFile.fromFile(file);
        assertEquals("test code", sourceFile.getCode());
        assertEquals(file.getPath(), sourceFile.getOriginalPath());
        file.delete();
    }

    @Test
    public void testFromCode() throws IOException {
        SourceFile sourceFile = SourceFile.fromCode("testFile", "test code");
        assertEquals("test code", sourceFile.getCode());
    }

    @Test
    public void testFromInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test code".getBytes());
        SourceFile sourceFile = SourceFile.fromInputStream("testFile", inputStream);
        assertEquals("test code", sourceFile.getCode());
    }

    @Test
    public void testFromReader() throws IOException {
        StringReader reader = new StringReader("test code");
        SourceFile sourceFile = SourceFile.fromReader("testFile", reader);
        assertEquals("test code", sourceFile.getCode());
    }

    @Test
    public void testClearCachedSource() throws IOException {
        SourceFile sourceFile = SourceFile.fromCode("testFile", "test code");
        assertNotNull(sourceFile.getCode());
        sourceFile.clearCachedSource();
        assertNull(sourceFile.getCode());
    }
}