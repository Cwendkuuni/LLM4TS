import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoad_fileWithCharset_success() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoad_fileWithoutCharset_success() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoad_fileNotFound_throwIOException() throws IOException {
        File file = new File("non_existent_file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            DataUtil.load(file, charsetName, baseUri);
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("No such file or directory"));
        }
    }

    @Test
    public void testLoad_inputStreamWithCharset_success() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoad_inputStreamWithoutCharset_success() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = null;
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoad_inputStreamCharsetNotFound_throwIOException() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "non_existent_charset";
        String baseUri = "http://example.com";

        try {
            DataUtil.load(inputStream, charsetName, baseUri);
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unsupported charset: non_existent_charset"));
        }
    }

    @Test
    public void testLoad_inputStreamNotFound_throwIOException() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("non_existent_file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            DataUtil.load(inputStream, charsetName, baseUri);
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Stream closed"));
        }
    }

    @Test
    public void testLoad_inputStreamWithParser_success() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());

        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseByteData_charsetFromMeta_success() {
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(ByteBuffer.wrap("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head></html>".getBytes()), charsetName, baseUri, parser);

        assertEquals("http://example.com", document.baseUri());
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData_charsetNotFound_throwIOException() {
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        try {
            DataUtil.parseByteData(ByteBuffer.wrap("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=non_existent_charset\"></head></html>".getBytes()), charsetName, baseUri, parser);
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unsupported charset: non_existent_charset"));
        }
    }

    @Test
    public void testReadToByteBuffer_inputStreamWithMaxSize_success() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1000;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_inputStreamWithoutMaxSize_success() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals(830, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType_validContentType_success() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_invalidCharset_returnNull() {
        String contentType = "text/html; charset=non_existent_charset";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(null, charset);
    }

}