import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";

            Document document = DataUtil.load(file, charsetName, baseUri);

            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";

            Document document = DataUtil.load(inputStream, charsetName, baseUri);

            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Parser parser = Parser.htmlParser();

            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            OutputStream outputStream = new OutputStreamStub();

            DataUtil.crossStreams(inputStream, outputStream);

            // Check if the outputStream received the data from the inputStream
            assertTrue(((OutputStreamStub) outputStream).isDataReceived());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Parser parser = Parser.htmlParser();

            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            int maxSize = 1024;

            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.remaining() <= maxSize);
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
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

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetInvalid() {
        String charset = "InvalidCharset";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    class OutputStreamStub extends OutputStream {
        private boolean dataReceived = false;

        public void write(int b) throws IOException {
            dataReceived = true;
        }

        public boolean isDataReceived() {
            return dataReceived;
        }
    }
}