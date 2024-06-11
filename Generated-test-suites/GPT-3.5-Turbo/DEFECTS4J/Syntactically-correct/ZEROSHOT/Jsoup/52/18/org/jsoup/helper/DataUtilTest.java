import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class DataUtilTest {

    @Mock
    private File file;

    @Mock
    private InputStream inputStream;

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        try {
            Document document = dataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        try {
            Document document = dataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        try {
            Document document = dataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            dataUtil.crossStreams(inputStream, outputStream);
            assertNotNull(outputStream);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        int maxSize = 10;

        try {
            ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            ByteBuffer byteBuffer = dataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = dataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}