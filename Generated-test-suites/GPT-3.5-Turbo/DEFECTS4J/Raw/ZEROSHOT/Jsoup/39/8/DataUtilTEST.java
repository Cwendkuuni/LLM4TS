import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        
        try {
            DataUtil.load(file, "UTF-8", "http://example.com");
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        
        try {
            DataUtil.load(inputStream, "UTF-8", "http://example.com");
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        
        try {
            DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testParseByteData() {
        Parser parser = Parser.htmlParser();
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String docData = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        
        byte[] byteData = docData.getBytes(Charset.forName(charsetName));
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
        
        Document doc = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testReadToByteBuffer() {
        byte[] testData = new byte[1024];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte) i;
        }
        
        InputStream inputStream = new ByteArrayInputStream(testData);
        
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            
            assertNotNull(byteBuffer);
            assertEquals(1024, byteBuffer.capacity());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadToByteBufferUnlimited() {
        byte[] testData = new byte[2048];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte) i;
        }
        
        InputStream inputStream = new ByteArrayInputStream(testData);
        
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            
            assertNotNull(byteBuffer);
            assertEquals(2048, byteBuffer.capacity());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        byte[] testData = new byte[2048];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte) i;
        }
        
        InputStream inputStream = new ByteArrayInputStream(testData);
        
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            
            assertNotNull(byteBuffer);
            assertEquals(1024, byteBuffer.capacity());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String expectedCharset1 = "UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";
        String expectedCharset2 = "ISO-8859-1";
        String contentType3 = "text/html; charset=UTF-16";
        String expectedCharset3 = "UTF-16";
        
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        
        assertEquals(expectedCharset1, charset1);
        assertEquals(expectedCharset2, charset2);
        assertEquals(expectedCharset3, charset3);
    }
}