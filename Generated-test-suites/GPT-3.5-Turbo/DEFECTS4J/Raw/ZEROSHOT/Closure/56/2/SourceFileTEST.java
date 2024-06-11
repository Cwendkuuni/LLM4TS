import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SourceFileTest {

    @Test
    public void testGetLine() throws IOException {
        SourceFile sourceFile = new SourceFile("testFile");
        String code = "Line 1\nLine 2\nLine 3";
        sourceFile.setCode(code);

        assertEquals("Line 1", sourceFile.getLine(1));
        assertEquals("Line 2", sourceFile.getLine(2));
        assertEquals("Line 3", sourceFile.getLine(3));

        assertNull(sourceFile.getLine(0));
        assertNull(sourceFile.getLine(4));
    }

    @Test
    public void testGetRegion() throws IOException {
        SourceFile sourceFile = new SourceFile("testFile");
        String code = "Line 1\nLine 2\nLine 3";
        sourceFile.setCode(code);

        assertNotNull(sourceFile.getRegion(1));
        assertNotNull(sourceFile.getRegion(2));
        assertNotNull(sourceFile.getRegion(3));

        assertNull(sourceFile.getRegion(0));
        assertNull(sourceFile.getRegion(4));
    }
}