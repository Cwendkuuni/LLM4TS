import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromExistingFile_shouldReturnDocumentWithExpectedContent() throws IOException {
        // Arrange
        File file = new File("testfile.html");
        file.createNewFile();
        String content = "<html><body><h1>Hello, world!</h1></body></html>";
        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);

        // Act
        Document document = DataUtil.load(file, null, null);

        // Assert
        assertNotNull(document);
        assertEquals(content, document.html());

        // Cleanup
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFromNonExistingFile_shouldThrowIOException() throws IOException {
        // Arrange
        File file = new File("nonexistent.html");

        // Act
        DataUtil.load(file, null, null);
    }

    @Test
    public void testLoadFromInputStream_shouldReturnDocumentWithExpectedContent() throws IOException {
        // Arrange
        String content = "<html><body><h1>Hello, world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // Act
        Document document = DataUtil.load(inputStream, null, null);

        // Assert
        assertNotNull(document);
        assertEquals(content, document.html());
    }

    @Test
    public void testLoadFromInputStreamWithCharset_shouldReturnDocumentWithExpectedContent() throws IOException {
        // Arrange
        String content = "<html><body><h1>Hello, world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // Act
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), null, Parser.htmlParser());

        // Assert
        assertNotNull(document);
        assertEquals(content, document.html());
    }

    @Test
    public void testCrossStreams_shouldCopyInputStreamToOutputStream() throws IOException {
        // Arrange
        String content = "Hello, world!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert
        assertEquals(content, outputStream.toString());
    }

    @Test
    public void testReadToByteBuffer_shouldReturnByteBufferWithExpectedContent() throws IOException {
        // Arrange
        String content = "Hello, world!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertEquals(content, StandardCharsets.UTF_8.decode(byteBuffer).toString());
    }

    @Test
    public void testReadFileToByteBuffer_shouldReturnByteBufferWithExpectedContent() throws IOException {
        // Arrange
        File file = new File("testfile.txt");
        file.createNewFile();
        String content = "Hello, world!";
        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);

        // Act
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Assert
        assertEquals(content, StandardCharsets.UTF_8.decode(byteBuffer).toString());

        // Cleanup
        file.delete();
    }

    @Test
    public void testMimeBoundary_shouldReturnRandomString() {
        // Arrange & Act
        String mimeBoundary = DataUtil.mimeBoundary();

        // Assert
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testGetCharsetFromContentType_shouldReturnCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset_shouldReturnNull() {
        // Arrange
        String contentType = "text/html; charset=INVALID-CHARSET";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void testValidateCharsetWithValidCharset_shouldReturnSameCharset() {
        // Arrange
        String charset = "UTF-8";

        // Act
        String validatedCharset = DataUtil.validateCharset(charset);

        // Assert
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithUnsupportedCharset_shouldReturnNull() {
        // Arrange
        String charset = "INVALID-CHARSET";

        // Act
        String validatedCharset = DataUtil.validateCharset(charset);

        // Assert
        assertNull(validatedCharset);
    }

}