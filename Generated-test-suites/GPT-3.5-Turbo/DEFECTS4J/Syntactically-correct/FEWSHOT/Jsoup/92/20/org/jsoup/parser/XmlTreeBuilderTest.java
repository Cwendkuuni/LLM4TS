import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        assertSame(ParseSettings.preserveCase, builder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);

        builder.initialiseParse(reader, baseUri, parser);

        Document doc = builder.doc;
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
        assertEquals(1, builder.stack.size());
        assertTrue(builder.stack.get(0) instanceof Document);
    }

    @Test
    public void testParseReader() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";

        Document doc = builder.parse(reader, baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseString() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = builder.parse(input, baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.add(new Token.Attribute("id", "test"));

        Element element = builder.insert(startTag);

        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(1, element.attributes().size());
        assertEquals("test", element.attr("id"));
        assertEquals(builder.baseUri, element.baseUri());
    }

    @Test
    public void testProcessEndTag() {
        Element div = new Element(Tag.valueOf("div"), "");
        builder.stack.add(div);

        Token.EndTag endTag = new Token.EndTag("div");
        builder.popStackToClose(endTag);

        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("Test comment");

        builder.insert(comment);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
        Comment insertedComment = (Comment) builder.currentElement().childNode(0);
        assertEquals("Test comment", insertedComment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Test character");

        builder.insert(character);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
        TextNode insertedText = (TextNode) builder.currentElement().childNode(0);
        assertEquals("Test character", insertedText.text());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");

        builder.insert(doctype);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
        DocumentType insertedDoctype = (DocumentType) builder.currentElement().childNode(0);
        assertEquals("html", insertedDoctype.name());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "");

        builder.insertNode(element);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertSame(element, builder.currentElement().childNode(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div><h1>Hello World</h1></div>";
        String baseUri = "http://example.com";

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, new Parser(builder));

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element div = (Element) nodes.get(0);
        assertEquals("div", div.tagName());
        assertEquals("http://example.com", div.baseUri());
        assertEquals(1, div.children().size());
        assertTrue(div.children().get(0) instanceof Element);
        Element h1 = (Element) div.children().get(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());
    }
}