import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<xml><element>value</element></xml>"), "http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Element rootElement = doc.child(0);
        Assert.assertEquals("xml", rootElement.tagName());
        Element element = rootElement.child(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("value", element.text());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<xml><element>value</element></xml>", "http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Element rootElement = doc.child(0);
        Assert.assertEquals("xml", rootElement.tagName());
        Element element = rootElement.child(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("value", element.text());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, true);
        builder.initialiseParse(new StringReader("<xml><element>value</element></xml>"), "http://example.com", errors, settings);
        Assert.assertEquals("http://example.com", builder.baseUri);
        Assert.assertEquals(0, errors.size());
        Assert.assertEquals(settings, builder.settings);
    }

    @Test
    public void testProcessForStartTagToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("element", "attribute=value");
        Element el = builder.insert(startTag);
        Assert.assertEquals("element", el.tagName());
        Assert.assertEquals("attribute", el.attributes().asList().get(0).getKey());
        Assert.assertEquals("value", el.attributes().asList().get(0).getValue());
    }

    @Test
    public void testProcessForEndTagToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("element");
        builder.insert(endTag);
        List<Element> stack = builder.stack;
        Assert.assertEquals(0, stack.size());
    }

    @Test
    public void testProcessForCommentToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.insert(commentToken);
        List<Node> nodes = builder.doc.childNodes();
        Assert.assertEquals(1, nodes.size());
        Node comment = nodes.get(0);
        Assert.assertEquals(Node.COMMENT_NODE, comment.nodeType());
        Assert.assertEquals("This is a comment", comment.toString());
    }

    @Test
    public void testProcessForCharacterToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("value");
        builder.insert(characterToken);
        List<Node> nodes = builder.doc.childNodes();
        Assert.assertEquals(1, nodes.size());
        Node textNode = nodes.get(0);
        Assert.assertEquals(Node.TEXT_NODE, textNode.nodeType());
        Assert.assertEquals("value", textNode.toString());
    }

    @Test
    public void testProcessForDoctypeToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
        builder.insert(doctypeToken);
        List<Node> nodes = builder.doc.childNodes();
        Assert.assertEquals(1, nodes.size());
        Node doctypeNode = nodes.get(0);
        Assert.assertEquals(Node.DOCTYPE_NODE, doctypeNode.nodeType());
        Assert.assertEquals("html", ((DocumentType) doctypeNode).name());
        Assert.assertEquals("-//W3C//DTD HTML 4.01//EN", ((DocumentType) doctypeNode).publicId());
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", ((DocumentType) doctypeNode).systemId());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("element");
        builder.initialiseParse(new StringReader("<xml><element></element></xml>"), "http://example.com", new ParseErrorList(), new ParseSettings());
        builder.process(new Token.StartTag("element", ""));
        builder.insert(endTag);
        List<Element> stack = builder.stack;
        Assert.assertEquals(0, stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<element>value</element>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("value", element.text());
    }
}