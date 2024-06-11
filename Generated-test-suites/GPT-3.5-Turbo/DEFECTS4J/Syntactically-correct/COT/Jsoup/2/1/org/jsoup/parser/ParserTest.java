
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        String title = doc.title();

        Assert.assertEquals("Test", title);

        String body = doc.body().text();

        Assert.assertEquals("Hello World!", body);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        String body = doc.body().text();

        Assert.assertEquals("Hello World!", body);
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        String comment = doc.body().childNode(0).outerHtml();

        Assert.assertEquals("<!-- This is a comment -->", comment);
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<h1>Hello World!</h1>]]>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        String cdata = doc.body().childNode(0).outerHtml();

        Assert.assertEquals("<![CDATA[<h1>Hello World!</h1>]]>", cdata);
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        String xmlDecl = doc.childNode(0).outerHtml();

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", xmlDecl);
    }

    @Test
    public void testParseEndTag() {
        String html = "<div>This is a <b>bold</b> statement.</div>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        String divText = doc.body().childNode(0).text();
        String bText = doc.body().childNode(0).childNode(0).text();

        Assert.assertEquals("This is a bold statement.", divText);
        Assert.assertEquals("bold", bText);
    }

    // Add more test methods for other public methods in the Parser class

}

