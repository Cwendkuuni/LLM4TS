import org.junit.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import static org.junit.Assert.*;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setup() throws IOException {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("test1");
        lines.add("test2");
        lines.add("test3");
        fileText = FileText.fromLines(file, lines);
    }

    @After
    public void teardown() {
        fileText = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(fileText);
        assertEquals("test.txt", fileText.getFile().getName());
        assertNull(fileText.getCharset());
        assertEquals("test1\ntest2\ntest3\n", fileText.getFullText());
        assertArrayEquals(new String[]{"test1", "test2", "test3"}, fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }

    @Test
    public void testLineColumn() {
        LineColumn lc1 = fileText.lineColumn(0);
        assertEquals(1, lc1.getLine());
        assertEquals(0, lc1.getColumn());

        LineColumn lc2 = fileText.lineColumn(6);
        assertEquals(2, lc2.getLine());
        assertEquals(0, lc2.getColumn());

        LineColumn lc3 = fileText.lineColumn(12);
        assertEquals(3, lc3.getLine());
        assertEquals(0, lc3.getColumn());

        LineColumn lc4 = fileText.lineColumn(13);
        assertEquals(3, lc4.getLine());
        assertEquals(1, lc4.getColumn());
    }

    @Test
    public void testGet() {
        assertEquals("test1", fileText.get(0));
        assertEquals("test2", fileText.get(1));
        assertEquals("test3", fileText.get(2));
    }

    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }
}