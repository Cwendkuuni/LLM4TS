import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    private static FileText fileText;

    @BeforeClass
    public static void setUp() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("This is line 1.");
        lines.add("This is line 2.");
        lines.add("This is line 3.");

        fileText = FileText.fromLines(file, lines);
    }

    @Test
    public void testGetFile() {
        File file = fileText.getFile();
        assertEquals("testFile.txt", file.getName());
    }

    @Test
    public void testGetCharset() {
        assertNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        assertEquals("This is line 1.\nThis is line 2.\nThis is line 3.\n", fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() {
        String[] linesArray = fileText.toLinesArray();
        assertEquals(3, linesArray.length);
        assertEquals("This is line 1.", linesArray[0]);
        assertEquals("This is line 2.", linesArray[1]);
        assertEquals("This is line 3.", linesArray[2]);
    }

    @Test
    public void testLineColumn() {
        LineColumn lc1 = fileText.lineColumn(8);
        assertEquals(1, lc1.getLine());
        assertEquals(8, lc1.getColumn());

        LineColumn lc2 = fileText.lineColumn(22);
        assertEquals(2, lc2.getLine());
        assertEquals(6, lc2.getColumn());

        LineColumn lc3 = fileText.lineColumn(40);
        assertEquals(3, lc3.getLine());
        assertEquals(5, lc3.getColumn());

        LineColumn lc4 = fileText.lineColumn(42);
        assertEquals(3, lc4.getLine());
        assertEquals(7, lc4.getColumn());

        LineColumn lc5 = fileText.lineColumn(50);
        assertEquals(3, lc5.getLine());
        assertEquals(15, lc5.getColumn());
    }

    @Test
    public void testGet() {
        assertEquals("This is line 1.", fileText.get(0));
        assertEquals("This is line 2.", fileText.get(1));
        assertEquals("This is line 3.", fileText.get(2));
    }

    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }
}