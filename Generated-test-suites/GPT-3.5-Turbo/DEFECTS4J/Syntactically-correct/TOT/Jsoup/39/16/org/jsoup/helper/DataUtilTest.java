import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad_withExistingFile() throws IOException {
        // Test with an existing file, typical use case
        File file = new File("test.html");
        String content = "<html><body><h1>Hello World!</h1></body></html>";
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        }

        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        assertEquals(content, doc.html());
    }

    @Test
    public void testLoad_withNonExistingFile() throws IOException {
        // Test with a non-existing file, error scenario
        File file = new File("non_existing_file.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNull(doc);
    }

    @Test
    public void testLoad_withInputStream() throws IOException {
        // Test with an input stream, typical use case
        String content = "<html><body><h1>Hello World!</h1></body></html>";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        Document doc = DataUtil.load(new ByteArrayInputStream(bytes), "UTF-8", "");
        assertNotNull(doc);
        assertEquals(content, doc.html());
    }

    @Test
    public void testLoad_withCharsetDetection() throws IOException {
        // Test with charset detection from meta tag
        String content = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=EUC-JP\"></head><body><h1>Hello World!</h1></body></html>";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        Document doc = DataUtil.load(new ByteArrayInputStream(bytes), null, "");
        assertNotNull(doc);
        assertEquals(content, doc.html());
    }

    @Test
    public void testLoad_withUnsupportedCharset() throws IOException {
        // Test with unsupported charset, error scenario
        String content = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=INVALID-CHARSET\"></head><body><h1>Hello World!</h1></body></html>";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        Document doc = DataUtil.load(new ByteArrayInputStream(bytes), null, "");
        assertNotNull(doc);
        assertNotEquals(content, doc.html());
    }

    @Test
    public void testLoad_withByteBuffer() throws IOException {
        // Test with ByteBuffer input, typical use case
        String content = "<html><body><h1>Hello World!</h1></body></html>";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(content, doc.html());
    }

    @Test
    public void testReadToByteBuffer_withMaxSize() throws IOException {
        // Test reading input stream into a byte buffer with a maximum size
        byte[] bytes = new byte[10];
        for (int i = 0; i < 10; i++) {
            bytes[i] = (byte) i;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        int maxSize = 5;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType() {
        // Test parsing charset from content type header with a valid content type
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType() {
        // Test parsing charset from content type header with an invalid content type
        String contentType = "text/html; charset=INVALID-CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_withNullContentType() {
        // Test parsing charset from null content type
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}