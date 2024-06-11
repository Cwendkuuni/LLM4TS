import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        String expectedCharsetName = "UTF-8";
        String expectedBaseUri = "http://example.com";
        
        Document doc = DataUtil.load(testFile, expectedCharsetName, expectedBaseUri);
        
        assertNotNull(doc);
        assertEquals(expectedBaseUri, doc.baseUri());
        // Add more assertions based on expected content of the document
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        File testFile = new File("test.html");
        String expectedCharsetName = "UTF-8";
        String expectedBaseUri = "http://example.com";

        FileInputStream inStream = new FileInputStream(testFile);
        
        Document doc = DataUtil.load(inStream, expectedCharsetName, expectedBaseUri);
        
        assertNotNull(doc);
        assertEquals(expectedBaseUri, doc.baseUri());
        // Add more assertions based on expected content of the document
        
        inStream.close();
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        File testFile = new File("test.xml");
        String expectedCharsetName = "UTF-8";
        String expectedBaseUri = "http://example.com";
        Parser expectedParser = Parser.xmlParser();
        
        FileInputStream inStream = new FileInputStream(testFile);
        
        Document doc = DataUtil.load(inStream, expectedCharsetName, expectedBaseUri, expectedParser);
        
        assertNotNull(doc);
        assertEquals(expectedBaseUri, doc.baseUri());
        // Add more assertions based on expected content of the document
        
        inStream.close();
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String expectedCharsetName = "UTF-8";
        String expectedBaseUri = "http://example.com";
        Parser expectedParser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, expectedCharsetName, expectedBaseUri, expectedParser);
        
        assertNotNull(doc);
        assertEquals(expectedBaseUri, doc.baseUri());
        // Add more assertions based on expected content of the document
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inStream = new FileInputStream("test.txt");
        int maxSize = 50;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        
        assertNotNull(byteData);
        assertTrue(byteData.limit() <= maxSize);
        
        inStream.close();
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("test.txt");

        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        
        assertNotNull(byteData);
        // Add more assertions based on the content of the ByteBuffer
        
        inStream.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=utf-8";
        String expectedCharset = "UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals(expectedCharset, charset);
    }
}