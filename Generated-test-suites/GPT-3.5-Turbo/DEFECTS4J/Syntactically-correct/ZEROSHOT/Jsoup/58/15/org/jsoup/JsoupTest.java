import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        // Test case 1
        String html1 = "<html><body>This is a test</body></html>";
        String baseUri1 = "http://example.com";
        Document doc1 = Jsoup.parse(html1, baseUri1);
        assertNotNull(doc1);
        assertEquals("This is a test", doc1.body().text());
        
        // Test case 2
        String html2 = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri2 = "http://example.com";
        Document doc2 = Jsoup.parse(html2, baseUri2);
        assertNotNull(doc2);
        assertEquals("Hello World", doc2.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithParser() {
        // Test case 1
        String html1 = "<html><body>This is a test</body></html>";
        String baseUri1 = "http://example.com";
        Parser parser1 = new Parser();
        Document doc1 = Jsoup.parse(html1, baseUri1, parser1);
        assertNotNull(doc1);
        assertEquals("This is a test", doc1.body().text());
        
        // Test case 2
        String html2 = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri2 = "http://example.com";
        Parser parser2 = new Parser();
        Document doc2 = Jsoup.parse(html2, baseUri2, parser2);
        assertNotNull(doc2);
        assertEquals("Hello World", doc2.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        // Test case 1
        String html1 = "<html><body>This is a test</body></html>";
        Document doc1 = Jsoup.parse(html1);
        assertNotNull(doc1);
        assertEquals("This is a test", doc1.body().text());
        
        // Test case 2
        String html2 = "<html><body><h1>Hello World</h1></body></html>";
        Document doc2 = Jsoup.parse(html2);
        assertNotNull(doc2);
        assertEquals("Hello World", doc2.body().getElementsByTag("h1").text());
    }

    @Test
    public void testConnect() {
        // Test case 1
        String url1 = "http://example.com";
        Connection connection1 = Jsoup.connect(url1);
        assertNotNull(connection1);

        // Test case 2
        String url2 = "http://example.com";
        Document doc2 = Jsoup.connect(url2).userAgent("Mozilla").data("name", "jsoup").get();
        assertNotNull(doc2);
    }

    @Test
    public void testParseFile() throws IOException {
        // Test case 1
        File file1 = new File("test.html");
        String charsetName1 = "UTF-8";
        String baseUri1 = "http://example.com";
        Document doc1 = Jsoup.parse(file1, charsetName1, baseUri1);
        assertNotNull(doc1);
        
        // Test case 2
        File file2 = new File("test.html");
        String charsetName2 = "UTF-8";
        Document doc2 = Jsoup.parse(file2, charsetName2);
        assertNotNull(doc2);
    }

    @Test
    public void testParseInputStream() throws IOException {
        // Test case 1
        InputStream in1 = new FileInputStream("test.html");
        String charsetName1 = "UTF-8";
        String baseUri1 = "http://example.com";
        Document doc1 = Jsoup.parse(in1, charsetName1, baseUri1);
        assertNotNull(doc1);

        // Test case 2
        InputStream in2 = new FileInputStream("test.html");
        String charsetName2 = "UTF-8";
        String baseUri2 = "http://example.com";
        Parser parser2 = new Parser();
        Document doc2 = Jsoup.parse(in2, charsetName2, baseUri2, parser2);
        assertNotNull(doc2);
    }

    @Test
    public void testParseBodyFragment() {
        // Test case 1
        String bodyHtml1 = "<h1>Hello World</h1>";
        String baseUri1 = "http://example.com";
        Document doc1 = Jsoup.parseBodyFragment(bodyHtml1, baseUri1);
        assertNotNull(doc1);
        assertEquals("Hello World", doc1.body().getElementsByTag("h1").text());
        
        // Test case 2
        String bodyHtml2 = "<p>This is a test</p>";
        Document doc2 = Jsoup.parseBodyFragment(bodyHtml2);
        assertNotNull(doc2);
        assertEquals("This is a test", doc2.body().getElementsByTag("p").text());
    }

    @Test
    public void testClean() {
        // Test case 1
        String bodyHtml1 = "<div><script>alert('Hello World!');</script></div>";
        String baseUri1 = "http://example.com";
        Whitelist whitelist1 = Whitelist.basic();
        String cleanHtml1 = Jsoup.clean(bodyHtml1, baseUri1, whitelist1);
        assertNotNull(cleanHtml1);
        assertEquals("<div></div>", cleanHtml1);
        
        // Test case 2
        String bodyHtml2 = "<div><p>This is a test</p></div>";
        Whitelist whitelist2 = Whitelist.basic();
        String cleanHtml2 = Jsoup.clean(bodyHtml2, whitelist2);
        assertNotNull(cleanHtml2);
        assertEquals("<div><p>This is a test</p></div>", cleanHtml2);
    }

    @Test
    public void testCleanWithOutputSettings() {
        // Test case 1
        String bodyHtml1 = "<div><script>alert('Hello World!');</script></div>";
        String baseUri1 = "http://example.com";
        Whitelist whitelist1 = Whitelist.basic();
        Document.OutputSettings outputSettings1 = new Document.OutputSettings();
        String cleanHtml1 = Jsoup.clean(bodyHtml1, baseUri1, whitelist1, outputSettings1);
        assertNotNull(cleanHtml1);
        assertEquals("<div></div>", cleanHtml1);
        
        // Test case 2
        String bodyHtml2 = "<div><p>This is a test</p></div>";
        Whitelist whitelist2 = Whitelist.basic();
        Document.OutputSettings outputSettings2 = new Document.OutputSettings();
        String cleanHtml2 = Jsoup.clean(bodyHtml2, whitelist2, outputSettings2);
        assertNotNull(cleanHtml2);
        assertEquals("<div><p>This is a test</p></div>", cleanHtml2);
    }

    @Test
    public void testIsValid() {
        // Test case 1
        String bodyHtml1 = "<div><script>alert('Hello World!');</script></div>";
        Whitelist whitelist1 = Whitelist.basic();
        boolean isValid1 = Jsoup.isValid(bodyHtml1, whitelist1);
        assertFalse(isValid1);
        
        // Test case 2
        String bodyHtml2 = "<div><p>This is a test</p></div>";
        Whitelist whitelist2 = Whitelist.basic();
        boolean isValid2 = Jsoup.isValid(bodyHtml2, whitelist2);
        assertTrue(isValid2);
    }
}