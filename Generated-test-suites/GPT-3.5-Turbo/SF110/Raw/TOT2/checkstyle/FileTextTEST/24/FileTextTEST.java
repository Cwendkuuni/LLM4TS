import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertTrue(fileText.size() > 0);
        assertEquals(fileText.get(0), fileText.toLinesArray()[0]);

        LineColumn lineColumn = fileText.lineColumn(5);
        assertNotNull(lineColumn);
    }

    @Test
    public void testFileTextFromLines() {
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");

        File file = new File("test.txt");
        FileText fileText = FileText.fromLines(file, lines);

        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(2, fileText.size());
        assertEquals("Line 1", fileText.get(0));
    }

    @Test(expected = IOException.class)
    public void testFileTextInvalidCharset() throws IOException {
        File file = new File("test.txt");
        new FileText(file, "INVALID_CHARSET");
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextInvalidFile() throws IOException {
        File file = new File("invalid_file.txt");
        new FileText(file, "UTF-8");
    }

    // Add more test cases for edge cases and exception handling scenarios

}