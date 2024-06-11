import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("Hello\nWorld\n");
        
        assertEquals(0, sourceFile.getLineOffset(1));
        assertEquals(6, sourceFile.getLineOffset(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffsetInvalidLine() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("Hello\nWorld\n");
        
        sourceFile.getLineOffset(0);
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("Hello\nWorld\n");
        
        assertEquals(2, sourceFile.getNumLines());
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("Hello\nWorld\n");
        
        assertEquals("Hello", sourceFile.getLine(1));
        assertEquals("World", sourceFile.getLine(2));
    }

    @Test
    public void testGetLineNotExists() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("Hello\nWorld\n");
        
        assertNull(sourceFile.getLine(3));
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("1\n2\n3\n4\n5\n");
        
        Region region = sourceFile.getRegion(3);
        assertNotNull(region);
        assertEquals("1\n2\n3\n4\n5", region.getContent());
        assertEquals(1, region.getStartLine());
        assertEquals(5, region.getEndLine());
    }

    @Test
    public void testGetRegionNotExists() {
        SourceFile sourceFile = new SourceFile("Test.java");
        sourceFile.setCode("1\n2\n3\n4\n5\n");
        
        assertNull(sourceFile.getRegion(6));
    }

    @Test
    public void testToString() {
        SourceFile sourceFile = new SourceFile("Test.java");
        assertEquals("Test.java", sourceFile.toString());
    }
}