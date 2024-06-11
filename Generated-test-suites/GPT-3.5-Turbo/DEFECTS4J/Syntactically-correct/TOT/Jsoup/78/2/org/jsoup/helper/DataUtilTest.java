import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    private String baseUri;
    private File file;
    private InputStream inputStream;
    private String charsetName;

    @Before
    public void setUp() {
        baseUri = "http://example.com";
        file = new File("test.html");
        inputStream = new ByteArrayInputStream("<html><body><h1>Hello World!</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        charsetName = "UTF-8";
    }

    @Test
    public void load_file_ValidFile_ReturnsDocument() throws IOException {
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void load_file_InvalidFile_ThrowsIOException() {
        file = new File("nonexistent.html");
        assertThrows(IOException.class, () -> {
            DataUtil.load(file, charsetName, baseUri);
        });
    }

    @Test
    public void load_inputStream_ValidInputStream_ReturnsDocument() throws IOException {
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void load_inputStream_InvalidInputStream_ThrowsIOException() {
        inputStream = new ByteArrayInputStream("invalid html".getBytes(StandardCharsets.UTF_8));
        assertThrows(IOException.class, () -> {
            DataUtil.load(inputStream, charsetName, baseUri);
        });
    }

    @Test
    public void load_inputStreamWithParser_ValidInputStream_ReturnsDocument() throws IOException {
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void load_inputStreamWithParser_InvalidInputStream_ThrowsIOException() {
        inputStream = new ByteArrayInputStream("invalid html".getBytes(StandardCharsets.UTF_8));
        assertThrows(IOException.class, () -> {
            DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        });
    }

    @Test
    public void crossStreams_ValidInputStreamAndOutputStream_NoExceptionThrown() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        OutputStream out = System.out;
        assertDoesNotThrow(() -> {
            DataUtil.crossStreams(in, out);
        });
    }

    @Test
    public void parseInputStream_ValidInputStreamAndCharset_ReturnsDocument() throws IOException {
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void parseInputStream_InvalidInputStreamAndCharset_ReturnsDocument() {
        InputStream invalidInputStream = new ByteArrayInputStream("invalid html".getBytes(StandardCharsets.UTF_8));
        assertThrows(IOException.class, () -> {
            DataUtil.parseInputStream(invalidInputStream, charsetName, baseUri, Parser.htmlParser());
        });
    }

    @Test
    public void readToByteBuffer_ValidInputStreamAndMaxSize_ReturnsByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void readToByteBuffer_InvalidInputStreamAndMaxSize_ReturnsByteBuffer() {
        InputStream invalidInputStream = new ByteArrayInputStream("invalid html".getBytes(StandardCharsets.UTF_8));
        assertThrows(IOException.class, () -> {
            DataUtil.readToByteBuffer(invalidInputStream, 1024);
        });
    }

    @Test
    public void readToByteBuffer_ValidInputStream_ReturnsByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void readFileToByteBuffer_ValidFile_ReturnsByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void emptyByteBuffer_ReturnsEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void getCharsetFromContentType_ValidContentType_ReturnsCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void getCharsetFromContentType_InvalidContentType_ReturnsNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void mimeBoundary_ReturnsValidMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void detectCharsetFromBom_ValidByteBuffer_ReturnsBomCharset() {
        byte[] bomUtf8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bomUtf8);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(bomUtf8.length, bomCharset.offset);
    }

    @Test
    public void detectCharsetFromBom_InvalidByteBuffer_ReturnsNull() {
        byte[] invalidBom = {0x01, 0x02, 0x03, 0x04};
        ByteBuffer byteBuffer = ByteBuffer.wrap(invalidBom);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }

}