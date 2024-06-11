import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {
    
    @Test
    public void parseTest() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(html, baseUri);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void parseTestWithParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = Jsoup.parse(html, baseUri, parser);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void parseTestWithoutBaseUri() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        
        Document doc = Jsoup.parse(html);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void connectTest() {
        String url = "http://example.com";
        
        Connection connection = Jsoup.connect(url);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void parseFileTest() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void parseFileWithoutBaseUriTest() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        
        Document doc = Jsoup.parse(file, charsetName);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void parseInputStreamTest() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void parseInputStreamWithParserTest() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = Jsoup.parse(in, charsetName, baseUri, parser);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void parseBodyFragmentTest() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void parseBodyFragmentWithoutBaseUriTest() {
        String bodyHtml = "<h1>Hello World!</h1>";
        
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void parseUrlTest() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        
        Document doc = Jsoup.parse(url, timeoutMillis);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void cleanTest() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void cleanTestWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void cleanTestWithOutputSettings() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        // Perform assertions
        // ...
    }
    
    @Test
    public void isValidTest() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        // Perform assertions
        // ...
    }
}