

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class DataUtilTest {

    //Test case for load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFromFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            assertNotNull(document);
            assertTrue(document instanceof Document);
            assertNotNull(document.head());
            assertNotNull(document.body());
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    //Test case for load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";

            Document document = DataUtil.load(inputStream, charsetName, baseUri);

            assertNotNull(document);
            assertTrue(document instanceof Document);
            assertNotNull(document.head());
            assertNotNull(document.body());
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    //Test case for load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();

            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

            assertNotNull(document);
            assertTrue(document instanceof Document);
            assertNotNull(document.head());
            assertNotNull(document.body());
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    //Test case for crossStreams(final InputStream in, final OutputStream out)
    @Test
    public void testCrossStreams() {
        try {
            String input = "Test input string";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            DataUtil.crossStreams(inputStream, outputStream);

            assertEquals(input, outputStream.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    //Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseByteData() {
        try {
            String htmlData = "<html><head></head><body></body></html>";
            ByteBuffer byteBuffer = ByteBuffer.wrap(htmlData.getBytes(StandardCharsets.UTF_8));
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();

            Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

            assertNotNull(document);
            assertTrue(document instanceof Document);
            assertNotNull(document.head());
            assertNotNull(document.body());
            assertEquals(baseUri, document.baseUri());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    //Test case for readToByteBuffer(InputStream inStream, int maxSize)
    @Test
    public void testReadToByteBuffer() {
        try {
            String inputData = "Test input data";
            InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

            assertEquals(inputData, new String(byteBuffer.array(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    //Test case for readFileToByteBuffer(File file)
    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("test.html");
            String testData = "<html><head></head><body></body></html>";
            PrintWriter writer = new PrintWriter(file);
            writer.write(testData);
            writer.close();

            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

            assertEquals(testData, new String(byteBuffer.array(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    //Test case for emptyByteBuffer()
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    //Test case for getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    //Test case for mimeBoundary()
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}

