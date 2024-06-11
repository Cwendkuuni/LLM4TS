import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataUtilTest {
    private DataUtil dataUtil;

    @BeforeEach
    void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = dataUtil.load(file, "UTF-8", "");
        
        assertNotNull(doc);
    }

    @Test
    void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = dataUtil.load(in, "UTF-8", "");
        
        assertNotNull(doc);
    }

    @Test
    void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.xml");
        Document doc = dataUtil.load(in, "UTF-8", "", Parser.xmlParser());
        
        assertNotNull(doc);
    }

    @Test
    void testLoadWithNullFile() {
        assertThrows(IOException.class, () -> {
            dataUtil.load(null, "UTF-8", "");
        });
    }

    @Test
    void testLoadWithNullInputStream() {
        assertThrows(IOException.class, () -> {
            dataUtil.load(null, "UTF-8", "");
        });
    }

    @Test
    void testLoadWithNullParser() {
        assertThrows(IOException.class, () -> {
            InputStream in = new FileInputStream("test.xml");
            dataUtil.load(in, "UTF-8", "", null);
        });
    }

    @Test
    void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = System.out;

        dataUtil.crossStreams(in, out);
    }

    @Test
    void testParseInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = dataUtil.parseInputStream(in, "UTF-8", "", Parser.htmlParser());
        
        assertNotNull(doc);
    }

    @Test
    void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        ByteBuffer buffer = dataUtil.readToByteBuffer(in, 1024);
        
        assertNotNull(buffer);
    }

    @Test
    void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        ByteBuffer buffer = dataUtil.readToByteBuffer(in);
        
        assertNotNull(buffer);
    }

    @Test
    void testReadFileToByteBuffer() throws IOException {
        File file = new File("input.txt");
        ByteBuffer buffer = dataUtil.readFileToByteBuffer(file);
        
        assertNotNull(buffer);
    }

    @Test
    void testEmptyByteBuffer() {
        ByteBuffer buffer = dataUtil.emptyByteBuffer();
        
        assertEquals(0, buffer.capacity());
    }

    @Test
    void testGetCharsetFromContentType() {
        String charset = dataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        
        assertEquals("UTF-8", charset);
    }

    @Test
    void testGetCharsetFromContentTypeWithoutCharset() {
        String charset = dataUtil.getCharsetFromContentType("text/html");
        
        assertEquals(null, charset);
    }

    @Test
    void testGetCharsetFromContentTypeWithInvalidCharset() {
        String charset = dataUtil.getCharsetFromContentType("text/html; charset=InvalidCharset");
        
        assertEquals(null, charset);
    }

    @Test
    void testValidateCharset() {
        String validCharset = dataUtil.validateCharset("UTF-8");
        
        assertEquals("UTF-8", validCharset);
    }

    @Test
    void testValidateCharsetWithUnsupportedCharset() {
        String validCharset = dataUtil.validateCharset("InvalidCharset");
        
        assertEquals(null, validCharset);
    }

    @Test
    void testMimeBoundary() {
        String mimeBoundary = dataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
    }
}