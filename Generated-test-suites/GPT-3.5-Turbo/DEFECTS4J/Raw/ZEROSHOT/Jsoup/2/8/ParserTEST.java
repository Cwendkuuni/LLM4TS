import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    private String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
    private String baseUri = "http://example.com";

    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        assertEquals("Title", doc.select("h1").text());
        assertEquals("Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        String fragment = "<h1>Title</h1><p>Paragraph</p>";
        Document doc = Parser.parseBodyFragment(fragment, baseUri);
        assertEquals("Title", doc.select("h1").text());
        assertEquals("Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseComment() {
        String comment = "<!-- This is a comment -->";
        Document doc = Parser.parse(comment, baseUri);
        assertEquals("<!-- This is a comment -->", doc.html());
    }

    @Test
    public void testParseCdata() {
        String cdata = "<![CDATA[This is CDATA]]>";
        Document doc = Parser.parse(cdata, baseUri);
        assertEquals("This is CDATA", doc.html());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = Parser.parse(xmlDecl, baseUri);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.html());
    }

    @Test
    public void testParseEndTag() {
        String endTag = "<h1>Title</h1></body></html>";
        Document doc = Parser.parse(endTag, baseUri);
        assertEquals("<h1>Title</h1>", doc.html());
    }

    @Test
    public void testParseStartTag() {
        String startTag = "<h1>Title</h1>";
        Document doc = Parser.parse(startTag, baseUri);
        assertEquals("<h1>Title</h1>", doc.html());
    }

    @Test
    public void testParseAttribute() {
        String attribute = "<h1 class=\"header\">Title</h1>";
        Document doc = Parser.parse(attribute, baseUri);
        assertEquals("<h1 class=\"header\">Title</h1>", doc.html());
    }

    @Test
    public void testParseTextNode() {
        String textNode = "<h1>Title</h1>Text";
        Document doc = Parser.parse(textNode, baseUri);
        assertEquals("<h1>Title</h1>Text", doc.html());
    }

    @Test
    public void testParseTextNodeWithHtmlEntity() {
        String textNode = "<h1>&lt;Title&gt;</h1>";
        Document doc = Parser.parse(textNode, baseUri);
        assertEquals("<h1>&lt;Title&gt;</h1>", doc.html());
    }

    @Test
    public void testParseCdataInStartTag() {
        String cdata = "<script><![CDATA[console.log('Hello, World!');]]></script>";
        Document doc = Parser.parse(cdata, baseUri);
        assertEquals("<script>console.log('Hello, World!');</script>", doc.html());
    }
}