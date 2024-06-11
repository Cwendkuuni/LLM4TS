import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private File file;
    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        file = new File("test.html");
        baseUri = "http://www.example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoad_File_ValidCharset_ValidBaseUri() throws IOException {
        // Arrange
        String charsetName = "UTF-8";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_File_InValidCharset_ValidBaseUri() throws IOException {
        // Arrange
        String charsetName = "InvalidCharset";

        // Act & Assert
        assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    public void testLoad_File_ValidCharset_InvalidBaseUri() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String invalidBaseUri = "invalidBaseUri";

        // Act & Assert
        assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, invalidBaseUri));
    }

    @Test
    public void testLoad_InputStream_ValidCharset_ValidBaseUri() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_InputStream_InValidCharset_ValidBaseUri() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "InvalidCharset";

        // Act & Assert
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoad_InputStream_ValidCharset_InvalidBaseUri() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String invalidBaseUri = "invalidBaseUri";

        // Act & Assert
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, invalidBaseUri));
    }

    @Test
    public void testLoad_InputStream_CustomParser_ValidBaseUri() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
    }

    // Additional methods' test cases...

    @Test
    public void testCrossStreams_ValidInputStream_ValidOutputStream() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert
        // Verify the output stream content
    }

    // Additional methods' test cases...

    @Test
    public void testReadToByteBuffer_InputStream_ValidMaxSize() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream(file);
        int maxSize = 1024;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        assertNotNull(byteBuffer);
        assertEquals(byteBuffer.capacity(), maxSize);
    }

    @Test
    public void testReadToByteBuffer_InputStream_InvalidMaxSize() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream(file);
        int maxSize = -1;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> DataUtil.readToByteBuffer(inputStream, maxSize));
    }

    // Additional methods' test cases...

    @Test
    public void testGetCharsetFromContentType_ValidContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        // Act
        String mimeBoundary = DataUtil.mimeBoundary();

        // Assert
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    // Additional methods' test cases...

}