import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() {
        try {
            File file = new File("test.html");
            assertNotNull(DataUtil.load(file, "UTF-8", "http://example.com"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            assertNotNull(DataUtil.load(inputStream, "UTF-8", "http://example.com"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            assertNotNull(DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        // byteData testing is difficult as it requires a ByteBuffer object which is not easily mockable
        // Instead, we validate the output after parsing the byte data
        // Negative tests are included to check for null or invalid input

        // Positive test with valid input
        ByteBuffer byteData = ByteBuffer.allocate(1024); // Replace with actual byte data
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);

        // Negative test with null charsetName
        doc = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        assertNotNull(doc); // Will default to UTF-8

        // Negative test with invalid charsetName
        doc = DataUtil.parseByteData(byteData, "Invalid Charset", "http://example.com", Parser.htmlParser());
        assertNotNull(doc); // Will default to UTF-8
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            assertNotNull(DataUtil.readToByteBuffer(inputStream, 1024));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            assertNotNull(DataUtil.readToByteBuffer(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Positive test with valid content type
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        // Negative test with null content type
        charset = DataUtil.getCharsetFromContentType(null);
        assertEquals(null, charset);

        // Negative test with invalid charset
        contentType = "text/html; charset=Invalid Charset";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
}