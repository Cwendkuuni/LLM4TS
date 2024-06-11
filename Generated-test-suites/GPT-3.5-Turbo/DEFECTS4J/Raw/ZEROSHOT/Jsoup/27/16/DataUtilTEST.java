import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataUtilTest {

    @Mock
    private File mockFile;

    @Mock
    private InputStream mockInputStream;

    @Mock
    private Parser mockParser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoad_withFileAndCharsetNameAndBaseUri_shouldLoadDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil dataUtil = spy(DataUtil.class);
        FileInputStream mockFileInputStream = mock(FileInputStream.class);
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        Document mockDocument = mock(Document.class);
        whenNew(FileInputStream.class).withParameterTypes(File.class).withArguments(mockFile).thenReturn(mockFileInputStream);
        doReturn(mockByteData).when(dataUtil).readToByteBuffer(mockFileInputStream);
        doReturn(mockDocument).when(dataUtil).parseByteData(mockByteData, charsetName, baseUri, Parser.htmlParser());

        // Act
        Document result = dataUtil.load(mockFile, charsetName, baseUri);

        // Assert
        assertEquals(mockDocument, result);
        verify(mockFileInputStream).close();
    }

    @Test
    public void testLoad_withInputStreamAndCharsetNameAndBaseUri_shouldLoadDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil dataUtil = spy(DataUtil.class);
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        Document mockDocument = mock(Document.class);
        doReturn(mockByteData).when(dataUtil).readToByteBuffer(mockInputStream);
        doReturn(mockDocument).when(dataUtil).parseByteData(mockByteData, charsetName, baseUri, Parser.htmlParser());

        // Act
        Document result = dataUtil.load(mockInputStream, charsetName, baseUri);

        // Assert
        assertEquals(mockDocument, result);
    }

    @Test
    public void testLoad_withInputStreamAndCharsetNameAndBaseUriAndParser_shouldLoadDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil dataUtil = spy(DataUtil.class);
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        Document mockDocument = mock(Document.class);
        doReturn(mockByteData).when(dataUtil).readToByteBuffer(mockInputStream);
        doReturn(mockDocument).when(dataUtil).parseByteData(mockByteData, charsetName, baseUri, mockParser);

        // Act
        Document result = dataUtil.load(mockInputStream, charsetName, baseUri, mockParser);

        // Assert
        assertEquals(mockDocument, result);
    }

    @Test
    public void testParseByteData_withCharsetNameNull_shouldParseByteDataWithDefaultCharset() {
        // Arrange
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser mockParser = mock(Parser.class);
        DataUtil dataUtil = spy(DataUtil.class);
        Document mockDocument = mock(Document.class);
        Element mockMeta = mock(Element.class);
        when(mockParser.parseInput(anyString(), eq(baseUri))).thenReturn(mockDocument);
        when(mockDocument.select(anyString())).thenReturn(mockElement);
        when(mockMeta.attr(anyString())).thenReturn("charset=UTF-8");
        when(mockElement.first()).thenReturn(mockMeta);

        // Act
        Document result = dataUtil.parseByteData(mockByteData, charsetName, baseUri, mockParser);

        // Assert
        assertEquals(mockDocument, result);
    }

    @Test
    public void testParseByteData_withCharsetNameNotNull_shouldParseByteDataWithSpecifiedCharset() {
        // Arrange
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser mockParser = mock(Parser.class);
        DataUtil dataUtil = spy(DataUtil.class);
        Document mockDocument = mock(Document.class);
        when(mockParser.parseInput(anyString(), eq(baseUri))).thenReturn(mockDocument);

        // Act
        Document result = dataUtil.parseByteData(mockByteData, charsetName, baseUri, mockParser);

        // Assert
        assertEquals(mockDocument, result);
    }

    @Test
    public void testReadToByteBuffer_shouldReadFromInputStreamToByteBuffer() throws IOException {
        // Arrange
        DataUtil dataUtil = new DataUtil();
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(DataUtil.bufferSize);
        int read = 10;
        when(mockInputStream.read(buffer)).thenReturn(read).thenReturn(-1);

        // Act
        ByteBuffer result = dataUtil.readToByteBuffer(mockInputStream);

        // Assert
        verify(mockInputStream, times(2)).read(buffer);
        assertEquals(ByteBuffer.wrap(outStream.toByteArray()), result);
    }

    @Test
    public void testGetCharsetFromContentType_withContentTypeNull_shouldReturnNull() {
        // Arrange
        String contentType = null;

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType_shouldReturnCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_shouldReturnNull() {
        // Arrange
        String contentType = "text/html";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(result);
    }
}