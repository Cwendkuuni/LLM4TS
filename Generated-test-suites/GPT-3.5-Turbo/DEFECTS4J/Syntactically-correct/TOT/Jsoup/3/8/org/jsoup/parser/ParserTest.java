import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser("", "", false);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Heading 1</h1><p>Paragraph 1</p></body></html>";
        Document doc = parser.parse(html, "");
        
        // Test the structure and content of the parsed document
        Element root = doc.child(0);
        Assert.assertEquals("html", root.tagName());
        
        Element body = root.child(0);
        Assert.assertEquals("body", body.tagName());
        
        Elements headings = body.getElementsByTag("h1");
        Assert.assertEquals(1, headings.size());
        Assert.assertEquals("Heading 1", headings.get(0).text());
        
        Elements paragraphs = body.getElementsByTag("p");
        Assert.assertEquals(1, paragraphs.size());
        Assert.assertEquals("Paragraph 1", paragraphs.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Heading 2</h2><p>Paragraph 2</p>";
        Document doc = parser.parseBodyFragment(bodyHtml, "");
        
        // Test the structure and content of the parsed body fragment
        Element body = doc.body();
        Assert.assertNotNull(body);
        
        Elements headings = body.getElementsByTag("h2");
        Assert.assertEquals(1, headings.size());
        Assert.assertEquals("Heading 2", headings.get(0).text());
        
        Elements paragraphs = body.getElementsByTag("p");
        Assert.assertEquals(1, paragraphs.size());
        Assert.assertEquals("Paragraph 2", paragraphs.get(0).text());
    }

    @Test
    public void testParseBodyFragmentWithEmptyHtml() {
        String bodyHtml = "";
        Document doc = parser.parseBodyFragment(bodyHtml, "");
        
        // Test that body is empty
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertTrue(body.children().isEmpty());
    }

    @Test
    public void testParseBodyFragmentWithInvalidHtml() {
        String bodyHtml = "<h3>Heading 3</h3>";
        Document doc = parser.parseBodyFragment(bodyHtml, "");
        
        // Test that body is empty if the HTML is not a valid body fragment
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertTrue(body.children().isEmpty());
    }

    @Test
    public void testParseWithComment() {
        String html = "<html><!-- This is a comment --><body><p>Paragraph with comment</p></body></html>";
        Document doc = parser.parse(html, "");
        
        // Test that the comment is parsed correctly and does not affect the structure of the document
        Element root = doc.child(0);
        Assert.assertEquals(3, root.childNodes().size());
        
        Comment comment = (Comment) root.childNode(0);
        Assert.assertEquals(" This is a comment ", comment.getData());
        
        Element body = root.child(2);
        Assert.assertEquals(1, body.childNodes().size());
        
        Element paragraph = body.child(0);
        Assert.assertEquals("Paragraph with comment", paragraph.text());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<html><body><![CDATA[This is CDATA]]></body></html>";
        Document doc = parser.parse(html, "");
        
        // Test that the CDATA section is parsed correctly and does not affect the structure of the document
        Element root = doc.child(0);
        Assert.assertEquals(3, root.childNodes().size());
        
        Element body = root.child(2);
        Assert.assertEquals(1, body.childNodes().size());
        
        TextNode cdata = (TextNode) body.childNode(0);
        Assert.assertEquals("This is CDATA", cdata.text());
    }

    @Test
    public void testParseWithXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body><p>Paragraph with XML declaration</p></body></html>";
        Document doc = parser.parse(html, "");
        
        // Test that the XML declaration is parsed correctly and does not affect the structure of the document
        Element root = doc.child(0);
        Assert.assertEquals(3, root.childNodes().size());

        Assert.assertEquals(html, doc.outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><h4>Heading 4</h4></body></html>";
        Document doc = parser.parse(html, "");
        
        // Test that the end tag is parsed correctly and does not affect the structure of the document
        Element root = doc.child(0);
        Assert.assertEquals("html", root.tagName());

        Element body = root.child(0);
        Assert.assertEquals(1, body.childNodes().size());
        
        Element heading = body.child(0);
        Assert.assertEquals("h4", heading.tagName());
        Assert.assertEquals("Heading 4", heading.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><div class=\"container\"><p>Paragraph inside div</p></div></body></html>";
        Document doc = parser.parse(html, "");
        
        // Test that the start tag is parsed correctly and does not affect the structure of the document
        Element root = doc.child(0);
        Assert.assertEquals(3, root.childNodes().size());

        Element body = root.child(0);
        Assert.assertEquals(1, body.childNodes().size());

        Element div = body.child(0);
        Assert.assertEquals("div", div.tagName());
        Assert.assertEquals("container", div.className());
        Assert.assertEquals(1, div.childNodes().size());
        
        Element paragraph = div.child(0);
        Assert.assertEquals("p", paragraph.tagName());
        Assert.assertEquals("Paragraph inside div", paragraph.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body>This is a text node<span>Inside span</span></body></html>";
        Document doc = parser.parse(html, "");
        
        // Test that the text node is parsed correctly and does not affect the structure of the document
        Element root = doc.child(0);
        Assert.assertEquals(3, root.childNodes().size());

        Element body = root.child(0);
        Assert.assertEquals(3, body.childNodes().size());
        
        TextNode textNode = (TextNode) body.childNode(0);
        Assert.assertEquals("This is a text node", textNode.text());
        
        Element span = body.child(2);
        Assert.assertEquals("span", span.tagName());
        Assert.assertEquals(1, span.childNodes().size());
        
        TextNode innerTextNode = (TextNode) span.childNode(0);
        Assert.assertEquals("Inside span", innerTextNode.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><div class=\"container\" id='div_id'><p>Paragraph inside div</p></div></body></html>";
        Document doc = parser.parse(html, "");
        
        // Test that the attributes are parsed correctly and do not affect the structure of the document
        Element root = doc.child(0);
        Assert.assertEquals(3, root.childNodes().size());

        Element body = root.child(0);
        Assert.assertEquals(1, body.childNodes().size());

        Element div = body.child(0);
        Assert.assertEquals("div", div.tagName());
        Assert.assertEquals(2, div.attributes().size());
        Assert.assertEquals("container", div.className());
        Assert.assertEquals("div_id", div.id());
        Assert.assertEquals(1, div.childNodes().size());
        
        Element paragraph = div.child(0);
        Assert.assertEquals("p", paragraph.tagName());
        Assert.assertEquals("Paragraph inside div", paragraph.text());
    }
}