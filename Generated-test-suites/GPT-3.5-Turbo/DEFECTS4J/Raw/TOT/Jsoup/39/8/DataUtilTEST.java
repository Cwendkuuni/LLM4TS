import org.jsoup.Jsoup;
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

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoad_File_ValidFile_ShouldReturnDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        Document expectedDocument = Jsoup.parse(file, CHARSET_NAME, BASE_URI);

        // Act
        Document actualDocument = dataUtil.load(file, CHARSET_NAME, BASE_URI);

        // Assert
        assertNotNull(actualDocument);
        assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test(expected = IOException.class)
    public void testLoad_File_InvalidFile_ShouldThrowIOException() throws IOException {
        // Arrange
        File file = new File("invalid.html");

        // Act
        dataUtil.load(file, CHARSET_NAME, BASE_URI);

        // Assert
        // IOException is expected to be thrown
    }

    @Test
    public void testLoad_InputStream_ValidInputStream_ShouldReturnDocument() throws IOException {
        // Arrange
        String html = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document expectedDocument = Jsoup.parse(inputStream, CHARSET_NAME, BASE_URI);

        // Act
        Document actualDocument = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI);

        // Assert
        assertNotNull(actualDocument);
        assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test(expected = IOException.class)
    public void testLoad_InputStream_InvalidInputStream_ShouldThrowIOException() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("".getBytes());

        // Act
        dataUtil.load(inputStream, CHARSET_NAME, BASE_URI);

        // Assert
        // IOException is expected to be thrown
    }

    @Test
    public void testLoad_InputStreamWithParser_ValidInputStream_ShouldReturnDocument() throws IOException {
        // Arrange
        String html = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document expectedDocument = Jsoup.parse(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        // Act
        Document actualDocument = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        // Assert
        assertNotNull(actualDocument);
        assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test(expected = IOException.class)
    public void testLoad_InputStreamWithParser_InvalidInputStream_ShouldThrowIOException() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("".getBytes());

        // Act
        dataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        // Assert
        // IOException is expected to be thrown
    }

    // Additional test cases for parseByteData, readToByteBuffer, and getCharsetFromContentType methods can be included here

}