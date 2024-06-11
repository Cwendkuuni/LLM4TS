import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.txt");
        InputStream inputStream = new FileInputStream(file);
        
        int maxSize = 1024;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}