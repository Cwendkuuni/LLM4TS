import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class SourceFileTest {

    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("testFile.java");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidFileName() {
        SourceFile invalidSourceFile = new SourceFile("");
    }

    @Test
    public void testGetName() {
        assertEquals("testFile.java", sourceFile.getName());
    }

    @Test
    public void testIsExtern() {
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testSetIsExtern() {
        sourceFile.setIsExtern(true);
        assertTrue(sourceFile.isExtern());
    }

    @Test
    public void testGetLineOffset() throws IOException {
        String code = "Line1\nLine2\nLine3\nLine4";
        sourceFile.setCode(code);

        assertEquals(0, sourceFile.getLineOffset(1));
        assertEquals(6, sourceFile.getLineOffset(2));
        assertEquals(12, sourceFile.getLineOffset(3));
        assertEquals(18, sourceFile.getLineOffset(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffset_invalidLineNumber() throws IOException {
        String code = "Line1\nLine2\nLine3\nLine4";
        sourceFile.setCode(code);
        sourceFile.getLineOffset(0);
    }

    @Test
    public void testGetNumLines() throws IOException {
        String code = "Line1\nLine2\nLine3\nLine4";
        sourceFile.setCode(code);

        assertEquals(4, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() throws IOException {
        String code = "Line1\nLine2\nLine3\nLine4";
        sourceFile.setCode(code);

        assertEquals("Line1", sourceFile.getLine(1));
        assertEquals("Line2", sourceFile.getLine(2));
        assertEquals("Line3", sourceFile.getLine(3));
        assertEquals("Line4", sourceFile.getLine(4));
    }

    @Test
    public void testGetLine_nullResult() throws IOException {
        sourceFile.setCode("Line1");
        assertNull(sourceFile.getLine(2));
    }

    @Test
    public void testGetRegion() throws IOException {
        String code = "Line1\nLine2\nLine3\nLine4\nLine5";
        sourceFile.setCode(code);

        Region region1 = sourceFile.getRegion(1);
        assertEquals(1, region1.getStartLine());
        assertEquals(3, region1.getEndLine());
        assertEquals("Line1\nLine2\nLine3", region1.getContent());

        Region region2 = sourceFile.getRegion(4);
        assertEquals(2, region2.getStartLine());
        assertEquals(4, region2.getEndLine());
        assertEquals("Line2\nLine3\nLine4", region2.getContent());
    }

    @Test
    public void testGetRegion_nullResult() throws IOException {
        String code = "Line1\nLine2\nLine3\nLine4\nLine5";
        sourceFile.setCode(code);

        assertNull(sourceFile.getRegion(6));
    }
}