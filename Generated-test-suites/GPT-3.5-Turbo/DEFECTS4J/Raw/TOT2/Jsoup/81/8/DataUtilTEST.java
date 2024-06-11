import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals("Title of the document", doc.title());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(in, charsetName, baseUri);

        assertNotNull(doc);
        assertEquals("Title of the document", doc.title());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.load(in, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("Title of the document", doc.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, 1000);

        assertNotNull(byteBuffer);
        assertEquals(1000, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}