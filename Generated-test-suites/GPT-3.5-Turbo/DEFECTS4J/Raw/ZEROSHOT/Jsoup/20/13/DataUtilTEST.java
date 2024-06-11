import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.*;

public class DataUtilTest {
    
    private DataUtil dataUtil;
    
    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }
    
    @Test
    public void testLoadFile() throws IOException {
        File in = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = dataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        FileInputStream in = new FileInputStream(new File("test.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = dataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        in.close();
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        FileInputStream in = new FileInputStream(new File("test.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = dataUtil.load(in, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        in.close();
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset().name());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream in = new FileInputStream(new File("test.html"));
        ByteBuffer byteData = dataUtil.readToByteBuffer(in);
        assertNotNull(byteData);
        in.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}