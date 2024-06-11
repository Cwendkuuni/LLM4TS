import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {
    
    private Document doc;
    private String baseUri;
    
    @Before
    public void setUp() {
        doc = null;
        baseUri = "https://example.com/";
    }
    
    @Test
    public void testLoadFromFile() {
        File file = new File("path/to/file.html");
        try {
            doc = DataUtil.load(file, "UTF-8", baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred while loading file from file system");
        }
    }
    
    @Test
    public void testLoadFromInputStream() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        
        try {
            doc = DataUtil.load(in, "UTF-8", baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred while loading file from InputStream");
        }
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.htmlParser();
        
        try {
            doc = DataUtil.load(in, "UTF-8", baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred while loading file from InputStream with Parser");
        }
    }
    
    @Test
    public void testParseByteData() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        
        doc = DataUtil.parseByteData(byteData, "UTF-8", baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }
    
    @Test
    public void testReadToByteBuffer() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred while reading InputStream to ByteBuffer");
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}