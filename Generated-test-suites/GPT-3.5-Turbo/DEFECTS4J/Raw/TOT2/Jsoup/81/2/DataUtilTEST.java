import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    private File testFile;
    private String baseURI;

    @Before
    public void setUp() {
        testFile = new File("test/test.html");
        baseURI = "http://www.example.com";
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = DataUtil.load(testFile, "UTF-8", baseURI);
        assertNotNull(doc);
        assertEquals(baseURI, doc.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        FileInputStream in = new FileInputStream(testFile);
        Document doc = DataUtil.load(in, "UTF-8", baseURI);
        in.close();
        assertNotNull(doc);
        assertEquals(baseURI, doc.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        FileInputStream in = new FileInputStream(testFile);
        Document doc = DataUtil.load(in, "UTF-8", baseURI, Parser.xmlParser());
        in.close();
        assertNotNull(doc);
        assertEquals(baseURI, doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testStr = "Test String";
        InputStream in = new StringReader(testStr);
        StringBuilder out = new StringBuilder();
        DataUtil.crossStreams(in, out);
        assertEquals(testStr, out.toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream in = new FileInputStream(testFile);
        int maxSize = 1024;
        ByteBuffer buffer = DataUtil.readToByteBuffer(in, maxSize);
        assertNotNull(buffer);
        assertEquals(maxSize, buffer.limit());
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        FileInputStream in = new FileInputStream(testFile);
        ByteBuffer buffer = DataUtil.readToByteBuffer(in);
        assertNotNull(buffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(testFile);
        assertNotNull(buffer);
        assertTrue(buffer.limit() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertNotNull(buffer);
        assertEquals(0, buffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);
    }

}