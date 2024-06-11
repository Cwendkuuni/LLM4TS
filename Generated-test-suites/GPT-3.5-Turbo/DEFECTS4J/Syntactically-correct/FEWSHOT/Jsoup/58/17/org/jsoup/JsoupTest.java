import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsoupTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertTrue(doc.title().equals("Test"));
        assertTrue(doc.baseUri().equals(baseUri));
    }
    
    @Test
    public void testParseWithParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertTrue(doc.title().equals("Test"));
        assertTrue(doc.baseUri().equals(baseUri));
    }
    
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertTrue(doc.title().equals("Test"));
        assertTrue(doc.baseUri().equals(""));
    }
    
    @Test
    public void testConnect() {
        String url = "https://www.example.com";
        Connection con = Jsoup.connect(url);
        assertTrue(con.request().url().equals(url));
    }
    
    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        assertTrue(doc.baseUri().equals(baseUri));
    }
    
    @Test
    public void testParseFromFileWithoutBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(file, charsetName);
        assertTrue(doc.baseUri().equals(file.getAbsolutePath()));
    }
    
    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        assertTrue(doc.baseUri().equals(baseUri));
    }
    
    @Test
    public void testParseFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(in, charsetName, baseUri, parser);
        assertTrue(doc.baseUri().equals(baseUri));
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertTrue(doc.baseUri().equals(baseUri));
    }
    
    @Test
    public void testClean() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertTrue(cleanHtml.equals("<h1>Hello World!</h1>"));
    }
    
    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertTrue(cleanHtml.equals("<h1>Hello World!</h1>"));
    }
    
    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertTrue(cleanHtml.equals("<h1>\n Hello World!\n</h1>"));
    }
    
    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
}