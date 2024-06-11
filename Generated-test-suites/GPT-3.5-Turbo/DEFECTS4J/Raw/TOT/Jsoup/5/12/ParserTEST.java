import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private static final String BASE_URI = "http://www.example.com";

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(null, BASE_URI, false);
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        Document document = parser.parse(html, BASE_URI);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n" +
                " <head>\n" +
                "  <title>Test</title>\n" +
                " </head>\n" +
                " <body></body>\n" +
                "</html>", document.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        Document document = parser.parseBodyFragment(bodyHtml, BASE_URI);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div>\n" +
                "   <p>Test</p>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>", document.outerHtml());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Test</div></p>";
        Document document = parser.parseBodyFragmentRelaxed(bodyHtml, BASE_URI);

        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div>\n" +
                "   <p>\n" +
                "    Test\n" +
                "   </p>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>", document.outerHtml());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Element element = parser.parseComment(html, BASE_URI);

        Assert.assertNotNull(element);
        Assert.assertEquals("<!-- This is a comment -->", element.outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Element element = parser.parseXmlDecl(html, BASE_URI);

        Assert.assertNotNull(element);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", element.outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "</div>";
        Element element = parser.parseEndTag(html, BASE_URI);

        Assert.assertNotNull(element);
        Assert.assertEquals("</div>", element.outerHtml());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div class=\"container\"></div>";
        Element element = parser.parseStartTag(html, BASE_URI);

        Assert.assertNotNull(element);
        Assert.assertEquals("<div class=\"container\"></div>", element.outerHtml());
    }

    @Test
    public void testParseTextNode() {
        String html = "This is a text node";
        Element element = parser.parseTextNode(html, BASE_URI);

        Assert.assertNotNull(element);
        Assert.assertEquals("This is a text node", element.outerHtml());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        Element element = parser.parseCdata(html, BASE_URI);

        Assert.assertNotNull(element);
        Assert.assertEquals("<![CDATA[This is CDATA]]>", element.outerHtml());
    }

    @Test
    public void testParseAttribute() {
        String html = "href=\"http://www.example.com\"";
        Attribute attribute = parser.parseAttribute(html, BASE_URI);

        Assert.assertNotNull(attribute);
        Assert.assertEquals("href", attribute.getKey());
        Assert.assertEquals("http://www.example.com", attribute.getValue());
    }

    @Test
    public void testStackHasValidParent() {
        Tag childTag = Tag.valueOf("div");
        boolean validParent = parser.stackHasValidParent(childTag);

        Assert.assertTrue(validParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Tag tag = Tag.valueOf("div");
        Element parent = parser.popStackToSuitableContainer(tag);

        Assert.assertNull(parent);
    }

    @Test
    public void testPopStackToClose() {
        Tag tag = Tag.valueOf("div");
        Element element = parser.popStackToClose(tag);

        Assert.assertNull(element);
    }

    @Test
    public void testLast() {
        Element element = parser.last();

        Assert.assertNull(element);
    }

}