import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml>test</xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        assertEquals(0, xmlTreeBuilder.errors.size());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        String input = "<xml>test</xml>";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        Token.StartTag startTag = new Token.StartTag("");
        xmlTreeBuilder.process(startTag);

        assertEquals("xml", xmlTreeBuilder.currentElement().nodeName());
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.currentElement()));
    }

    @Test
    public void testProcess_EndTag() {
        String input = "<xml>test</xml>";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        xmlTreeBuilder.stack.add(new Element("xml"));
        Token.EndTag endTag = new Token.EndTag("xml");
        xmlTreeBuilder.process(endTag);

        assertNull(xmlTreeBuilder.currentElement());
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcess_Comment() {
        String input = "<!-- Comment -->";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        Token.Comment commentToken = new Token.Comment(" Comment ");
        xmlTreeBuilder.process(commentToken);

        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertTrue(node instanceof Comment);
        assertEquals("Comment", node.outerHtml());
    }

    @Test
    public void testProcess_Character() {
        String input = "test";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        Token.Character characterToken = new Token.Character("test");
        xmlTreeBuilder.process(characterToken);

        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertTrue(node instanceof TextNode);
        assertEquals("test", node.outerHtml());
    }

    @Test
    public void testProcess_Doctype() {
        String input = "<!DOCTYPE html>";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.process(doctypeToken);

        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertTrue(node instanceof DocumentType);
        assertEquals("<!DOCTYPE html>", node.outerHtml());
    }

    @Test
    public void testInsertNode() {
        xmlTreeBuilder.stack.add(new Element("parent"));
        Node node = new Element("child");
        xmlTreeBuilder.insertNode(node);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("child", childNodes.get(0).nodeName());
    }

    @Test
    public void testInsert_StartTag() {
        String input = "<start>";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        Token.StartTag startTag = new Token.StartTag("start");
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("start", element.nodeName());
        assertTrue(element.attributes().isEmpty());
        assertTrue(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged);
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testInsert_Comment() {
        String input = "<!-- Comment -->";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        Token.Comment commentToken = new Token.Comment(" Comment ");
        xmlTreeBuilder.insert(commentToken);

        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertTrue(node instanceof Comment);
        assertEquals("Comment", node.outerHtml());
    }

    @Test
    public void testInsert_Character() {
        String input = "test";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        Token.Character characterToken = new Token.Character("test");
        xmlTreeBuilder.insert(characterToken);

        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertTrue(node instanceof TextNode);
        assertEquals("test", node.outerHtml());
    }

    @Test
    public void testInsert_Doctype() {
        String input = "<!DOCTYPE html>";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctypeToken);

        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertTrue(node instanceof DocumentType);
        assertEquals("<!DOCTYPE html>", node.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder.stack.add(new Element("parent"));
        Element element = new Element("child");
        xmlTreeBuilder.stack.add(element);

        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<test>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        assertEquals("test", childNodes.get(0).nodeName());
    }
}