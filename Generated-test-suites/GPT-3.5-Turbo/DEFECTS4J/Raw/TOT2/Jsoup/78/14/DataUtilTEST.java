import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        OutputStream outputStream = new FileOutputStream("output.txt");
        DataUtil.crossStreams(inputStream, outputStream);
        // Check if streams are successfully crossed
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        // Add more assertions as needed
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // Add more assertions as needed
    }

    // Add more test cases for other public methods in DataUtil class

}