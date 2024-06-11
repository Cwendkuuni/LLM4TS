import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to load file: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://www.example.com";
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to load input stream: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://www.example.com";
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to load input stream with parser: " + e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("Failed to read to byte buffer: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}