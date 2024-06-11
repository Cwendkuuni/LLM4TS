import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private String baseUri;
    private Document document;
    private ParseErrorList parseErrorList;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        baseUri = "http://example.com";
        document = new Document(baseUri);
        parseErrorList = new ParseErrorList();
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><child></child></root>", baseUri, parseErrorList);
        assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
        assertFalse(parseErrorList.hasErrors());
    }

    @Test
    public void testProcess_startTag() {
        xmlTreeBuilder.stack.add(document);
        xmlTreeBuilder.process(Token.StartTag("root", "<root>"));
        Elements elements = document.getElementsByTag("root");
        assertFalse(elements.isEmpty());
        assertEquals("root", elements.first().tagName());
    }

    @Test
    public void testProcess_endTag() {
        Element rootElement = new Element("root");
        xmlTreeBuilder.stack.add(document);
        xmlTreeBuilder.stack.add(rootElement);
        xmlTreeBuilder.process(Token.EndTag("root", "</root>"));
        Elements elements = document.getElementsByTag("root");
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testProcess_commentTag() {
        xmlTreeBuilder.stack.add(document);
        xmlTreeBuilder.process(Token.Comment("<!-- This is a comment -->"));
        assertEquals(1, document.childNodes().size());
        assertEquals("<!-- This is a comment -->", document.childNode(0).outerHtml());
    }

    @Test
    public void testProcess_characterTag() {
        xmlTreeBuilder.stack.add(document);
        xmlTreeBuilder.process(Token.Character("This is some text"));
        assertEquals(1, document.childNodes().size());
        assertEquals("This is some text", document.childNode(0).outerHtml());
    }

    @Test
    public void testProcess_doctypeTag() {
        xmlTreeBuilder.stack.add(document);
        xmlTreeBuilder.process(Token.Doctype("html", null, null));
        assertEquals(1, document.childNodes().size());
        assertEquals("<!DOCTYPE html>", document.childNode(0).outerHtml());
    }

    @Test
    public void testInsertNode() {
        xmlTreeBuilder.stack.add(document);
        Element parent = new Element("parent");
        xmlTreeBuilder.stack.add(parent);
        TextNode child = new TextNode("This is the child node", "");
        xmlTreeBuilder.insertNode(child);
        assertEquals(1, parent.childNodes().size());
        assertEquals("This is the child node", parent.childNode(0).outerHtml());
    }

    @Test
    public void testInsert_startTag() {
        Element parent = new Element("parent");
        xmlTreeBuilder.stack.add(document);
        xmlTreeBuilder.stack.add(parent);
        Token.StartTag startTag = Token.StartTag("child", "<child>");
        xmlTreeBuilder.insert(startTag);
        assertEquals(1, parent.children().size());
        assertEquals("child", parent.child(0).tagName());
    }

    @Test
    public void testInsert_startTag_selfClosing() {
        Element parent = new Element("parent");
        xmlTreeBuilder.stack.add(document);
        xmlTreeBuilder.stack.add(parent);
        Token.StartTag selfClosingTag = Token.StartTag("child", "<child/>");
        xmlTreeBuilder.insert(selfClosingTag);
        assertEquals(1, parent.children().size());
        assertTrue(parent.child(0).isSelfClosing());
    }

    @Test
    public void testInsert_startTag_unknownSelfClosing() {
        Element parent = new Element("parent");
        xmlTreeBuilder.stack.add(document);
        xmlTreeBuilder.stack.add(parent);
        Token.StartTag selfClosingTag = Token.StartTag("unknown", "<unknown/>");
        xmlTreeBuilder.insert(selfClosingTag);
        assertEquals(1, parent.children().size());
        assertTrue(parent.child(0).isSelfClosing());
        assertTrue(parent.child(0).tag().isKnownTag());
    }

    @Test
    public void testInsert_commentToken() {
        xmlTreeBuilder.stack.add(document);
        Token.Comment commentToken = Token.Comment("<!-- This is a comment -->");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, document.childNodes().size());
        assertEquals("<!-- This is a comment -->", document.childNode(0).outerHtml());
    }

    @Test
    public void testInsert_bogusCommentToken() {
        xmlTreeBuilder.stack.add(document);
        Token.Comment commentToken = Token.Comment("<!-- This is a bogus comment -->");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, document.childNodes().size());
        assertEquals("<!-- This is a bogus comment -->", document.childNode(0).outerHtml());
    }

    @Test
    public void testInsert_characterToken() {
        xmlTreeBuilder.stack.add(document);
        Token.Character characterToken = Token.Character("This is some text");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, document.childNodes().size());
        assertEquals("This is some text", document.childNode(0).outerHtml());
    }

    @Test
    public void testInsert_doctypeToken() {
        xmlTreeBuilder.stack.add(document);
        Token.Doctype doctypeToken = Token.Doctype("html", null, null);
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(1, document.childNodes().size());
        assertEquals("<!DOCTYPE html>", document.childNode(0).outerHtml());
    }

    @Test
    public void testPopStackToClose_elementFound() {
        Element parent = new Element("parent");
        Element child = new Element("child");
        xmlTreeBuilder.stack.add(parent);
        xmlTreeBuilder.stack.add(child);
        Token.EndTag endTag = Token.EndTag("child", "</child>");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(parent, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testPopStackToClose_elementNotFound() {
        Element parent = new Element("parent");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        xmlTreeBuilder.stack.add(parent);
        xmlTreeBuilder.stack.add(child1);
        xmlTreeBuilder.stack.add(child2);
        Token.EndTag endTag = Token.EndTag("unknown", "</unknown>");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(3, xmlTreeBuilder.stack.size());
        assertEquals(child2, xmlTreeBuilder.stack.get(2));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element>This is the text</element>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parseErrorList);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("element", nodes.get(0).nodeName());
        assertEquals("This is the text", nodes.get(0).outerHtml());
        assertFalse(parseErrorList.hasErrors());
    }
}