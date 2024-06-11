
import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileContentsTest {
    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = {"line 1", "line 2", "line 3"};
        fileContents = new FileContents("test.java", lines);
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 0);
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 2, 0, 2, 5);
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(1, 0);
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testGetCppComments() {
        fileContents.reportCppComment(1, 0);
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 0, 2, 5);
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetCComments() {
        fileContents.reportCComment(1, 0, 2, 5);
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(2, 0, 3, 5);
        assertNotNull(fileContents.getJavadocBefore(3));
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertNotNull(text);
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, text.toLinesArray());
    }

    @Test
    public void testGetLines() {
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        assertEquals("line 2", fileContents.getLine(1));
    }

    @Test
    public void testGetFileName() {
        assertEquals("test.java", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("test.java", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 5));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}
