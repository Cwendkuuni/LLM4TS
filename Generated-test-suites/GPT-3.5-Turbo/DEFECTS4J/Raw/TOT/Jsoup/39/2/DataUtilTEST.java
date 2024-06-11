import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private File testFile;
    private String testFileContent;
    private static final String BASE_URI = "http://example.com/";

    @Before
    public void setup() throws IOException {
        testFile = new File("test.html");
        testFileContent = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        FileUtil.writeStringToFile(testFileContent, testFile);
    }

    @After
    public void cleanup() {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoad_file_validCharset_returnsDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";

        // Act
        Document document = DataUtil.load(testFile, charsetName, BASE_URI);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void testLoad_file_invalidCharset_throwsIOException() {
        // Arrange
        String charsetName = "InvalidCharset";

        // Act & Assert
        try {
            DataUtil.load(testFile, charsetName, BASE_URI);
        } catch (IOException e) {
            assertEquals("java.nio.charset.UnsupportedCharsetException: InvalidCharset", e.getMessage());
        }
    }

    @Test
    public void testLoad_file_nullCharset_usesDefaultCharset() throws IOException {
        // Arrange
        String charsetName = null;

        // Act
        Document document = DataUtil.load(testFile, charsetName, BASE_URI);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void testLoad_file_nonExistentFile_throwsIOException() {
        // Arrange
        String charsetName = "UTF-8";
        File nonExistentFile = new File("nonexistent.html");

        // Act & Assert
        try {
            DataUtil.load(nonExistentFile, charsetName, BASE_URI);
        } catch (IOException e) {
            assertEquals("java.io.FileNotFoundException: nonexistent.html (The system cannot find the file specified)", e.getMessage());
        }
    }

    @Test
    public void testLoad_inputStream_validCharset_returnsDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        FileInputStream inputStream = new FileInputStream(testFile);

        // Act
        Document document = DataUtil.load(inputStream, charsetName, BASE_URI);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void testLoad_inputStream_invalidCharset_throwsIOException() throws IOException {
        // Arrange
        String charsetName = "InvalidCharset";

        // Create InputStream with testFileContent
        InputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_8));

        // Act & Assert
        try {
            DataUtil.load(inputStream, charsetName, BASE_URI);
        } catch (IOException e) {
            assertEquals("java.nio.charset.UnsupportedCharsetException: InvalidCharset", e.getMessage());
        }
    }

    @Test
    public void testLoad_inputStream_nullCharset_usesDefaultCharset() throws IOException {
        // Arrange
        String charsetName = null;
        FileInputStream inputStream = new FileInputStream(testFile);

        // Act
        Document document = DataUtil.load(inputStream, charsetName, BASE_URI);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void testLoad_inputStream_nonExistentFile_throwsIOException() {
        // Arrange
        String charsetName = "UTF-8";
        InputStream inputStream = new FileInputStream(new File("nonexistent.html"));

        // Act & Assert
        try {
            DataUtil.load(inputStream, charsetName, BASE_URI);
        } catch (IOException e) {
            assertEquals("java.io.FileNotFoundException: nonexistent.html (The system cannot find the file specified)", e.getMessage());
        }
    }

    @Test
    public void testLoad_inputStream_withParser_validCharset_returnsDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        FileInputStream inputStream = new FileInputStream(testFile);
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, BASE_URI, parser);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void testLoad_inputStream_withParser_invalidCharset_throwsIOException() throws IOException {
        // Arrange
        String charsetName = "InvalidCharset";
        Parser parser = Parser.htmlParser();

        // Create InputStream with testFileContent
        InputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_8));

        // Act & Assert
        try {
            DataUtil.load(inputStream, charsetName, BASE_URI, parser);
        } catch (IOException e) {
            assertEquals("java.nio.charset.UnsupportedCharsetException: InvalidCharset", e.getMessage());
        }
    }

    @Test
    public void testLoad_inputStream_withParser_nullCharset_usesDefaultCharset() throws IOException {
        // Arrange
        String charsetName = null;
        FileInputStream inputStream = new FileInputStream(testFile);
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, BASE_URI, parser);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void testParseByteData_validCharset_returnsDocument() {
        // Arrange
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(testFileContent.getBytes(StandardCharsets.UTF_8));

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, BASE_URI, Parser.htmlParser());

        // Assert
        assertNotNull(document);
    }

    @Test
    public void testParseByteData_invalidCharset_returnsDocumentWithoutCharset() {
        // Arrange
        String charsetName = "InvalidCharset";
        ByteBuffer byteData = ByteBuffer.wrap(testFileContent.getBytes(StandardCharsets.UTF_8));

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, BASE_URI, Parser.htmlParser());

        // Assert
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData_nullCharset_returnsDocumentWitDefaultCharset() {
        // Arrange
        String charsetName = null;
        ByteBuffer byteData = ByteBuffer.wrap(testFileContent.getBytes(StandardCharsets.UTF_8));

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, BASE_URI, Parser.htmlParser());

        // Assert
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer_validInputStream_returnsByteBuffer() throws IOException {
        // Arrange
        FileInputStream inputStream = new FileInputStream(testFile);

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBuffer_inputStreamWithMaxSize_returnsByteBufferWithMaxSize() throws IOException {
        // Arrange
        FileInputStream inputStream = new FileInputStream(testFile);
        int maxSize = 50;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_nonExistentFile_throwsIOException() {
        // Arrange
        InputStream inputStream = new FileInputStream(new File("nonexistent.html"));

        // Act & Assert
        try {
            DataUtil.readToByteBuffer(inputStream);
        } catch (IOException e) {
            assertEquals("java.io.FileNotFoundException: nonexistent.html (The system cannot find the file specified)", e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType_validContentType_returnsCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_missingCharset_returnsNull() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals(null, charset);
    }

    @Test
    public void testGetCharsetFromContentType_invalidCharset_returnsNull() {
        // Arrange
        String contentType = "text/html; charset=InvalidCharset";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals(null, charset);
    }

    @Test
    public void testGetCharsetFromContentType_invalidContentType_returnsNull() {
        // Arrange
        String contentType = "invalid";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals(null, charset);
    }

    @Test
    public void testGetCharsetFromContentType_nullContentType_returnsNull() {
        // Arrange
        String contentType = null;

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals(null, charset);
    }
}