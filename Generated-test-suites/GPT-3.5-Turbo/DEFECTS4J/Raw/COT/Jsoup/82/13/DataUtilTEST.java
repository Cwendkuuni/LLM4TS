import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "https://example.com");
        // TODO: Add assertions to validate the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "https://example.com");
        // TODO: Add assertions to validate the loaded document
    }

    @Test
    public void testLoadWithParser() throws IOException {
        File file = new File("test.xml");
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, "UTF-8", "https://example.com", Parser.xmlParser());
        // TODO: Add assertions to validate the loaded document
    }
  
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = getClass().getResourceAsStream("input.txt");
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        // TODO: Add assertions to validate the output stream
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.parseInputStream(in, "UTF-8", "https://example.com", Parser.htmlParser());
        // TODO: Add assertions to validate the parsed document
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 1024);
        // TODO: Add assertions to validate the byte buffer
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        // TODO: Add assertions to validate the mime boundary
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=utf-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        // TODO: Add assertions to validate the charset
    }
}