import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class SourceFileTest {

    @Test
    public void testConstructor() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertEquals("TestFile", sourceFile.getName());
        assertFalse(sourceFile.isExtern());
    }

    @Test
    public void testGetLineOffset() {
        SourceFile sourceFile = new SourceFile("TestFile");
        int lineOffset = sourceFile.getLineOffset(1);
        assertEquals(0, lineOffset);
    }

    @Test
    public void testGetLineOffsetWithInvalidLine() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertThrows(IllegalArgumentException.class, () -> {
            sourceFile.getLineOffset(0);
        });
    }

    @Test
    public void testGetNumLines() {
        SourceFile sourceFile = new SourceFile("TestFile");
        int numLines = sourceFile.getNumLines();
        // Add assertion based on expected number of lines
    }

    @Test
    public void testGetCode() {
        SourceFile sourceFile = new SourceFile("TestFile");
        assertThrows(IOException.class, () -> {
            sourceFile.getCode();
        });
        // Add I/O exception handling test
    }

    @Test
    public void testGetLine() {
        SourceFile sourceFile = new SourceFile("TestFile");
        String line = sourceFile.getLine(1);
        // Add assertion based on expected line content
    }

    @Test
    public void testGetLineWithInvalidLine() {
        SourceFile sourceFile = new SourceFile("TestFile");
        String line = sourceFile.getLine(0);
        assertNull(line);
    }

    @Test
    public void testGetRegion() {
        SourceFile sourceFile = new SourceFile("TestFile");
        Region region = sourceFile.getRegion(1);
        // Add assertion based on expected region content
    }

    @Test
    public void testGetRegionWithInvalidLine() {
        SourceFile sourceFile = new SourceFile("TestFile");
        Region region = sourceFile.getRegion(0);
        assertNull(region);
    }

    // Add more test cases for other methods and edge cases

}