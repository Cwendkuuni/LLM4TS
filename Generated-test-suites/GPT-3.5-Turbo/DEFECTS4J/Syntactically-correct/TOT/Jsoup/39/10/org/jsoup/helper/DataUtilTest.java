import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private InputStream mockInputStream;
    private File testFile;
    private String testCharset;
    private String testBaseUri;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testFile = new File("test.html");
        testCharset = "UTF-8";
        testBaseUri = "http://www.example.com/";
    }

    @Test
    public void testLoad_File_Valid() throws IOException {
        Document doc = DataUtil.load(testFile, testCharset, testBaseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoad_File_IOException() throws IOException {
        // Mocking FileInputStream to throw IOException
        FileInputStream mockFileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withArguments(testFile).thenThrow(new IOException());
        Document doc = DataUtil.load(testFile, testCharset, testBaseUri);
    }

    @Test
    public void testLoad_InputStream_Valid() throws IOException {
        Document doc = DataUtil.load(mockInputStream, testCharset, testBaseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoad_InputStream_IOException() throws IOException {
        // Mocking readToByteBuffer to throw IOException
        DataUtil dataUtilSpy = spy(new DataUtil());
        doThrow(new IOException()).when(dataUtilSpy).readToByteBuffer(mockInputStream);
        Document doc = dataUtilSpy.load(mockInputStream, testCharset, testBaseUri);
    }

    @Test
    public void testLoad_InputStreamWithParser_Valid() throws IOException {
        Parser testParser = Parser.xmlParser();
        Document doc = DataUtil.load(mockInputStream, testCharset, testBaseUri, testParser);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoad_InputStreamWithParser_IOException() throws IOException {
        // Mocking readToByteBuffer to throw IOException
        DataUtil dataUtilSpy = spy(new DataUtil());
        doThrow(new IOException()).when(dataUtilSpy).readToByteBuffer(mockInputStream);
        Parser testParser = Parser.xmlParser();
        Document doc = dataUtilSpy.load(mockInputStream, testCharset, testBaseUri, testParser);
    }

    @Test
    public void testParseByteData_Valid() {
        String testDocData = "<html><head><title>Test Document</title></head><body><h1>Test</h1></body></html>";
        ByteBuffer testByteData = ByteBuffer.wrap(testDocData.getBytes(Charset.forName(testCharset)));
        Parser testParser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(testByteData, testCharset, testBaseUri, testParser);
        assertNotNull(doc);
        assertEquals(testBaseUri, doc.baseUri());
    }

    @Test
    public void testParseByteData_NullCharsetName() {
        String testDocData = "<html><head><title>Test Document</title></head><body><h1>Test</h1></body></html>";
        ByteBuffer testByteData = ByteBuffer.wrap(testDocData.getBytes(Charset.forName(testCharset)));
        Parser testParser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(testByteData, null, testBaseUri, testParser);
        assertNotNull(doc);
        assertEquals(testBaseUri, doc.baseUri());
    }

    @Test
    public void testParseByteData_InvalidCharset() {
        String testDocData = "<html><head><title>Test Document</title></head><body><h1>Test</h1></body></html>";
        ByteBuffer testByteData = ByteBuffer.wrap(testDocData.getBytes(Charset.forName(testCharset)));
        Parser testParser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(testByteData, "INVALID_CHARSET", testBaseUri, testParser);
        assertNotNull(doc);
        assertEquals(testBaseUri, doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer_Valid() throws IOException {
        // Mocking read from input stream
        byte[] testBytes = "Test Input Stream".getBytes();
        when(mockInputStream.read(any(byte[].class))).thenReturn(testBytes.length);
        when(mockInputStream.read(any(byte[].class))).thenReturn(-1);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(mockInputStream);
        assertNotNull(byteBuffer);
        assertEquals(testBytes.length, byteBuffer.capacity());
    }

    @Test
    public void testReadToByteBuffer_CappedSize() throws IOException {
        // Mocking read from input stream
        byte[] testBytes = "Test Input Stream".getBytes();
        when(mockInputStream.read(any(byte[].class))).thenReturn(testBytes.length);
        when(mockInputStream.read(any(byte[].class))).thenReturn(-1);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(mockInputStream, 10);
        assertNotNull(byteBuffer);
        assertEquals(10, byteBuffer.capacity());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBuffer_IOException() throws IOException {
        // Mocking read from input stream to throw IOException
        when(mockInputStream.read(any(byte[].class))).thenThrow(new IOException());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(mockInputStream);
    }

    @Test
    public void testGetCharsetFromContentType_ValidCharset() {
        String testContentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(testContentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String testContentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(testContentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidCharset() {
        String testContentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(testContentType);
        assertNull(charset);
    }

}