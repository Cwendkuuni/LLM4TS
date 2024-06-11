import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    private final String baseUri = "http://example.com";

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.xml");
        Document doc = DataUtil.load(inputStream, null, baseUri, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Create input/output streams for testing
        InputStream inputStream = new FileInputStream("input.txt");
        OutputStream outputStream = new FileOutputStream("output.txt");

        // Test crossStreams method
        DataUtil.crossStreams(inputStream, outputStream);

        // Close streams
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        inputStream.close();
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'h', 'e', 'l', 'l', 'o'});
        BomCharset bomCharset1 = DataUtil.detectCharsetFromBom(byteBuffer1);
        assertNotNull(bomCharset1);
        assertEquals("UTF-8", bomCharset1.charset);
        assertEquals(true, bomCharset1.offset);

        ByteBuffer byteBuffer2 = ByteBuffer.wrap(new byte[]{'h', 'e', 'l', 'l', 'o'});
        BomCharset bomCharset2 = DataUtil.detectCharsetFromBom(byteBuffer2);
        assertNull(bomCharset2);
    }
}