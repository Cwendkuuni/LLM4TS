import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {
    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoad_withFileAndCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_withInputStreamAndCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_withInputStreamAndCharsetNameAndBaseUriAndParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteData_withByteBufferAndCharsetNameAndBaseUriAndParser() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer_withInputStreamAndMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        int maxSize = 1024;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_withInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType_withContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidCharset() {
        String contentType = "text/html; charset=INVALID";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_withUnsupportedCharset() {
        String contentType = "text/html; charset=unsupported";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}