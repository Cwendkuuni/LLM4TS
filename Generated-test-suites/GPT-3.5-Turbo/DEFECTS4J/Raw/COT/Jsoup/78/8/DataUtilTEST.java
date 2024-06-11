import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            String file = "test.html";
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(new File(file), charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(in, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream in = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.load(in, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new FileInputStream("input.txt");
            OutputStream out = new FileOutputStream("output.txt");
            DataUtil.crossStreams(in, out);
            in.close();
            out.close();
            File outputFile = new File("output.txt");
            assertTrue(outputFile.exists());
            outputFile.delete();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.parseInputStream(in, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream in = new FileInputStream("test.html");
            ByteBuffer buffer = DataUtil.readToByteBuffer(in, 1024);
            assertNotNull(buffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("test.html");
            ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(buffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertNotNull(buffer);
        assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
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
        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream("test.html"), 4);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }
}