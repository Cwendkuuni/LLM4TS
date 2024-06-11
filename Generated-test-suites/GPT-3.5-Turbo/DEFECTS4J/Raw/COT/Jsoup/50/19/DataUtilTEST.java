
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";

            Document document = DataUtil.load(file, charsetName, baseUri);

            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";

            Document document = DataUtil.load(inputStream, charsetName, baseUri);

            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();

            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            OutputStream outputStream = new FileOutputStream("output.txt");

            DataUtil.crossStreams(inputStream, outputStream);

            File outputFile = new File("output.txt");
            assertTrue(outputFile.exists());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Hello, world!".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");

            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("input.txt");

            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.array().length);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}
