import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataUtilTest {
    private File testFile;
    private String testHtml;
    private String baseUri;

    @Before
    public void setUp() {
        testFile = new File("test.html"); // Create a test file
        testHtml = "<html><head><title>Test</title></head><body><h1>Welcome</h1></body></html>";
        baseUri = "http://example.com";
    }

    @After
    public void tearDown() {
        if (testFile.exists()) {
            testFile.delete(); // Clean up test file
        }
    }

    @Test
    public void testLoadFromFile() {
        try {
            // Write test HTML to file
            FileUtils.writeStringToFile(testFile, testHtml, StandardCharsets.UTF_8);
            
            // Load HTML from file
            Document doc = DataUtil.load(testFile, "UTF-8", baseUri);

            // Verification
            assertNotNull(doc);
            assertEquals(testHtml, doc.html());
            assertEquals("Test", doc.title());
        } catch (IOException e) {
            fail("IOException thrown during test execution");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            // Convert test HTML to input stream
            InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));

            // Load HTML from input stream
            Document doc = DataUtil.load(inputStream, "UTF-8", baseUri);

            // Verification
            assertNotNull(doc);
            assertEquals(testHtml, doc.html());
            assertEquals("Test", doc.title());
        } catch (IOException e) {
            fail("IOException thrown during test execution");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            // Convert test HTML to input stream
            InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));

            // Load HTML from input stream with custom parser
            Document doc = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.xmlParser());

            // Verification
            assertNotNull(doc);
            assertEquals(testHtml, doc.html());
            assertEquals("Test", doc.title());
        } catch (IOException e) {
            fail("IOException thrown during test execution");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            // Create input and output streams for testing
            ByteArrayInputStream input = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            // Cross input and output streams
            DataUtil.crossStreams(input, output);

            // Verification
            assertEquals(testHtml, output.toString("UTF-8"));
        } catch (IOException e) {
            fail("IOException thrown during test execution");
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            // Convert test HTML to input stream
            InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));

            // Parse HTML from input stream
            Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", baseUri, Parser.htmlParser());

            // Verification
            assertNotNull(doc);
            assertEquals(testHtml, doc.html());
            assertEquals("Test", doc.title());
        } catch (IOException e) {
            fail("IOException thrown during test execution");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            // Convert test HTML to input stream
            InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));

            // Read input stream to byte buffer
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

            // Verification
            assertNotNull(byteBuffer);
            assertEquals(testHtml, new String(byteBuffer.array(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            fail("IOException thrown during test execution");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        // Get charset from content type
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Verification
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        // Generate mime boundary
        String mimeBoundary = DataUtil.mimeBoundary();

        // Verification
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testCharsetDetectionFromBom() {
        try {
            // Convert test HTML to input stream with BOM
            byte[] bomUtf8 = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            byte[] bytesUtf8 = testHtml.getBytes(StandardCharsets.UTF_8);
            byte[] inputBytesUtf8 = new byte[bomUtf8.length + bytesUtf8.length];
            System.arraycopy(bomUtf8, 0, inputBytesUtf8, 0, bomUtf8.length);
            System.arraycopy(bytesUtf8, 0, inputBytesUtf8, bomUtf8.length, bytesUtf8.length);
            InputStream inputStreamUtf8 = new ByteArrayInputStream(inputBytesUtf8);

            // Detect charset from BOM
            Document docUtf8 = DataUtil.parseInputStream(inputStreamUtf8, null, baseUri, Parser.htmlParser());

            // Verification
            assertNotNull(docUtf8);
            assertEquals(testHtml, docUtf8.html());
            assertEquals("Test", docUtf8.title());
        } catch (IOException e) {
            fail("IOException thrown during test execution");
        }
    }

    @Test
    public void testCharsetValidation() {
        String invalidCharset = "Invalid-Charset";

        // Validate invalid charset
        String validatedCharset = DataUtil.validateCharset(invalidCharset);

        // Verification
        assertNull(validatedCharset);
    }
}