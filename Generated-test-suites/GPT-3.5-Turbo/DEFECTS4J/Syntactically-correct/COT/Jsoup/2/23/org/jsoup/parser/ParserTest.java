import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParse_WithComment() {
        String html = "<!-- Test Comment --><html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParse_WithCDATA() {
        String html = "<![CDATA[<h1>Hello World!</h1>]]><html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParse_WithXmlDeclaration() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParse_WithEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html></body>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParse_WithInvalidStartTag() {
        String html = "<html><head><title><h1>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParse_WithAttribute() {
        String html = "<html lang=\"en\"><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("en", doc.select("html").attr("lang"));
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParse_WithInnerText() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1>Some text</body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World! Some text", doc.body().text());
    }

    @Test
    public void testParse_WithCDATAInInnerText() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1><![CDATA[Some text with <tags>]]></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World! Some text with <tags>", doc.body().text());
    }

    @Test
    public void testParse_WithBaseHref() {
        String html = "<html><head><title>Test</title><base href=\"https://example.com/\"></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assert statements to test the functionality of the method
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://example.com/", doc.baseUri());
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World!", doc.body().select("h1").text());
    }

}