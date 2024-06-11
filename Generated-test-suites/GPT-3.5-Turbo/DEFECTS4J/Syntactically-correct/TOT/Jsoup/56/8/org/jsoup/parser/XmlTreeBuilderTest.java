import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "<root><child>Test</child></root>";
        String baseUri = "https://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
        assertEquals(1, document.children().size());

        Element root = document.child(0);
        assertEquals("root", root.tagName());
        assertEquals(1, root.children().size());

        Element child = root.child(0);
        assertEquals("child", child.tagName());
        assertEquals(1, child.children().size());

        Node textNode = child.childNode(0);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Test", ((TextNode) textNode).getWholeText());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child>Test</child></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.defaultSettings();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        List<Node> stack = xmlTreeBuilder.getStack();

        assertNotNull(stack);
        assertEquals(1, stack.size());

        Node document = stack.get(0);
        assertTrue(document instanceof Document);

        Document.OutputSettings outputSettings = ((Document) document).outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.Syntax.xml, outputSettings.syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes().put("class", "test");

        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("test", element.attr("class"));
    }

    @Test
    public void testProcess_EndTag() {
        Element divElement = new Element("div");
        xmlTreeBuilder.getStack().add(divElement);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.process(endTag);

        assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("Test comment");
        xmlTreeBuilder.process(comment);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof Comment);
        assertEquals("Test comment", ((Comment) node).getData());
    }

    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("Test character");
        xmlTreeBuilder.process(character);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof TextNode);
        assertEquals("Test character", ((TextNode) node).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("-//W3C//DTD HTML 4.0//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/REC-html40/strict.dtd");

        xmlTreeBuilder.process(doctype);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).getName());
        assertEquals("-//W3C//DTD HTML 4.0//EN", ((DocumentType) node).getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", ((DocumentType) node).getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        Node node = new TextNode("Test", "");
        element.append(node);

        xmlTreeBuilder.insertNode(node);

        assertEquals(element, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes().put("class", "test");
        startTag.setSelfClosing(false);

        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("test", element.attr("class"));
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element, xmlTreeBuilder.getStack().get(0));
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag selfClosingTag = new Token.StartTag("img");
        selfClosingTag.setSelfClosing(true);

        Element element = xmlTreeBuilder.insert(selfClosingTag);
        assertNotNull(element);
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
        assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testInsert_StartTag_UnknownTag() {
        Token.StartTag unknownTag = new Token.StartTag("unknown");
        unknownTag.setSelfClosing(true);

        Element element = xmlTreeBuilder.insert(unknownTag);
        assertNotNull(element);
        assertFalse(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
        assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment comment = new Token.Comment("Test comment");
        xmlTreeBuilder.insert(comment);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof Comment);
        assertEquals("Test comment", ((Comment) node).getData());
    }

    @Test
    public void testInsert_Comment_BogusDeclaration() {
        Token.Comment bogusComment = new Token.Comment("!DOCTYPE html PUBLIC");
        xmlTreeBuilder.insert(bogusComment);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof XmlDeclaration);
        assertEquals("!DOCTYPE", node.nodeName());
        assertEquals("html", ((XmlDeclaration) node).name());
        assertEquals("PUBLIC", ((XmlDeclaration) node).data());
    }

    @Test
    public void testInsert_Character() {
        Token.Character character = new Token.Character("Test character");
        xmlTreeBuilder.insert(character);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof TextNode);
        assertEquals("Test character", ((TextNode) node).getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("-//W3C//DTD HTML 4.0//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/REC-html40/strict.dtd");

        xmlTreeBuilder.insert(doctype);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).getName());
        assertEquals("-//W3C//DTD HTML 4.0//EN", ((DocumentType) node).getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", ((DocumentType) node).getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Element div1 = new Element("div");
        Element div2 = new Element("div");
        Element div3 = new Element("div");

        List<Element> stack = new ArrayList<>();
        stack.add(div1);
        stack.add(div2);
        stack.add(div3);
        xmlTreeBuilder.setStack(stack);

        Token.EndTag endTag = new Token.EndTag("div");
        endTag.attributes().put("class", "test");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child>Test</child>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.defaultSettings();

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        assertEquals("child", ((Element) node).tagName());
        assertEquals("Test", ((Element) node).text());
    }

}