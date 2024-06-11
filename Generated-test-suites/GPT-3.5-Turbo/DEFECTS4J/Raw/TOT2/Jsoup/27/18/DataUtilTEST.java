import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("Document base URI should match", baseUri, document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "ISO-8859-1";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("Document base URI should match", baseUri, document.baseUri());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());
        
        assertNotNull(document);
        assertEquals("Document base URI should match", baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = "Test data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteBuffer);
        assertEquals("Byte buffer should contain correct number of bytes", data.length, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("Charset should match", "UTF-8", charset);
    }

}