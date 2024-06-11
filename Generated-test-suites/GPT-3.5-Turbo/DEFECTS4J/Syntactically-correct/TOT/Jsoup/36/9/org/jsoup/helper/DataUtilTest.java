import org.junit.*;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad_validFile_returnsDocument() {
        // Arrange
        String filePath = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "";

        // Act
        Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);

        // Assert
        assertNotNull(doc);
    }

    @Test
    public void testLoad_invalidFile_throwsIOException() {
        // Arrange
        String filePath = "invalid.html";
        String charsetName = "UTF-8";
        String baseUri = "";

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(new File(filePath), charsetName, baseUri);
        });
    }

    @Test
    public void testLoad_validInputStream_returnsDocument() {
        // Arrange
        InputStream in = new ByteArrayInputStream("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "";

        // Act
        Document doc = DataUtil.load(in, charsetName, baseUri);

        // Assert
        assertNotNull(doc);
    }

    @Test
    public void testLoad_invalidInputStream_throwsIOException() {
        // Arrange
        InputStream in = null;
        String charsetName = "UTF-8";
        String baseUri = "";

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(in, charsetName, baseUri);
        });
    }

    @Test
    public void testLoad_validInputStreamWithParser_returnsDocument() {
        // Arrange
        InputStream in = new ByteArrayInputStream("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        // Act
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);

        // Assert
        assertNotNull(doc);
    }

    @Test
    public void testLoad_invalidInputStreamWithParser_throwsIOException() {
        // Arrange
        InputStream in = null;
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(in, charsetName, baseUri, parser);
        });
    }

    @Test
    public void testParseByteData_validByteBufferWithCharset_returnsDocument() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());

        // Act
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData_nullCharset_returnsDocumentWithDefaultCharset() {
        // Arrange
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());

        // Act
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(doc);
        assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteData_validByteBufferWithMetaCharset_returnsDocumentWithMetaCharset() {
        // Arrange
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        ByteBuffer byteData = ByteBuffer.wrap("<meta charset=\"UTF-8\">test".getBytes());

        // Act
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteData_validByteBufferWithMetaHttpEquivCharset_returnsDocumentWithMetaHttpEquivCharset() {
        // Arrange
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        ByteBuffer byteData = ByteBuffer.wrap("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">test".getBytes());

        // Act
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer_validInputStream_returnsByteBuffer() {
        // Arrange
        InputStream in = new ByteArrayInputStream("test".getBytes());

        // Act
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);

        // Assert
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBuffer_validInputStreamWithMaxSize_returnsLimitedByteBuffer() {
        // Arrange
        InputStream in = new ByteArrayInputStream("test".getBytes());
        int maxSize = 2;

        // Act
        ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);

        // Assert
        assertNotNull(byteData);
        assertEquals(2, byteData.limit());
    }

    @Test
    public void testReadToByteBuffer_invalidInputStream_throwsIOException() {
        // Arrange
        InputStream in = null;

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.readToByteBuffer(in);
        });
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
    public void testGetCharsetFromContentType_invalidContentType_returnsNull() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_unsupportedCharset_returnsNull() {
        // Arrange
        String contentType = "text/html; charset=invalidCharset";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

}