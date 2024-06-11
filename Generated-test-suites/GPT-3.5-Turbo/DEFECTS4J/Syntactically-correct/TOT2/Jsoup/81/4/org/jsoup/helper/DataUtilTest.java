import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    private File testFile;
    private String baseUri;
    private DataUtil dataUtil;

    @Before
    public void setUp() {
        testFile = new File("test.html");
        baseUri = "https://www.example.com";
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = dataUtil.load(testFile, "UTF-8", baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream(testFile);
        Document doc = dataUtil.load(in, "UTF-8", baseUri);
        Assert.assertNotNull(doc);
        in.close();
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream(testFile);
        Document doc = dataUtil.load(in, "UTF-8", baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
        in.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream(testFile);
        OutputStream out = new ByteArrayOutputStream();
        dataUtil.crossStreams(in, out);
        Assert.assertTrue(out.toByteArray().length > 0);
        in.close();
        out.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream in = new FileInputStream(testFile);
        ByteBuffer buffer = dataUtil.readToByteBuffer(in, 1024);
        Assert.assertTrue(buffer.capacity() > 0);
        in.close();
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream in = new FileInputStream(testFile);
        ByteBuffer buffer = dataUtil.readToByteBuffer(in);
        Assert.assertTrue(buffer.capacity() > 0);
        in.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer buffer = dataUtil.readFileToByteBuffer(testFile);
        Assert.assertTrue(buffer.capacity() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = dataUtil.emptyByteBuffer();
        Assert.assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

}