
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String baseUri = "https://example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("Test Title", doc.title());
        Assert.assertEquals("Test Heading", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test Paragraph</p></div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertTrue(doc.body().select("div").hasText());
        Assert.assertEquals("Test Paragraph", doc.body().select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Test Paragraph</div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertTrue(doc.body().select("div").hasText());
        Assert.assertEquals("Test Paragraph", doc.body().select("p").text());
    }

    @Test
    public void testParseComment() {
        String commentHtml = "<!-- This is a comment -->";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(commentHtml, baseUri);

        Assert.assertEquals("<!-- This is a comment -->", doc.body().html());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(xmlDecl, baseUri);

        Assert.assertEquals(xmlDecl, doc.body().html());
    }

    @Test
    public void testParseEndTag() {
        String endTagHtml = "<p>Test Paragraph</p>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(endTagHtml, baseUri);

        Assert.assertEquals("Test Paragraph", doc.body().select("p").text());
    }

    @Test
    public void testParseStartTag() {
        String startTagHtml = "<img src=\"image.jpg\" alt=\"Test Image\">";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(startTagHtml, baseUri);

        Assert.assertEquals("image.jpg", doc.body().select("img").attr("src"));
        Assert.assertEquals("Test Image", doc.body().select("img").attr("alt"));
    }

    @Test
    public void testParseTextNode() {
        String textHtml = "<p>Test Paragraph</p>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(textHtml, baseUri);

        Assert.assertEquals("Test Paragraph", doc.body().text());
    }

    @Test
    public void testParseCdata() {
        String cdataHtml = "<![CDATA[This is CDATA]]>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(cdataHtml, baseUri);

        Assert.assertEquals(cdataHtml, doc.body().html());
    }

    @Test
    public void testParseAttribute() {
        String attributeHtml = "<div class=\"test\" id=\"123\">Test Div</div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(attributeHtml, baseUri);

        Assert.assertEquals("test", doc.body().select("div").attr("class"));
        Assert.assertEquals("123", doc.body().select("div").attr("id"));
        Assert.assertEquals("Test Div", doc.body().select("div").text());
    }

    @Test
    public void testStackHasValidParent() {
        String parentHtml = "<div><p>Test Paragraph</p></div>";
        String childHtml = "<span>Test Span</span>";
        String baseUri = "https://example.com";

        Document parentDoc = Parser.parseBodyFragment(parentHtml, baseUri);
        Document childDoc = Parser.parseBodyFragment(childHtml, baseUri);

        Assert.assertTrue(parentDoc.body().html().contains(childDoc.body().html()));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        String containerHtml = "<div><p>Test Paragraph</p></div>";
        String childHtml = "<span>Test Span</span>";
        String baseUri = "https://example.com";

        Document containerDoc = Parser.parseBodyFragment(containerHtml, baseUri);
        Document childDoc = Parser.parseBodyFragment(childHtml, baseUri);

        Assert.assertTrue(containerDoc.body().html().contains(childDoc.body().html()));
    }

    @Test
    public void testPopStackToClose() {
        String html = "<div><p>Test Paragraph</p></div></div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(html, baseUri);

        Assert.assertEquals("<div><p>Test Paragraph</p></div>", doc.body().html());
    }

    @Test
    public void testLast() {
        String html = "<div><p>Test Paragraph</p></div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(html, baseUri);

        Assert.assertEquals("Test Paragraph", doc.body().select("p").text());
        Assert.assertEquals("Test Paragraph", doc.body().childNodes().get(0).toString());
    }
}

