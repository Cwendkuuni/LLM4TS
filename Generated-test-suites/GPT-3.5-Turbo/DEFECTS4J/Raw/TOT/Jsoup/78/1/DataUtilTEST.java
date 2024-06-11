import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class DataUtilTest {
    private static final String BASE_URI = "http://example.com";
    
    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        Document doc;
        try {
            doc = DataUtil.load(file, "UTF-8", BASE_URI);
            Assertions.assertNotNull(doc);
            Assertions.assertEquals(BASE_URI, doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document doc;
        try {
            doc = DataUtil.load(inputStream, "UTF-8", BASE_URI);
            Assertions.assertNotNull(doc);
            Assertions.assertEquals(BASE_URI, doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        Document doc;
        try {
            doc = DataUtil.load(inputStream, "UTF-8", BASE_URI, Parser.xmlParser());
            Assertions.assertNotNull(doc);
            Assertions.assertEquals(BASE_URI, doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCrossStreams() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test input".getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(byteArrayInputStream, byteArrayOutputStream);
            ByteArrayInputStream resultInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            BufferedReader reader = new BufferedReader(new InputStreamReader(resultInputStream));
            String line = reader.readLine();
            Assertions.assertEquals("test input", line);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document doc;
        try {
            doc = DataUtil.parseInputStream(inputStream, "UTF-8", BASE_URI, Parser.htmlParser());
            Assertions.assertNotNull(doc);
            Assertions.assertEquals(BASE_URI, doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            Assertions.assertNotNull(byteBuffer);
            Assertions.assertTrue(byteBuffer.hasRemaining());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            Assertions.assertNotNull(byteBuffer);
            Assertions.assertTrue(byteBuffer.hasRemaining());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.html");
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            Assertions.assertNotNull(byteBuffer);
            Assertions.assertTrue(byteBuffer.hasRemaining());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assertions.assertNotNull(byteBuffer);
        Assertions.assertFalse(byteBuffer.hasRemaining());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assertions.assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testInvalidContentType() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assertions.assertNull(charset);
    }
    
    @Test
    public void testValidateCharset() {
        String invalidCharset = "invalid";
        String validCharset = DataUtil.validateCharset(invalidCharset);
        Assertions.assertNull(validCharset);
        
        String supportedCharset = "UTF-8";
        String supportedCharsetResult = DataUtil.validateCharset(supportedCharset);
        Assertions.assertEquals("UTF-8", supportedCharsetResult);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assertions.assertNotNull(mimeBoundary);
        Assertions.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bom = new byte[] {
            (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assertions.assertNotNull(bomCharset);
        Assertions.assertEquals("UTF-8", bomCharset.charset);
        Assertions.assertEquals(3, bomCharset.offset);
    }
}