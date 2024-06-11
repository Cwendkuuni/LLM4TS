import org.junit.Test;
import org.jsoup.helper.DataUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        assertNotNull(DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStream() throws Exception {
        InputStream inputStream = new FileInputStream("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        assertNotNull(DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        InputStream inputStream = new FileInputStream("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        assertNotNull(DataUtil.load(inputStream, charsetName, baseUri, new Parser()));
    }

    @Test
    public void testCrossStreams() throws Exception {
        InputStream inputStream = new FileInputStream("testInput.txt");
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertNotNull(outputStream);
    }

    @Test
    public void testParseByteData() throws Exception {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File("testFile.txt"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        assertNotNull(DataUtil.parseByteData(byteBuffer, charsetName, baseUri, new Parser()));
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testMimeBoundary() throws Exception {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

}