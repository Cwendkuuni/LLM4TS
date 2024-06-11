import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void load_shouldReturnDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void parseByteData_shouldReturnDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void readToByteBuffer_shouldReturnByteBuffer() throws IOException {
        // Arrange
        File file = new File("test.html");

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(file);

        // Assert
        assertNotNull(byteBuffer);
        assertEquals("ByteBuffer length should be equal to file length", file.length(), byteBuffer.capacity());
    }

    @Test
    public void crossStreams_shouldCopyInputStreamToOutputStream() throws IOException {
        // Arrange
        ByteBuffer byteData = ByteBuffer.allocate(100);
        InputStream inputStream = new ByteArrayInputStream(byteData.array());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert
        byte[] inputBytes = byteData.array();
        byte[] outputBytes = outputStream.toByteArray();
        assertArrayEquals("Input and output byte arrays should be equal", inputBytes, outputBytes);
    }

    @Test
    public void getCharsetFromContentType_shouldReturnCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNotNull(charset);
        assertEquals("Charset should be equal to UTF-8", "UTF-8", charset);
    }

    @Test
    public void mimeBoundary_shouldReturnRandomStringWithBoundaryLength() {
        // Act
        String mimeBoundary = DataUtil.mimeBoundary();

        // Assert
        assertNotNull(mimeBoundary);
        assertEquals("Mime boundary length should be equal to boundary length constant",
                DataUtil.boundaryLength, mimeBoundary.length());
    }

}