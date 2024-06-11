
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<String>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        fileText = FileText.fromLines(file, lines);
    }

    @Test
    public void testFileText() {
        assertNotNull(fileText);
        assertEquals("test.txt", fileText.getFile().getName());
    }

    @Test
    public void testGetCharset() {
        assertNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        assertNotNull(fileText.getFullText());
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() {
        assertNotNull(fileText.toLinesArray());
        assertEquals(3, fileText.toLinesArray().length);
    }

    @Test
    public void testLineColumn() {
        assertNull(fileText.lineColumn(-1));
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 5), fileText.lineColumn(5));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(6));
        assertEquals(new LineColumn(2, 5), fileText.lineColumn(11));
        assertEquals(new LineColumn(3, 0), fileText.lineColumn(12));
        assertEquals(new LineColumn(3, 5), fileText.lineColumn(17));
        assertNull(fileText.lineColumn(18));
    }

    @Test
    public void testGet() {
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }
}
