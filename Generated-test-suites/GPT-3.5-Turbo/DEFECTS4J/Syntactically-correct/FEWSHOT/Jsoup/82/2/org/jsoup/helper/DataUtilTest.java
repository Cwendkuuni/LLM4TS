import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("input data".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertArrayEquals("input data".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        int maxSize = 1024;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(byteBuffer);
        // Add more assertions here
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        // Add more assertions here
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }
    
    // Add more test methods for the remaining methods in the DataUtil class

}