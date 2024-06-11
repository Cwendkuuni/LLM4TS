import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        byte[] expectedOutput = "Hello, World!".getBytes();
        byte[] actualOutput = outputStream.toByteArray();
        
        assertEquals(expectedOutput.length, actualOutput.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], actualOutput[i]);
        }
    }

    @Test
    public void testParseByteData() {
        // Test case for when charsetName is null and charset is detected from meta tag
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("input.txt");
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        String charsetName = null;
        
        String result = DataUtil.detectCharsetFromBom(byteData, charsetName);
        
        assertEquals("UTF-8", result);
    }
}

