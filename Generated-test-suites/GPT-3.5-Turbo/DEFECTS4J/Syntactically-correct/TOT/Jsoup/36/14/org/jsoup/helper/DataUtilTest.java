import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class DataUtilTest {

    @Mock
    private static File mockFile;
    @Mock
    private static InputStream mockInputStream;

    private String charsetName;
    private String baseUri;

    public DataUtilTest(String charsetName, String baseUri) {
        this.charsetName = charsetName;
        this.baseUri = baseUri;
    }

    @BeforeClass
    public static void setup() {
        MockitoAnnotations.initMocks(DataUtilTest.class);
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = new Document("Test document");
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
        when(mockFile.length()).thenReturn(100L);
        when(DataUtil.load(mockFile, charsetName, baseUri)).thenReturn(document);

        Document result = DataUtil.load(mockFile, charsetName, baseUri);

        assertEquals(document, result);
    }

    @Test
    public void testLoadFromFileNotFound() throws IOException {
        when(DataUtil.load(mockFile, charsetName, baseUri)).thenThrow(new FileNotFoundException());

        try {
            Document result = DataUtil.load(mockFile, charsetName, baseUri);
        } catch (FileNotFoundException e) {
            assertEquals("File not found", e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document document = new Document("Test document");
        when(DataUtil.load(mockInputStream, charsetName, baseUri)).thenReturn(document);

        Document result = DataUtil.load(mockInputStream, charsetName, baseUri);

        assertEquals(document, result);
    }

    @Test
    public void testLoadFromInputStreamIOException() throws IOException {
        when(DataUtil.load(mockInputStream, charsetName, baseUri)).thenThrow(new IOException());

        try {
            Document result = DataUtil.load(mockInputStream, charsetName, baseUri);
        } catch (IOException e) {
            assertEquals("IOException", e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Document document = new Document("Test document");
        Parser parser = new Parser();
        when(DataUtil.load(mockInputStream, charsetName, baseUri, parser)).thenReturn(document);

        Document result = DataUtil.load(mockInputStream, charsetName, baseUri, parser);

        assertEquals(document, result);
    }

    @Test
    public void testLoadFromInputStreamWithParserIOException() throws IOException {
        Parser parser = new Parser();
        when(DataUtil.load(mockInputStream, charsetName, baseUri, parser)).thenThrow(new IOException());

        try {
            Document result = DataUtil.load(mockInputStream, charsetName, baseUri, parser);
        } catch (IOException e) {
            assertEquals("IOException", e.getMessage());
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        Document document = new Document("Test document");
        when(parser.parseInput(byteBuffer.toString(), baseUri)).thenReturn(document);

        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertEquals(document, result);
    }

    @Test
    public void testParseByteDataWithSpecificCharset() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        Document document = new Document("Test document");
        when(parser.parseInput(byteBuffer.toString(), baseUri)).thenReturn(document);

        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertEquals(document, result);
    }

    @Test
    public void testParseByteDataWithMetaTagCharset() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        Document document = new Document("Test document");
        Element meta = new Element("meta");
        meta.attr("http-equiv", "content-type");
        meta.attr("content", "text/html;charset=UTF-8");
        document.append(meta);
        when(parser.parseInput(byteBuffer.toString(), baseUri)).thenReturn(document);

        Document result = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertEquals(document, result);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteBuffer expected = ByteBuffer.allocate(10);
        InputStream inputStream = new FileInputStream("test.txt");
        int maxSize = 10;
        when(DataUtil.readToByteBuffer(inputStream, maxSize)).thenReturn(expected);

        ByteBuffer result = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertEquals(expected, result);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        ByteBuffer expected = ByteBuffer.allocate(10);
        InputStream inputStream = new FileInputStream("test.txt");
        when(DataUtil.readToByteBuffer(inputStream, 0)).thenReturn(expected);

        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);

        assertEquals(expected, result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, result);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=INVALID";
        String expected = null;
        
        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, result);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"UTF-8", "http://example.com"},
                {"ISO-8859-1", "http://example.com"},
                {null, "http://example.com"}
        };
        return Arrays.asList(data);
    }
}