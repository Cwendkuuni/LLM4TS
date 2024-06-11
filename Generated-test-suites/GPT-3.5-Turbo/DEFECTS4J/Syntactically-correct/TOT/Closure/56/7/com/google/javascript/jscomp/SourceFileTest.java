import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SourceFileTest {

    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("TestFile");
    }

    @Test
    public void testConstructorWithValidFileName() {
        Assert.assertEquals("TestFile", sourceFile.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyFileName() {
        sourceFile = new SourceFile("");
    }

    @Test
    public void testGetLineOffsetWithValidLineNumber() {
        int lineNumber = 1;
        int lineOffset = sourceFile.getLineOffset(lineNumber);
        Assert.assertEquals(0, lineOffset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineOffsetWithInvalidLineNumber() {
        int lineNumber = -1;
        sourceFile.getLineOffset(lineNumber);
    }

    @Test
    public void testGetNumLinesBeforeSettingCode() {
        int numLines = sourceFile.getNumLines();
        Assert.assertEquals(1, numLines);
    }

    @Test
    public void testGetNumLinesAfterSettingCode() throws IOException {
        sourceFile.setCode("Line 1\nLine 2\nLine 3");
        int numLines = sourceFile.getNumLines();
        Assert.assertEquals(3, numLines);
    }

    @Test
    public void testGetLineWithValidLineNumber() throws IOException {
        sourceFile.setCode("Line 1\nLine 2\nLine 3");
        int lineNumber = 2;
        String line = sourceFile.getLine(lineNumber);
        Assert.assertEquals("Line 2", line);
    }

    @Test
    public void testGetLineWithInvalidLineNumber() throws IOException {
        sourceFile.setCode("Line 1\nLine 2\nLine 3");
        int lineNumber = 4;
        String line = sourceFile.getLine(lineNumber);
        Assert.assertNull(line);
    }

    @Test
    public void testGetRegionWithValidLineNumber() throws IOException {
        sourceFile.setCode("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6");
        int lineNumber = 4;
        SourceFile.Region region = sourceFile.getRegion(lineNumber);
        Assert.assertEquals("Line 2\nLine 3\nLine 4\nLine 5\nLine 6", region.getContents());
        Assert.assertEquals(2, region.getStartLine());
        Assert.assertEquals(6, region.getEndLine());
    }

    @Test
    public void testGetRegionWithInvalidLineNumber() throws IOException {
        sourceFile.setCode("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6");
        int lineNumber = 7;
        SourceFile.Region region = sourceFile.getRegion(lineNumber);
        Assert.assertNull(region);
    }

    @Test
    public void testToString() {
        String toString = sourceFile.toString();
        Assert.assertEquals("TestFile", toString);
    }
}