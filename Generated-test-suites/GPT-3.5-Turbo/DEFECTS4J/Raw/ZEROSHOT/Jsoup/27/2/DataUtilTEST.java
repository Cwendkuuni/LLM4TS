import org.junit.*;
import java.io.*;

public class DataUtilTest {
    
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        // write your assertions here
        
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(in, charsetName, baseUri);
        
        // write your assertions here
        
        in.close();
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        
        // write your assertions here
        
        in.close();
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // write your assertions here
        
        doc.outputSettings().charset(charsetName);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("example.html");
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        
        // write your assertions here
        
        inStream.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // write your assertions here
        
    }
}