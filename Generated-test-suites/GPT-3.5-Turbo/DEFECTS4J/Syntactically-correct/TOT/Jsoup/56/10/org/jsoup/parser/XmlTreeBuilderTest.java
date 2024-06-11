import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;

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
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(parseSettings);
    }

    @Test
    public void testParse() {
        String input = "<test>hello world</test>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("<test>hello world</test>", document.outerHtml());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<test>hello world</test>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings parseSettings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, parseSettings);

        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("test");
        xmlTreeBuilder.stack.push(new Element("parent", "http://example.com"));
        boolean result = xmlTreeBuilder.process(startTag);
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("test", xmlTreeBuilder.stack.peek().tagName());
    }

    @Test
    public void testInsertNode() {
        Node node = new TextNode("hello", "http://example.com");
        xmlTreeBuilder.stack.push(new Element("parent", "http://example.com"));
        xmlTreeBuilder.insertNode(node);
        assertEquals(node, xmlTreeBuilder.stack.peek().childNode(0));
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("test");
        startTag.attributes().put("attr", "value");

        xmlTreeBuilder.baseUri = "http://example.com";
        ParseSettings parseSettings = ParseSettings.preserveCase;

        Element result = xmlTreeBuilder.insert(startTag);

        assertNotNull(result);
        assertEquals("test", result.tagName());
        assertEquals("http://example.com", result.baseUri());
        assertTrue(result.attributes().contains("attr"));
        assertEquals("value", result.attributes().get("attr"));
        assertFalse(result.isSelfClosing());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(result, xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("test comment");

        xmlTreeBuilder.baseUri = "http://example.com";
        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.stack.peek().childNodes().size());
        assertTrue(xmlTreeBuilder.stack.peek().childNode(0) instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.stack.peek().childNode(0);
        assertEquals("test comment", comment.getData());
        assertEquals("http://example.com", comment.baseUri());
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("test character");

        xmlTreeBuilder.baseUri = "http://example.com";
        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.stack.peek().childNodes().size());
        assertTrue(xmlTreeBuilder.stack.peek().childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.stack.peek().childNode(0);
        assertEquals("test character", textNode.getWholeText());
        assertEquals("http://example.com", textNode.baseUri());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("-//W3C//DTD HTML 4.0//EN");
        doctype.systemIdentifier("http://www.w3.org/TR/REC-html40/strict.dtd");

        xmlTreeBuilder.baseUri = "http://example.com";
        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.stack.peek().childNodes().size());
        assertTrue(xmlTreeBuilder.stack.peek().childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.peek().childNode(0);
        assertEquals("html", documentType.name());
        assertEquals("-//W3C//DTD HTML 4.0//EN", documentType.publicIdentifier());
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", documentType.systemIdentifier());
        assertEquals("http://example.com", documentType.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("test");

        Element parent = new Element("parent", "http://example.com");
        Element testElement = new Element("test", "http://example.com");
        parent.appendChild(testElement);
        xmlTreeBuilder.stack.push(parent);
        xmlTreeBuilder.stack.push(new Element("child", "http://example.com"));

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(parent, xmlTreeBuilder.stack.peek());
        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<test>hello world</test>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings parseSettings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, parseSettings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("test", element.tagName());
        assertNull(element.parentNode());
    }

}