import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
           
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");

            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.xml");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());

            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/input.txt");
            OutputStream outputStream = new FileOutputStream("output.txt");

            DataUtil.crossStreams(inputStream, outputStream);

            File outputFile = new File("output.txt");
            assertTrue(outputFile.exists());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/input.txt");
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
            InputStream inputStream = getClass().getResourceAsStream("/input.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("input.txt");
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

            assertNotNull(byteBuffer);
            assertEquals(file.length(), byteBuffer.capacity());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(emptyBuffer);
        assertEquals(0, emptyBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream inputStream = getClass.getResourceAsStream("/test.html");
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());

            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);

        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF });
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
    }
}