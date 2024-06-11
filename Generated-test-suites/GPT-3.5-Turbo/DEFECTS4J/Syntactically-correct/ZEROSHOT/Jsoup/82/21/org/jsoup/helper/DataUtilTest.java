import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    private String baseUri = "https://www.example.com";

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", baseUri);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", baseUri);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.xml");
        Document doc = DataUtil.load(in, "UTF-8", baseUri, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        byte[] expected = "Hello, World!".getBytes();
        byte[] actual = out.toByteArray();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(in, "UTF-8", baseUri, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(in, 1024);
        byte[] expected = "Hello, World!".getBytes();
        byte[] actual = buffer.array();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteArrayInputStream in = new ByteArrayInputStream(bom);
        ByteBuffer byteData = DataUtil.readToByteBuffer(in, 1024);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);
    }
}