import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadCustomParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNull(doc); // As null because charsetName is null in method
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferUnlimited() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
    }
}