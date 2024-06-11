import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private Document document;

    @Before
    public void setUp() throws IOException {
        File file = new File("test.html");
        document = DataUtil.load(file, "UTF-8", "");
    }

    @Test
    public void testLoadFile() throws IOException {
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("test.html");
        assertNotNull(inputStream);
        document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("test.html");
        assertNotNull(inputStream);
        document = DataUtil.load(inputStream, "UTF-8", "", Jsoup.parser());
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("test.html");
        assertNotNull(inputStream);
        DataUtil.crossStreams(inputStream, System.out);
        inputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("test.html");
        assertNotNull(inputStream);
        document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Jsoup.parser());
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("test.html");
        assertNotNull(inputStream);
        assertNotNull(DataUtil.readToByteBuffer(inputStream, 0));
        inputStream.close();
    }

    @Test
    public void testMimeBoundary() {
        assertNotNull(DataUtil.mimeBoundary());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));
    }
}