import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ParserTest {
    private Parser parser;
    private Document document;

    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";

        document = Parser.parse(html, baseUri);

        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("html", document.tagName());

        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());

        Element h1 = body.getElementsByTag("h1").first();
        Assert.assertNotNull(h1);
        Assert.assertEquals("Title", h1.text());

        Element p = body.getElementsByTag("p").first();
        Assert.assertNotNull(p);
        Assert.assertEquals("Paragraph", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";

        document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("body", document.tagName());

        Element h1 = document.getElementsByTag("h1").first();
        Assert.assertNotNull(h1);
        Assert.assertEquals("Title", h1.text());

        Element p = document.getElementsByTag("p").first();
        Assert.assertNotNull(p);
        Assert.assertEquals("Paragraph", p.text());
    }

    @Test
    public void testParseBodyFragmentWithEmptyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";

        document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("body", document.tagName());

        Assert.assertEquals(0, document.children().size());
    }

    @Test
    public void testParseBodyFragmentWithInvalidHtml() {
        String bodyHtml = "<h1>Title</p>";
        String baseUri = "http://example.com";

        document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("body", document.tagName());

        Element h1 = document.getElementsByTag("h1").first();
        Assert.assertNotNull(h1);
        Assert.assertEquals("Title", h1.text());
    }

    @Test
    public void testParseAttribute() {
        parser = Mockito.mock(Parser.class);

        parser.parseAttribute("\"key1\"", "\"value1\"");

        Mockito.verify(parser, Mockito.times(1))
                .parseAttribute(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testParseTextNode() {
        parser = Mockito.mock(Parser.class);
        parser.baseUri = "http://example.com";

        parser.parseTextNode("TextNode");

        Mockito.verify(parser, Mockito.times(1))
                .parseTextNode(Mockito.anyString());

        TextNode textNode = new TextNode("TextNode", "http://example.com");
        Assert.assertEquals(textNode, parser.last().childNodes().get(0));
    }

    @Test
    public void testParseCdata() {
        parser = Mockito.mock(Parser.class);
        parser.baseUri = "http://example.com";

        String rawText = "CDATA";

        parser.parseCdata(rawText);

        Mockito.verify(parser, Mockito.times(1))
                .parseCdata(Mockito.anyString());

        TextNode textNode = new TextNode(rawText, "http://example.com");
        Assert.assertEquals(textNode, parser.last().childNodes().get(0));
    }
}