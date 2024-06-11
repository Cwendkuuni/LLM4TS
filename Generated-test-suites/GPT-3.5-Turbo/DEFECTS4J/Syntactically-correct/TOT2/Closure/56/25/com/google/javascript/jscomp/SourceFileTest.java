import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testGetLineOffset() {
        SourceFile file = new SourceFile("testFile");
        assertEquals(0, file.getLineOffset(1));
        assertEquals(0, file.getLineOffset(100)); // Testing out of bound line number
    }

    @Test
    public void testGetNumLines() {
        SourceFile file = new SourceFile("testFile");
        assertEquals(1, file.getNumLines()); // File with no content
    }

    @Test
    public void testGetLine() throws IOException {
        SourceFile file = new SourceFile("testFile");
        assertNull(file.getLine(1)); // No content in file
    }

    @Test
    public void testGetRegion() throws IOException {
        SourceFile file = new SourceFile("testFile");
        assertNull(file.getRegion(1)); // No content in file
    }

    @Test
    public void testGetCode() throws IOException {
        SourceFile file = new SourceFile("testFile");
        assertNull(file.getCode()); // No content in file
    }

    @Test
    public void testGetCodeReader() throws IOException {
        SourceFile file = new SourceFile("testFile");
        assertNotNull(file.getCodeReader()); // No content in file but reader should not be null
    }

    @Test
    public void testGetName() {
        SourceFile file = new SourceFile("testFile");
        assertEquals("testFile", file.getName());
    }

    @Test
    public void testIsExtern() {
        SourceFile file = new SourceFile("testFile");
        assertFalse(file.isExtern());
    }

    @Test
    public void testGetOriginalPath() {
        SourceFile file = new SourceFile("testFile");
        assertEquals("testFile", file.getOriginalPath());
    }

    @Test
    public void testSetOriginalPath() {
        SourceFile file = new SourceFile("testFile");
        file.setOriginalPath("original/testFile");
        assertEquals("original/testFile", file.getOriginalPath());
    }

}