import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "https://www.example.com");
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testLoadWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertEquals("test", out.toString());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, (int) file.length());
        assertEquals(file.length(), byteBuffer.remaining());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
    }
}