import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad_withValidFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoad_withInvalidFile() {
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            fail("IOException was not thrown");
        } catch (IOException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testLoad_withNullCharset() {
        File file = new File("test.html");
        String charsetName = null;
        String baseUri = "http://www.example.com";

        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoad_withInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoad_withInputStreamAndParser() {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseByteData_withNullCharset() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(document.charset().displayName().toLowerCase(Locale.ENGLISH), "utf-8");
    }

    @Test
    public void testParseByteData_withCharsetMetaTag() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        // Adding meta charset tag to byte buffer
        byteBuffer.put("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=ISO-8859-1\">".getBytes());

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(document.charset().displayName().toLowerCase(Locale.ENGLISH), "iso-8859-1");
    }

    @Test
    public void testParseByteData_withCharsetAttribute() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        // Adding charset attribute to byte buffer
        byteBuffer.put("<meta charset=\"ISO-8859-1\">".getBytes());

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(document.charset().displayName().toLowerCase(Locale.ENGLISH), "iso-8859-1");
    }

    @Test
    public void testGetCharsetFromContentType_withValidCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals(charset.toLowerCase(Locale.ENGLISH), "utf-8");
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidCharset() {
        String contentType = "text/html; charset=Invalid-Charset";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testReadToByteBuffer_withValidInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.limit() <= maxSize);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadToByteBuffer_withInvalidInputStream() {
        InputStream inputStream = null;
        int maxSize = 1024;

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            fail("IOException was not thrown");
        } catch (IOException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testReadToByteBuffer_withUnlimitedSize() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 0;

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadToByteBuffer_withNegativeSize() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = -1;

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            fail("IllegalArgumentException was not thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testReadToByteBuffer_withInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

}