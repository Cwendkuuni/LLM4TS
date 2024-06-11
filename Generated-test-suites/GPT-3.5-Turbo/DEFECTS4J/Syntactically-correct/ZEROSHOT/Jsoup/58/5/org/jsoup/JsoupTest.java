import org.junit.Test;
import org.jsoup.Jsoup;

public class JsoupTest {
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Jsoup.parse(html, baseUri);
    }
    
    @Test
    public void testParseWithStringAndBaseUriAndParser() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Jsoup.parse(html, baseUri, parser);
    }
    
    @Test
    public void testParseWithString() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        
        Jsoup.parse(html);
    }
    
    @Test
    public void testConnect() {
        String url = "http://www.example.com";
        
        Jsoup.connect(url);
    }
    
    @Test
    public void testParseWithFileAndCharsetNameAndBaseUri() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Jsoup.parse(file, charsetName, baseUri);
    }
    
    @Test
    public void testParseWithFileAndCharsetName() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        
        Jsoup.parse(file, charsetName);
    }
    
    @Test
    public void testParseWithInputStreamAndCharsetNameAndBaseUri() throws IOException {
        InputStream in = new FileInputStream("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Jsoup.parse(in, charsetName, baseUri);
    }
    
    @Test
    public void testParseWithInputStreamAndCharsetNameAndBaseUriAndParser() throws IOException {
        InputStream in = new FileInputStream("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Jsoup.parse(in, charsetName, baseUri, parser);
    }
    
    @Test
    public void testParseBodyFragmentWithStringAndBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";
        
        Jsoup.parseBodyFragment(bodyHtml, baseUri);
    }
    
    @Test
    public void testParseBodyFragmentWithString() {
        String bodyHtml = "<h1>Hello World!</h1>";
        
        Jsoup.parseBodyFragment(bodyHtml);
    }
    
    @Test
    public void testParseWithURLAndTimeoutMillis() throws IOException {
        URL url = new URL("http://www.example.com");
        int timeoutMillis = 5000;
        
        Jsoup.parse(url, timeoutMillis);
    }
    
    @Test
    public void testCleanWithStringAndBaseUriAndWhitelist() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        
        Jsoup.clean(bodyHtml, baseUri, whitelist);
    }
    
    @Test
    public void testCleanWithStringAndWhitelist() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        Jsoup.clean(bodyHtml, whitelist);
    }
    
    @Test
    public void testCleanWithStringAndBaseUriAndWhitelistAndOutputSettings() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
    }
    
    @Test
    public void testIsValidWithStringAndWhitelist() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        Jsoup.isValid(bodyHtml, whitelist);
    }
}