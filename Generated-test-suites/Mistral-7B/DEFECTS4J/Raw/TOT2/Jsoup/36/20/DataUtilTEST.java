import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String fileContent = "test content";
        byte[] fileContentBytes = fileContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamDocumentWithParser() throws IOException {
        String fileContent = "test content";
        byte[] fileContentBytes = fileContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, "UTF-8", null, parser);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithNullCharset() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, null, null);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocumentWithNullCharset() throws IOException {
        String fileContent = "test content";
        byte[] fileContentBytes = fileContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        Document doc = DataUtil.load(inputStream, null, null);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocumentWithInvalidCharset() throws IOException {
        String fileContent = "test content";
        byte[] fileContentBytes = fileContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        Document doc = DataUtil.load(inputStream, "invalidCharset", null);
        fail("Expected IOException");
    }

    @Test
    public void testParseByteDataDocument() {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataDocumentWithDefaultCharset() {
        String fileContent = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataDocumentWithNullCharset() {
        String fileContent = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        fail("Expected IllegalArgumentException");
    }
}