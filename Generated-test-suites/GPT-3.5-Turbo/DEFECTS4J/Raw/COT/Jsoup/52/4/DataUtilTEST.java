
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "");
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadWithParser() {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        String input = "Hello, world!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            String output = outputStream.toString(StandardCharsets.UTF_8.name());
            assertEquals(input, output);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        String input = "Hello, world!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            String output = StandardCharsets.UTF_8.decode(byteBuffer).toString();
            assertEquals(input, output);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.txt");
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom_UTF16LE() {
        byte[] bom = {(byte) 0xFF, (byte) 0xFE};
        String charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bom), null);
        assertEquals("UTF-16", charsetName);
    }

    @Test
    public void testDetectCharsetFromBom_UTF16BE() {
        byte[] bom = {(byte) 0xFE, (byte) 0xFF};
        String charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bom), null);
        assertEquals("UTF-16", charsetName);
    }

    @Test
    public void testDetectCharsetFromBom_UTF32LE() {
        byte[] bom = {(byte) 0xFF, (byte) 0xFE, 0x00, 0x00};
        String charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bom), null);
        assertEquals("UTF-32", charsetName);
    }

    @Test
    public void testDetectCharsetFromBom_UTF32BE() {
        byte[] bom = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        String charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bom), null);
        assertEquals("UTF-32", charsetName);
    }

    @Test
    public void testDetectCharsetFromBom_UTF8() {
        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        String charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(bom), null);
        assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testDetectCharsetFromBom_NoBOM() {
        byte[] data = "Hello, world!".getBytes(StandardCharsets.UTF_8);
        String charsetName = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(data), "UTF-8");
        assertEquals("UTF-8", charsetName);
    }
}
