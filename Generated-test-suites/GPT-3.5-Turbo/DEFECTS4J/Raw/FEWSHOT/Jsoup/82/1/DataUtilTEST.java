import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() {
        try {
            Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            FileInputStream fis = new FileInputStream("test.html");
            Document doc = DataUtil.load(fis, "UTF-8", "http://example.com");
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            FileInputStream fis = new FileInputStream("test.xml");
            Document doc = DataUtil.load(fis, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
            assertEquals("xml", doc.childNode(0).nodeName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            FileInputStream fis = new FileInputStream("input.txt");
            FileOutputStream fos = new FileOutputStream("output.txt");
            DataUtil.crossStreams(fis, fos);

            File inputFile = new File("input.txt");
            File outputFile = new File("output.txt");
            assertTrue(inputFile.exists());
            assertTrue(outputFile.exists());
            assertEquals(inputFile.length(), outputFile.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            FileInputStream fis = new FileInputStream("test.html");
            Document doc = DataUtil.parseInputStream(fis, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            FileInputStream fis = new FileInputStream("test.html");
            ByteBuffer buffer = DataUtil.readToByteBuffer(fis, 1024 * 5);
            assertNotNull(buffer);
            assertTrue(buffer.remaining() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);

        contentType = "application/xml";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "utf-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNotNull(validatedCharset);
        assertEquals("UTF-8", validatedCharset);

        charset = "invalidCharset";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteBuffer byteData = ByteBuffer.wrap(bom);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }
}