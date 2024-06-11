import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/file"); // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        // Add additional assertions for expected values in the document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file"); // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        // Add additional assertions for expected values in the document
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file"); // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        // Add additional assertions for expected values in the document
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Content".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String output = outputStream.toString();
        assertEquals("Test Content", output);
    }

    // Add additional test methods for other public static methods in DataUtil class

}