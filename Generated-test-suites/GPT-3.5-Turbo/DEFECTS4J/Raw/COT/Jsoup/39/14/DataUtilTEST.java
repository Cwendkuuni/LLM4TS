
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        // Add assertions here
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><body>Hello World</body></html>".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        // Add assertions here
    }

    @Test
    public void testLoadFromParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><body>Hello World</body></html>".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        // Add assertions here
    }

    @Test
    public void testParseByteData() {
        // Add test cases for different scenarios
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(10, byteBuffer.limit());
        // Add assertions here
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        // Add assertions here
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // Add assertions here
    }
}
