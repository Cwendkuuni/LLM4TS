
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        try {
            Document document = DataUtil.load(new File("test.html"), "UTF-8", "");
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap("<html><body><h1>Hello, world!</h1></body></html>".getBytes());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteBuffer.array());
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap("<html><body><h1>Hello, world!</h1></body></html>".getBytes());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteBuffer.array());
            Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCrossStreams() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap("Hello, world!".getBytes());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteBuffer.array());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals("Hello, world!", outputStream.toString(StandardCharsets.UTF_8.name()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseByteData() {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream("Hello, world!".getBytes()));
            assertEquals("Hello, world!", Charset.defaultCharset().decode(byteBuffer).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadFileToByteBuffer() {
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File("test.txt"));
            assertEquals("Hello, world!", Charset.defaultCharset().decode(byteBuffer).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'H', 'e', 'l', 'l', 'o'});
        String charsetName = DataUtil.detectCharsetFromBom(byteBuffer, "UTF-8");
        assertEquals("UTF-8", charsetName);
    }
    
}
