import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataUtilTest {

    private String baseUri;

    @Before
    public void setUp() {
        baseUri = "https://example.com";
    }

    @Test
    public void testLoad_FileAndCharsetName_ValidResult() throws Exception {
        String charsetName = "UTF-8";
        File file = new File("test.html");

        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(doc);
    }

    @Test
    public void testLoad_InputStreamAndCharsetName_ValidResult() throws Exception {
        String charsetName = "UTF-8";
        InputStream in = getClass().getResourceAsStream("test.html");

        Document doc = DataUtil.load(in, charsetName, baseUri);

        assertNotNull(doc);
    }

    @Test
    public void testLoad_InputStreamCharsetNameAndParser_ValidResult() throws Exception {
        String charsetName = "UTF-8";
        InputStream in = getClass().getResourceAsStream("test.html");
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(in, charsetName, baseUri, parser);

        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams_ValidInput_ValidOutput() throws Exception {
        InputStream in = getClass().getResourceAsStream("input.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(in, out);

        byte[] expectedOutput = "This is a test input for crossStreams method.".getBytes();
        byte[] actualOutput = out.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseByteData_BOMAndCharset_ValidOutput() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xFE, (byte) 0xFF});
        String charsetName = "UTF-8";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset().name().toUpperCase());
    }

    @Test
    public void testParseByteData_WithoutBOMAndCharset_ValidOutput() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap("This is a test document.".getBytes());
        String charsetName = "UTF-8";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset().name().toUpperCase());
    }

    @Test
    public void testReadToByteBuffer_MaxSizeUnlimited_ValidOutput() throws Exception {
        InputStream inStream = getClass().getResourceAsStream("input.txt");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, 0);

        assertNotNull(byteBuffer);
        assertEquals(byteBuffer.capacity(), 48);
    }

    @Test
    public void testReadToByteBuffer_MaxSizeCapped_ValidOutput() throws Exception {
        InputStream inStream = getClass().getResourceAsStream("input.txt");
        int maxSize = 16;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);

        assertNotNull(byteBuffer);
        assertEquals(byteBuffer.capacity(), 16);
    }

    @Test
    public void testReadToByteBuffer_WithoutMaxSize_ValidOutput() throws Exception {
        InputStream inStream = getClass().getResourceAsStream("input.txt");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);

        assertNotNull(byteBuffer);
        assertEquals(byteBuffer.capacity(), 48);
    }

    @Test
    public void testReadFileToByteBuffer_ValidFile_ValidOutput() throws Exception {
        File file = new File("test.txt");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        assertNotNull(byteBuffer);
        assertEquals(byteBuffer.capacity(), 5);
    }

    @Test
    public void testEmptyByteBuffer_ValidInput_ValidOutput() throws Exception {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(byteBuffer.capacity(), 0);
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType_ValidOutput() throws Exception {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testMimeBoundary_ValidResult() throws Exception {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertEquals(boundary.length(), 32);
    }
}