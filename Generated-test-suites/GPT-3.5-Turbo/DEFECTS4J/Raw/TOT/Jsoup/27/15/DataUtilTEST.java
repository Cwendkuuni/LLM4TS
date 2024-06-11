import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class DataUtilTest {

    @Mock
    private File mockFile;

    @Mock
    private FileInputStream mockFileInputStream;

    @Mock
    private InputStream mockInputStream;

    @Mock
    private Parser mockParser;

    @Mock
    private Document mockDocument;

    @Test
    public void load_shouldReturnDocument_whenFileIsLoaded() throws IOException {
        // Arrange
        MockitoAnnotations.initMocks(this); // Initialize the mocks
        when(mockFileInputStream.read(any(byte[].class))).thenReturn(1, -1);
        when(mockParser.htmlParser()).thenReturn(Parser.htmlParser());
        when(mockParser.parseInput(any(), any())).thenReturn(mockDocument);

        // Act
        Document result = DataUtil.load(mockFile, "UTF-8", "http://example.com");

        // Assert
        verify(mockFileInputStream, times(2)).close();
        verify(mockFile, times(1)).close();
        assertEquals(mockDocument, result);
    }

    @Test
    public void load_shouldReturnDocument_whenInputStreamIsLoaded() throws IOException {
        // Arrange
        MockitoAnnotations.initMocks(this); // Initialize the mocks
        when(mockInputStream.read(any(byte[].class))).thenReturn(1, -1);
        when(mockParser.htmlParser()).thenReturn(Parser.htmlParser());
        when(mockParser.parseInput(any(), any())).thenReturn(mockDocument);

        // Act
        Document result = DataUtil.load(mockInputStream, "UTF-8", "http://example.com");

        // Assert
        verify(mockInputStream, times(1)).close();
        assertEquals(mockDocument, result);
    }

    @Test
    public void load_shouldReturnDocument_whenInputStreamAndParserAreLoaded() throws IOException {
        // Arrange
        MockitoAnnotations.initMocks(this); // Initialize the mocks
        when(mockInputStream.read(any(byte[].class))).thenReturn(1, -1);
        when(mockParser.parseInput(any(), any())).thenReturn(mockDocument);

        // Act
        Document result = DataUtil.load(mockInputStream, "UTF-8", "http://example.com", mockParser);

        // Assert
        verify(mockInputStream, times(1)).close();
        assertEquals(mockDocument, result);
    }

    @Test
    public void parseByteData_shouldReturnDocument_whenByteDataIsParsed() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap(new byte[5]);
        when(mockParser.htmlParser()).thenReturn(Parser.htmlParser());
        when(mockParser.parseInput(any(), any())).thenReturn(mockDocument);

        // Act
        Document result = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", mockParser);

        // Assert
        assertEquals(mockDocument, result);
    }

    @Test
    public void readToByteBuffer_shouldReturnByteBuffer_whenInputStreamIsRead() throws IOException {
        // Arrange
        MockitoAnnotations.initMocks(this); // Initialize the mocks
        byte[] buffer = new byte[5];
        when(mockInputStream.read(any(byte[].class))).thenReturn(1, -1);

        // Act
        ByteBuffer result = DataUtil.readToByteBuffer(mockInputStream);

        // Assert
        verify(mockInputStream, times(1)).close();
        assertEquals(ByteBuffer.wrap(buffer), result);
    }

    @Test
    public void getCharsetFromContentType_shouldReturnCharset_whenContentTypeIsProvided() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", result);
    }

    @Test
    public void getCharsetFromContentType_shouldReturnNull_whenContentTypeIsNull() {
        // Arrange
        String contentType = null;

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals(null, result);
    }

    @Test
    public void getCharsetFromContentType_shouldReturnNull_whenCharsetIsNotFound() {
        // Arrange
        String contentType = "text/html";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals(null, result);
    }

    @Test
    public void getCharsetFromContentType_shouldReturnUppercaseCharset_whenCharsetIsMixedCase() {
        // Arrange
        String contentType = "text/html; charset=utf-8";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", result);
    }

}