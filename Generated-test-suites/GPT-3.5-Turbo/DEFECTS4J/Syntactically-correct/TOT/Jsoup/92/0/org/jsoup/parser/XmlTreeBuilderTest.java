import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Parser parser;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parser = new Parser(xmlTreeBuilder);
    }

    @Test
    public void testDefaultSettings() {
        Assert.assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml>test</xml>");
        String baseUri = "http://example.com";
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        Document doc = xmlTreeBuilder.getDocument();

        Assert.assertNotNull(doc);
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testParse() {
        Reader input = new StringReader("<xml>test</xml>");
        String baseUri = "http://example.com";

        Document doc = xmlTreeBuilder.parse(input, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("xml", doc.select("xml").text());
    }

    @Test
    public void testParseWithCustomParser() {
        Reader input = new StringReader("<xml>test</xml>");
        String baseUri = "http://example.com";

        Document doc = xmlTreeBuilder.parse(input, baseUri, parser);

        Assert.assertNotNull(doc);
        Assert.assertEquals("xml", doc.select("xml").text());
    }

    @Test
    public void testParseWithStringInput() {
        String input = "<xml>test</xml>";
        String baseUri = "http://example.com";

        Document doc = xmlTreeBuilder.parse(input, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("xml", doc.select("xml").text());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        Element el = xmlTreeBuilder.insert(startTag);

        Assert.assertNotNull(el);
        Assert.assertEquals("undefined", el.tagName());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        xmlTreeBuilder.popStackToClose(endTag);

        // No assertion needed as we are testing the method behavior
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment();

        xmlTreeBuilder.insert(comment);

        Elements comments = xmlTreeBuilder.getDocument().select("comment");

        Assert.assertFalse(comments.isEmpty());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character();

        xmlTreeBuilder.insert(character);

        Elements characters = xmlTreeBuilder.getDocument().select("character");

        Assert.assertFalse(characters.isEmpty());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        xmlTreeBuilder.insert(doctype);

        Elements doctypes = xmlTreeBuilder.getDocument().select("doctype");

        Assert.assertFalse(doctypes.isEmpty());
    }

    @Test
    public void testInsertNode() {
        Element el = new Element("test");

        xmlTreeBuilder.insertNode(el);

        Elements elements = xmlTreeBuilder.getDocument().select("test");

        Assert.assertFalse(elements.isEmpty());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        Element el = xmlTreeBuilder.insert(startTag);

        Elements elements = xmlTreeBuilder.getDocument().select("undefined");

        Assert.assertFalse(elements.isEmpty());
        Assert.assertFalse("undefined".equals(el.tagName()));
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment();

        xmlTreeBuilder.insert(commentToken);

        Elements comments = xmlTreeBuilder.getDocument().select("comment");

        Assert.assertFalse(comments.isEmpty());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character token = new Token.Character();

        xmlTreeBuilder.insert(token);

        Elements characters = xmlTreeBuilder.getDocument().select("character");

        Assert.assertFalse(characters.isEmpty());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype d = new Token.Doctype();

        xmlTreeBuilder.insert(d);

        Elements doctypes = xmlTreeBuilder.getDocument().select("doctype");

        Assert.assertFalse(doctypes.isEmpty());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";

        xmlTreeBuilder.popStackToClose(endTag);

        // No assertion needed as we are testing the method behavior
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<xml>test</xml>";
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("xml", ((Element) nodes.get(0)).tagName());
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<xml>test</xml>";
        Element context = new Element("context");
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("xml", ((Element) nodes.get(0)).tagName());
    }

}