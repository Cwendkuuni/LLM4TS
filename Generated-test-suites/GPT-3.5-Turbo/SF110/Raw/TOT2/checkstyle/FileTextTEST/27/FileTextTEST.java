import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;

public class FileTextTest {

    private File file;
    private FileText fileText;

    @Before
    public void setUp() {
        String testContent = "Test line 1\nTest line 2\nTest line 3\n";
        List<String> lines = Arrays.asList(testContent.split("\n"));
        file = new File("test.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(testContent);
            writer.close();
            fileText = new FileText(file, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            fail("Error setting up test file");
        }
    }

    @Test
    public void testFileTextConstructor() {
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        assertTrue(fileText.getFullText().contains("Test line"));

        String[] expectedLines = {"Test line 1", "Test line 2", "Test line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFileTextCopyConstructor() {
        FileText copiedFileText = new FileText(fileText);
        assertNotNull(copiedFileText);
        assertEquals(fileText.getFile(), copiedFileText.getFile());
        assertEquals(fileText.getCharset(), copiedFileText.getCharset());
        assertEquals(fileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(fileText.toLinesArray(), copiedFileText.toLinesArray());
    }

    @Test(expected = IOException.class)
    public void testReadFileNonExistentFile() throws IOException {
        File nonExistentFile = new File("non_existent_file.txt");
        FileText.readFile(nonExistentFile, StandardCharsets.UTF_8.newDecoder());
    }

    @Test
    public void testLineColumn() {
        LineColumn lineColumn = fileText.lineColumn(15);
        assertEquals(2, lineColumn.getLine());
        assertEquals(5, lineColumn.getColumn());
    }

    @After
    public void tearDown() {
        file.delete();
    }
}