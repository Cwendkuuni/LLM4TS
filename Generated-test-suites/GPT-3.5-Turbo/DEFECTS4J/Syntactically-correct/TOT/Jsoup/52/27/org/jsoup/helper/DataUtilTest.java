
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;
import org.jsoup.nodes.Document;

public class DataUtilTest {

    private static File file;
    private static InputStream inputStream;
    private static String charsetName = "UTF-8";
    private static String baseUri = "http://www.example.com";

    @BeforeClass
    public static void setUp() {
        file = new File("test.html");

        // create a sample file for testing
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("<html><body><h1>This is a test</h1></body></html>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithFile() {
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals("This is a test", doc.body().select("h1").text());
        } catch (IOException e) {
            fail("IOException caught while loading file");
        }
    }

    @Test
    public void testLoadWithInputStream() {
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals("This is a test", doc.body().select("h1").text());
        } catch (IOException e) {
            fail("IOException caught while loading input stream");
        }
    }

    @Test
    public void testLoadWithParser() {
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
            assertNotNull(doc);
            assertEquals("This is a test", doc.body().select("h1").text());
        } catch (IOException e) {
            fail("IOException caught while loading input stream with parser");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new ByteArrayInputStream("Test String".getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            assertEquals("Test String", out.toString());
        } catch (IOException e) {
            fail("IOException caught while crossing streams");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>This is a test</h1></body></html>".getBytes());
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("This is a test", doc.body().select("h1").text());
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);
            assertNotNull(byteBuffer);
            assertEquals(45, byteBuffer.remaining());
        } catch (IOException e) {
            fail("IOException caught while reading input stream to byte buffer");
        }
    }

    @Test
    public void testReadToByteBufferUnlimited() {
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.remaining() >= 45);
        } catch (IOException e) {
            fail("IOException caught while reading input stream to byte buffer with unlimited size");
        }
    }

    @Test
    public void testReadToByteBufferFromFile() {
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
            assertEquals(45, byteBuffer.remaining());
        } catch (IOException e) {
            fail("IOException caught while reading file to byte buffer");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 65, 66, 67 });

        String charsetName = DataUtil.detectCharsetFromBom(byteData, "UTF-8");

        assertNotNull(charsetName);
        assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testDetectCharsetFromBomNoBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 65, 66, 67 });

        String charsetName = DataUtil.detectCharsetFromBom(byteData, "UTF-8");

        assertNotNull(charsetName);
        assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testDetectCharsetFromBomInvalidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 65, 66, 67 });

        String charsetName = DataUtil.detectCharsetFromBom(byteData, "INVALID_CHARSET");

        assertNotNull(charsetName);
        assertEquals("INVALID_CHARSET", charsetName);
    }
}

