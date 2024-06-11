import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.*;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileTextTest {

    private FileText fileText;

    @BeforeEach
    public void setUp() throws IOException {
        File file = new File("test.txt");
        fileText = new FileText(file, "UTF-8");
    }

    @Test
    public void testGetFile() {
        assertEquals("test.txt", fileText.getFile().getName());
    }

    @Test
    public void testGetCharset() {
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        assertNotNull(fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        String[] lines = fileText.toLinesArray();
        assertNotNull(lines);
        assertEquals(1, lines.length);
        assertEquals("", lines[0]);
    }

    @Test
    public void testLineColumn() {
        LineColumn lineColumn = fileText.lineColumn(5);
        assertEquals(1, lineColumn.getLineNo());
        assertEquals(5, lineColumn.getColumnNo());
    }

    @Test
    public void testGet() {
        assertEquals("", fileText.get(0));
    }

    @Test
    public void testSize() {
        assertEquals(1, fileText.size());
    }
}