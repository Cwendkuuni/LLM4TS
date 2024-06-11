import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File file = new File("non-existent-file.html"); // replace with non-existent file path
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "Test data...";
        ByteArrayInputStream in = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        String testData = "Test data...";
        ByteArrayInputStream in = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        DataUtil.load(in, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String testData = "Test data...";
        ByteArrayInputStream in = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        Document doc = DataUtil.load(in, charsetName, baseUri, DataUtil.Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullCharsetName() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        Document doc = DataUtil.parseByteData(byteData, null, "", DataUtil.Parser.htmlParser());
    }

    @Test
    public void testParseByteDataWithValidCharsetName() {
        String testData = "Test data...";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
    }
}