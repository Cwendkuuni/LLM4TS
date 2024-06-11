import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertNotNull(out);
    }

    @Test
    public void testParseByteData() {
        // Test cases for parsing byte data with different scenarios
        // Edge cases and error scenarios can be added here
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("test.txt");
        int maxSize = 100;
        DataUtil.readToByteBuffer(inStream, maxSize);
        // Add assertions
    }

    @Test
    public void testReadToByteBufferUnlimited() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("test.txt");
        DataUtil.readToByteBuffer(inStream);
        // Add assertions
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        DataUtil.readFileToByteBuffer(file);
        // Add assertions
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Test cases for detecting charset from Byte Order Marker
        // Edge cases and error scenarios can be added here
    }
}