import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {
    private File testFile;
    private InputStream testInputStream;
    private String testCharset;
    private String testBaseUri;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.html");
        testInputStream = new FileInputStream(testFile);
        testCharset = "UTF-8";
        testBaseUri = "http://example.com";
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = DataUtil.load(testFile, testCharset, testBaseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document doc = DataUtil.load(testInputStream, testCharset, testBaseUri);
        assertNotNull(doc);
        testInputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Document doc = DataUtil.load(testInputStream, testCharset, testBaseUri, Parser.htmlParser());
        assertNotNull(doc);
        testInputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("Hello World".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertEquals("Hello World", out.toString());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body>Hello World</body></html>".getBytes());
        Document doc = DataUtil.parseByteData(byteData, testCharset, testBaseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertNotNull(byteBuffer);
        testInputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertTrue(byteBuffer.capacity() == 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        charset = DataUtil.getCharsetFromContentType("text/html");
        assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == DataUtil.boundaryLength);
    }
}