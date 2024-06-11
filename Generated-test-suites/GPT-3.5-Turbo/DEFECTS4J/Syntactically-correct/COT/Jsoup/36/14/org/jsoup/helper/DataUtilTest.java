import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;

public class DataUtilTest {
    
    @Test
    public void testLoad_file() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testLoad_inputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }
    
    @Test
    public void testLoad_inputStream_parser() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }
    
    @Test(expected = IOException.class)
    public void testLoad_file_exception() throws IOException {
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        DataUtil.load(file, charsetName, baseUri);
    }
    
    @Test(expected = IOException.class)
    public void testLoad_inputStream_exception() throws IOException {
        InputStream inputStream = new FileInputStream("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }
    
    @Test(expected = IOException.class)
    public void testLoad_inputStream_parser_exception() throws IOException {
        InputStream inputStream = new FileInputStream("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();
        DataUtil.load(inputStream, charsetName, baseUri, parser);
    }
    
    @Test(expected = IOException.class)
    public void testParseByteData_exception() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    }
}