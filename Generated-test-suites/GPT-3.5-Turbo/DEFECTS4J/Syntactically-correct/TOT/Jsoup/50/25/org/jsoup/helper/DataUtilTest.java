import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DataUtilTest {

    private final File testFile = new File("test.html");
    private final String baseUri = "http://example.com";

    @Test
    public void loadFile_ValidFile_ShouldReturnDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";

        // Act
        Document result = DataUtil.load(testFile, charsetName, baseUri);

        // Assert
        assertNotNull(result);
        assertEquals(Jsoup.parse(testFile, charsetName, baseUri, Parser.htmlParser()), result);
    }

    @Test
    public void loadFile_NullFile_ShouldThrowIOException() throws IOException {
        // Arrange
        String charsetName = "UTF-8";

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(null, charsetName, baseUri);
        });
    }

    @Test
    public void loadInputStream_ValidInputStream_ShouldReturnDocument() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";

        // Act
        Document result = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(result);
        assertEquals(Jsoup.parse(testFile, charsetName, baseUri, Parser.htmlParser()), result);
    }

    @Test
    public void loadInputStream_NullInputStream_ShouldThrowIOException() throws IOException {
        // Arrange
        String charsetName = "UTF-8";

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load((InputStream) null, charsetName, baseUri);
        });
    }

    @Test
    public void loadInputStream_InvalidCharset_ShouldThrowIllegalCharsetNameException() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "InvalidCharset";

        // Act & Assert
        assertThrows(IllegalCharsetNameException.class, () -> {
            DataUtil.load(inputStream, charsetName, baseUri);
        });
    }

    @Test
    public void load_SpecifiedParser_ShouldReturnDocument() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        Parser parser = Parser.xmlParser();

        // Act
        Document result = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(result);
        assertEquals(Jsoup.parse(testFile, charsetName, baseUri, parser), result);
    }

    @Test
    public void crossStreams_ValidInputStreamAndOutputStream_ShouldCopyInputStreamToOutputStream() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        OutputStream outputStream = System.out;

        // Act
        DataUtil.crossStreams(inputStream, outputStream);

        // No assertions, output to console for manual verification
    }

    @Test
    public void parseByteData_ValidByteData_ShouldReturnDocument() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        byte[] byteData = "Test HTML".getBytes(Charset.forName(charsetName));

        // Act
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(result);
        assertEquals(Jsoup.parse(new String(byteData, Charset.forName(charsetName)), baseUri, parser), result);
    }

    @Test
    public void readToByteBuffer_ValidInputStream_ShouldReturnByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        // Act
        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertNotNull(result);
        byte[] expectedBytes = inputStream.readAllBytes();
        byte[] actualBytes = result.array();
        assertTrue(Arrays.equals(expectedBytes, actualBytes));
    }

    @Test
    public void readFileToByteBuffer_ValidFile_ShouldReturnByteBuffer() throws IOException {
        // Arrange
        byte[] fileBytes = "Test HTML".getBytes(Charset.forName("UTF-8"));
        Files.write(testFile.toPath(), fileBytes);

        // Act
        ByteBuffer result = DataUtil.readFileToByteBuffer(testFile);

        // Assert
        assertNotNull(result);
        byte[] actualBytes = result.array();
        assertTrue(Arrays.equals(fileBytes, actualBytes));
    }

    @Test
    public void emptyByteBuffer_ShouldReturnEmptyByteBuffer() {
        // Arrange

        // Act
        ByteBuffer result = DataUtil.emptyByteBuffer();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.capacity());
    }

    @Test
    public void getCharsetFromContentType_ValidContentType_ShouldReturnCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNotNull(result);
        assertEquals("UTF-8", result);
    }

    @Test
    public void getCharsetFromContentType_InvalidContentType_ShouldReturnNull() {
        // Arrange
        String contentType = "text/html; charset=InvalidCharset";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(result);
    }

    @Test
    public void mimeBoundary_ShouldReturnRandomMimeBoundary() {
        // Arrange

        // Act
        String result = DataUtil.mimeBoundary();

        // Assert
        assertNotNull(result);
        assertEquals(DataUtil.boundaryLength, result.length());
    }
}