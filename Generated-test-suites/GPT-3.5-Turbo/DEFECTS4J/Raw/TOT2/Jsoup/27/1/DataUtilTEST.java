import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class DataUtilTest {
    
    private static Document testDoc;

    @BeforeClass
    public static void setUp() {
        File testFile = new File("test.html");
        String testHtml = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        try {
            Document tempDoc = DataUtil.parseByteData(ByteBuffer.wrap(testHtml.getBytes()), "UTF-8", "", Parser.htmlParser());
            testDoc = tempDoc;
        } catch (IOException e) {
            fail("Exception occurred while setting up test document");
        }
    }

    @AfterClass
    public static void tearDown() {
        testDoc = null;
    }

    @Test
    public void testLoadFromFile() {
        try {
            Document loadedDoc = DataUtil.load(new File("test.html"), "UTF-8", "");
            assertEquals(testDoc.outerHtml(), loadedDoc.outerHtml());
        } catch (IOException e) {
            fail("Exception occurred while loading document from file");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(testDoc.outerHtml().getBytes());
            Document loadedDoc = DataUtil.load(inputStream, "UTF-8", "");
            assertEquals(testDoc.outerHtml(), loadedDoc.outerHtml());
        } catch (IOException e) {
            fail("Exception occurred while loading document from input stream");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(testDoc.outerHtml().getBytes());
            Document loadedDoc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
            assertEquals(testDoc.outerHtml(), loadedDoc.outerHtml());
        } catch (IOException e) {
            fail("Exception occurred while loading document from input stream with parser");
        }
    }

    @Test
    public void testParseByteDataWithCharset() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(testDoc.outerHtml().getBytes());
            Document parsedDoc = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
            assertEquals(testDoc.outerHtml(), parsedDoc.outerHtml());
        } catch (IOException e) {
            fail("Exception occurred while parsing byte data with charset");
        }
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(testDoc.outerHtml().getBytes());
            Document parsedDoc = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
            assertEquals(testDoc.outerHtml(), parsedDoc.outerHtml());
        } catch (IOException e) {
            fail("Exception occurred while parsing byte data without charset");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String invalidContentType = "text/html";
        String invalidCharset = DataUtil.getCharsetFromContentType(invalidContentType);
        assertNull(invalidCharset);
    }
}