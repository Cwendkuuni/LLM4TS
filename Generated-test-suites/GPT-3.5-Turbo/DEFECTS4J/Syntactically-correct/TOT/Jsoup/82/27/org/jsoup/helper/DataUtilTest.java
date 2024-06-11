import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile_validFile_expectDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Assert
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFile_invalidFile_expectIOException() throws IOException {
        // Arrange
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Assert
        // Expecting IOException to be thrown
    }

    @Test
    public void testLoadInputStream_validInputStream_expectDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStream_invalidInputStream_expectIOException() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        // Expecting IOException to be thrown
    }

    @Test
    public void testLoadInputStreamWithCustomParser_validInputStream_expectDocument() throws IOException {
        // Arrange
        File file = new File("test.xml");
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithCustomParser_invalidInputStream_expectIOException() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("nonexistent.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        // Expecting IOException to be thrown
    }

    @Test
    public void testCrossStreams_validInputStreamAndOutputStream_expectNoExceptions() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.txt");
        OutputStream outputStream = System.out;

        // Act
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert
        // No exceptions should be thrown
    }

    @Test
    public void testReadToByteBuffer_validInputStreamAndMaxSize_expectByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.txt");
        int maxSize = 1024;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBuffer_invalidInputStreamAndMaxSize_expectIOException() {
        // Arrange
        InputStream inputStream = null;
        int maxSize = 1024;

        // Act and Assert
        assertThrows(IOException.class, () -> DataUtil.readToByteBuffer(inputStream, maxSize));
    }

    @Test
    public void testGetCharsetFromContentType_validContentType_expectCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_invalidContentType_expectNull() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary_expectValidMimeBoundaryString() {
        // Act
        String mimeBoundary = DataUtil.mimeBoundary();

        // Assert
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

}