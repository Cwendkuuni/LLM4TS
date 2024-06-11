import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private static final String BASE_URI = "http://www.example.com";

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String expectedBaseUri = "http://www.example.com";

        try {
            Document document = DataUtil.load(file, charsetName, BASE_URI);

            assertNotNull(document);
            assertEquals(expectedBaseUri, document.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String expectedBaseUri = "http://www.example.com";

        try {
            Document document = DataUtil.load(inputStream, charsetName, BASE_URI);

            assertNotNull(document);
            assertEquals(expectedBaseUri, document.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String expectedBaseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        try {
            Document document = DataUtil.load(inputStream, charsetName, BASE_URI, parser);

            assertNotNull(document);
            assertEquals(expectedBaseUri, document.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        Charset defaultCharset = Charset.forName("UTF-8");
        String baseUri = "http://www.example.com";
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String expectedCharset = "UTF-8";

        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

        assertNotNull(document);
        assertEquals(expectedCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteDataWithCharset() {
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "http://www.example.com";
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String expectedCharset = "UTF-8";

        Document document = DataUtil.parseByteData(byteData, charset.name(), baseUri, Parser.htmlParser());

        assertNotNull(document);
        assertEquals(expectedCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        int maxSize = 10;
        int expectedSize = 10;

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

            assertNotNull(byteBuffer);
            assertEquals(expectedSize, byteBuffer.limit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        int expectedSize = 30;

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

            assertNotNull(byteBuffer);
            assertEquals(expectedSize, byteBuffer.limit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithMissingCharset() {
        String contentType = "text/html";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }
}