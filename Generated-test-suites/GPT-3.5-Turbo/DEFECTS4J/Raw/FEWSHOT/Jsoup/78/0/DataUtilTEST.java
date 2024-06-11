import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {

    private File testFile;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setup() {
        testFile = new File("test.html");
        charsetName = "UTF-8";
        baseUri = "http://example.com/";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() {
        try {
            Document document = DataUtil.load(testFile, charsetName, baseUri);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            OutputStream outputStream = System.out;
            DataUtil.crossStreams(inputStream, outputStream);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull(byteBuffer);
            assertEquals(1024, byteBuffer.capacity());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.capacity() > 0);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadToByteBufferFromFile() {
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.capacity() > 0);
        } catch (IOException e) {
            fail("IOException thrown");
        }
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
    public void testGetCharsetFromContentTypeWithNull() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeType() {
        String mime = DataUtil.mimeBoundary();
        assertNotNull(mime);
        assertEquals(32, mime.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);
        byteBuffer.rewind();
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);
    }
}