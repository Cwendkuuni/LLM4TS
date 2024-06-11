import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testGetName() {
        SourceFile sourceFile = new SourceFile("test");
        assertEquals("test", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile sourceFile = new SourceFile("test");
        assertFalse(sourceFile.isExtern());
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testGetLine() throws IOException {
        SourceFile sourceFile = new SourceFile("test");
        sourceFile.setCode("line1\nline2\nline3");

        assertEquals("line1", sourceFile.getLine(1));
        assertEquals("line2", sourceFile.getLine(2));
        assertEquals("line3", sourceFile.getLine(3));
        assertNull(sourceFile.getLine(4));
    }

    @Test
    public void testGetRegion() throws IOException {
        SourceFile sourceFile = new SourceFile("test");
        sourceFile.setCode("line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9");

        assertEquals("line1\nline2\nline3\nline4\nline5", sourceFile.getRegion(3).getText());
        assertEquals("line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9", sourceFile.getRegion(5).getText());
        assertNull(sourceFile.getRegion(10));
    }

    @Test
    public void testFromCode() throws IOException {
        SourceFile sourceFile = SourceFile.fromCode("test", "line1\nline2\nline3");
        assertEquals("test", sourceFile.getName());
        assertEquals("line1\nline2\nline3", sourceFile.getCode());
    }

    @Test
    public void testFromInputStream() throws IOException {
        SourceFile sourceFile = SourceFile.fromInputStream("test", "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9");
        assertEquals("test", sourceFile.getName());
        assertEquals("line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9", sourceFile.getCode());
    }

    @Test
    public void testFromReader() throws IOException {
        SourceFile sourceFile = SourceFile.fromReader("test", new FileReader("path/to/file.txt"));
        assertEquals("test", sourceFile.getName());
        assertEquals("line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9", sourceFile.getCode());
    }

    @Test
    public void testClearCachedSource() throws IOException {
        SourceFile sourceFile = SourceFile.fromCode("test", "line1\nline2\nline3");
        assertNotNull(sourceFile.getCode());
        sourceFile.clearCachedSource();
        assertNull(sourceFile.getCode());
    }
}