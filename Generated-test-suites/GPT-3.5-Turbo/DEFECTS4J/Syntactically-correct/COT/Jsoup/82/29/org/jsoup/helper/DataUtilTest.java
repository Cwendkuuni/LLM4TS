import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        String filePath = "test-file.html";
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";

        Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);

        assertNotNull(doc);
        // Add more assertions to test the functionality of the method
    }

    @Test
    public void testLoadInputStream() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("test-file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(doc);
        // Add more assertions to test the functionality of the method
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("test-file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(doc);
        // Add more assertions to test the functionality of the method
    }

    @Test
    public void testCrossStreams() throws Exception {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes("UTF-8"));
        OutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(in, out);

        String result = out.toString("UTF-8");
        assertEquals("Hello, World!", result);
        // Add more assertions to test the functionality of the method
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes("UTF-8"));
        int maxSize = 100;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);

        assertNotNull(byteBuffer);
        // Add more assertions to test the functionality of the method
    }

    // Add more test cases to cover all methods in the DataUtil class

}