import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test_data.txt");
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://www.example.com";
        
        Document doc = DataUtil.load(testFile, expectedCharset, expectedBaseUri);
        
        assertNotNull(doc);
        assertEquals(expectedBaseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://www.example.com";

        Document doc = DataUtil.load(inputStream, expectedCharset, expectedBaseUri);

        assertNotNull(doc);
        assertEquals(expectedBaseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://www.example.com";

        Document doc = DataUtil.load(inputStream, expectedCharset, expectedBaseUri, Parser.htmlParser());

        assertNotNull(doc);
        assertEquals(expectedBaseUri, doc.baseUri());
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String expectedBaseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, null, expectedBaseUri, parser);

        assertNotNull(doc);
        assertEquals(expectedBaseUri, doc.baseUri());
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, expectedCharset, expectedBaseUri, parser);

        assertNotNull(doc);
        assertEquals(expectedCharset, doc.charset().name());
        assertEquals(expectedBaseUri, doc.baseUri());
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws IOException {
        byte[] testData = new byte[100];
        Arrays.fill(testData, (byte) 'a');
        InputStream inputStream = new ByteArrayInputStream(testData);
        int maxSize = 0;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertEquals(testData.length, byteData.capacity());
    }

    @Test
    public void testReadToByteBufferWithCappedSize() throws IOException {
        byte[] testData = new byte[100];
        Arrays.fill(testData, (byte) 'a');
        InputStream inputStream = new ByteArrayInputStream(testData);
        int maxSize = 50;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertEquals(maxSize, byteData.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID-CHARSET";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

}