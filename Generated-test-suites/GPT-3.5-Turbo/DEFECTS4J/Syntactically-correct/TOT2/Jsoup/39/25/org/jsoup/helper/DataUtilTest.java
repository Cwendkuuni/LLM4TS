import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical use case
        // Test loading a file with valid input
        // Expected: No exceptions thrown
        try {
            Document document = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Edge case
        // Test loading a non-existent file
        // Expected: IOException is thrown
        assertThrows(IOException.class, () -> {
            Document document = DataUtil.load(new File("non_existent_file.html"), "UTF-8", "http://example.com");
        });

        // Error scenario
        // Test loading a null file
        // Expected: NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            Document document = DataUtil.load(null, "UTF-8", "http://example.com");
        });
    }

    @Test
    public void testLoadInputStream() {
        // Typical use case
        // Test loading an input stream with valid input
        // Expected: No exceptions thrown
        try (InputStream inputStream = new FileInputStream("test.html")) {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Edge case
        // Test loading an empty input stream
        // Expected: IOException is thrown
        assertThrows(IOException.class, () -> {
            ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
            Document document = DataUtil.load(emptyInputStream, "UTF-8", "http://example.com");
        });

        // Error scenario
        // Test loading a null input stream
        // Expected: NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            Document document = DataUtil.load(null, "UTF-8", "http://example.com");
        });
    }

    // Additional test cases for other methods should be added here

}