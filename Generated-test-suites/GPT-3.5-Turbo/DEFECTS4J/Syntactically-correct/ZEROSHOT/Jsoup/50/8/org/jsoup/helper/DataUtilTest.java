import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private File file;
    @Mock
    private InputStream inputStream;
    @Mock
    private Parser parser;
    @Mock
    private Document document;

    private DataUtil dataUtil;
    private String charsetName;
    private String baseUri;
    private String contentType;
    private int maxSize;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
        charsetName = "UTF-8";
        baseUri = "http://example.com";
        contentType = "text/html; charset=UTF-8";
        maxSize = 1000;
    }

    @Test
    public void testLoadFile() throws IOException {
        when(DataUtil.readFileToByteBuffer(file)).thenReturn(ByteBuffer.allocate(10));
        when(parser.htmlParser()).thenReturn(parser);
        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = dataUtil.load(file, charsetName, baseUri);

        assertNotNull(result);
        assertEquals(document, result);
        verify(DataUtil.class, times(1)).readFileToByteBuffer(file);
        verify(parser, times(1)).parseInput(anyString(), anyString());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(ByteBuffer.allocate(10));
        when(parser.htmlParser()).thenReturn(parser);
        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = dataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(result);
        assertEquals(document, result);
        verify(DataUtil.class, times(1)).readToByteBuffer(inputStream);
        verify(parser, times(1)).parseInput(anyString(), anyString());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(ByteBuffer.allocate(10));
        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = dataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(result);
        assertEquals(document, result);
        verify(DataUtil.class, times(1)).readToByteBuffer(inputStream);
        verify(parser, times(1)).parseInput(anyString(), anyString());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inStream = mock(InputStream.class);
        OutputStream outStream = mock(OutputStream.class);

        dataUtil.crossStreams(inStream, outStream);

        verify(inStream, times(1)).read(any(byte[].class));
        verify(outStream, times(1)).write(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(10);

        Document result = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(result);
        verify(parser, times(1)).parseInput(anyString(), anyString());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        when(inputStream.read(any(byte[].class))).thenReturn(10);

        ByteBuffer result = dataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(result);
        verify(inputStream, times(1)).read(any(byte[].class));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        when(inputStream.read(any(byte[].class))).thenReturn(10);

        ByteBuffer result = dataUtil.readToByteBuffer(inputStream);

        assertNotNull(result);
        verify(inputStream, times(1)).read(any(byte[].class));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        RandomAccessFile randomAccessFile = mock(RandomAccessFile.class);
        whenNew(RandomAccessFile.class).withArguments(file, "r").thenReturn(randomAccessFile);
        when(randomAccessFile.length()).thenReturn(10L);
        when(randomAccessFile.readFully(any(byte[].class))).thenReturn(10);

        ByteBuffer result = dataUtil.readFileToByteBuffer(file);

        assertNotNull(result);
        verify(randomAccessFile, times(1)).readFully(any(byte[].class));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer result = dataUtil.emptyByteBuffer();

        assertNotNull(result);
        assertEquals(0, result.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String result = dataUtil.getCharsetFromContentType(contentType);

        assertNotNull(result);
        assertEquals(charsetName, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        contentType = "text/html; charset=unsupportedCharset";

        String result = dataUtil.getCharsetFromContentType(contentType);

        assertNotNull(result);
        assertEquals(null, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String result = dataUtil.getCharsetFromContentType("text/html");

        assertNotNull(result);
        assertEquals(null, result);
    }

    @Test
    public void testMimeBoundary() {
        String result = dataUtil.mimeBoundary();

        assertNotNull(result);
        assertEquals(DataUtil.boundaryLength, result.length());
    }
}