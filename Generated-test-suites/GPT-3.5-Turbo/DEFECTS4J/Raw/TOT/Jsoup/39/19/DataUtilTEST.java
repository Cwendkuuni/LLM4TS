import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.*;

public class DataUtilTest {
    private File file;
    private InputStream inputStream;

    @Before
    public void setUp() {
        file = new File("test.html");
        inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
    }

    @Test
    public void testLoad_withCharset_validInput() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
            assertEquals(Charset.forName(charsetName), document.outputSettings().charset());
            // Additional assertions specific to the loaded document

        } catch (IOException e) {
            fail("Caught unexpected IOException");
        }
    }

    @Test
    public void testLoad_withCharset_invalidFile() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Document document = DataUtil.load(new File("invalid.html"), charsetName, baseUri);
            fail("Expected IOException, but it was not thrown");
        } catch (IOException e) {
            // Success
        }
    }

    @Test
    public void testLoad_withCharset_nullCharset() {
        try {
            String charsetName = null;
            String baseUri = "https://example.com";
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
            assertEquals(Charset.forName(DataUtil.defaultCharset), document.outputSettings().charset());
            // Additional assertions specific to the loaded document

        } catch (IOException e) {
            fail("Caught unexpected IOException");
        }
    }

    @Test
    public void testLoad_withInputStream_validInput() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
            assertEquals(Charset.forName(charsetName), document.outputSettings().charset());
            // Additional assertions specific to the loaded document

        } catch (IOException e) {
            fail("Caught unexpected IOException");
        }
    }

    @Test
    public void testLoad_withInputStream_invalidInput() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Document document = DataUtil.load(null, charsetName, baseUri);
            fail("Expected NullPointerException, but it was not thrown");
        } catch (NullPointerException e) {
            // Success
        } catch (IOException e) {
            fail("Caught unexpected IOException");
        }
    }

    @Test
    public void testLoad_withInputStream_invalidFile() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            InputStream invalidInputStream = getClass().getClassLoader().getResourceAsStream("invalid.html");
            Document document = DataUtil.load(invalidInputStream, charsetName, baseUri);
            fail("Expected IOException, but it was not thrown");
        } catch (IOException e) {
            // Success
        }
    }

    @Test
    public void testLoad_withInputStream_andParser_validInput() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            assertEquals(Charset.forName(charsetName), document.outputSettings().charset());
            // Additional assertions specific to the loaded document

        } catch (IOException e) {
            fail("Caught unexpected IOException");
        }
    }

    @Test
    public void testParseByteData_validInput() {
        try {
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.parseByteData(null, charsetName, baseUri, parser);
            assertNull(document);
        } catch (IOException e) {
            fail("Caught unexpected IOException");
        }
    }

    @Test
    public void testReadToByteBuffer_withMaxSize_validInput() {
        try {
            int maxSize = 1024;
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            assertTrue(byteBuffer.capacity() <= maxSize);
            // Additional assertions specific to the filled byte buffer

        } catch (IOException e) {
            fail("Caught unexpected IOException");
        }
    }

    @Test
    public void testReadToByteBuffer_withMaxSize_negativeMaxSize() {
        try {
            int maxSize = -1;
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            assertFalse(byteBuffer.capacity() > 0);
            // Additional assertions specific to the filled byte buffer

        } catch (IOException e) {
            fail("Caught unexpected IOException");
        }
    }

    @Test
    public void testReadToByteBuffer_validInput() {
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertTrue(byteBuffer.capacity() > 0);
            // Additional assertions specific to the filled byte buffer

        } catch (IOException e) {
            fail("Caught unexpected IOException");
        }
    }

    @Test
    public void testGetCharsetFromContentType_validContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_invalidContentType() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_supportedCharset() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentType_unsupportedCharset() {
        String contentType = "text/html; charset=unsupportedCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_uppercaseCharset() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType.toUpperCase());
        assertEquals("ISO-8859-1", charset);
    }
}