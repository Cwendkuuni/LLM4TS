import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com/";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com/";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(html, doc.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<root><child>Hello, world!</child></root>";
        InputStream in = new ByteArrayInputStream(xml.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com/";
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(xml, doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello, world!";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertEquals(input, out.toString());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello, world!</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com/";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("<html><head></head><body><h1>Hello, world!</h1></body></html>", doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "Hello, world!";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
        assertNotNull(byteBuffer);
        assertEquals(input, new String(byteBuffer.array()));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertNotNull(validatedCharset);
        assertEquals("UTF-8", validatedCharset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertTrue(mimeBoundary.length() > 0);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'H', 'e', 'l', 'l', 'o'});
        String charsetName = "UTF-8";
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertNotNull(detectedCharset);
        assertEquals("UTF-8", detectedCharset);
    }

}