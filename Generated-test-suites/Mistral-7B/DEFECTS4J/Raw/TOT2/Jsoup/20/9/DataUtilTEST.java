import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("Test Data".getBytes());
        String charsetName = "UTF-8";
        Document doc = DataUtil.parseByteData(byteBuffer, charsetName, "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFileDoesNotExist() throws IOException {
        File file = new File("NonExistentFile.html");
        DataUtil.load(file, "UTF-8", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharsetName() {
        DataUtil.load(new File("test.html"), "", "");
        DataUtil.parseByteData(ByteBuffer.wrap("Test Data".getBytes()), "AaBb", "", Parser.htmlParser());
    }

    @Test(expected = NullPointerException.class)
    public void testLoadInputStreamIsNull() throws IOException {
        DataUtil.load(null, "UTF-8", "");
    }

    @Test
    public void testLoadInvalidBaseUri() {
        Document doc = DataUtil.load(new File("test.html"), "UTF-8", "InvalidBaseUri");
        assertNotNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=INVALID_CHARSET";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}