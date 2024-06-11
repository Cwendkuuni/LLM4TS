import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, parser);
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
        assertEquals("GET", connection.method());
        assertEquals(url, connection.url().toString());
    }
    
    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testParseFileWithoutBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals("<body>\n <h1>Hello World!</h1>\n</body>", document.toString());
    }
    
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
        assertEquals("<body>\n <h1>Hello World!</h1>\n</body>", document.toString());
    }
    
    @Test
    public void testClean() {
        String bodyHtml = "<p>Untrusted HTML</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertEquals("<p>Untrusted HTML</p>", cleanHtml);
    }
    
    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<p>Untrusted HTML</p>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanHtml);
        assertEquals("<p>Untrusted HTML</p>", cleanHtml);
    }
    
    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p>Untrusted HTML</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertNotNull(cleanHtml);
        assertEquals("<p>Untrusted HTML</p>", cleanHtml);
    }
    
    @Test
    public void testIsValid() {
        String bodyHtml = "<p>Untrusted HTML</p>";
        Whitelist whitelist = Whitelist.basic();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(valid);
    }

}