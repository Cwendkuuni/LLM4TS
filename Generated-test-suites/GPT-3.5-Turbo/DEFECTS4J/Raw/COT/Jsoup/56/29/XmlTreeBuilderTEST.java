import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
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
        String input = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(settings, xmlTreeBuilder.settings);
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("h1");
        xmlTreeBuilder.process(startTag);
        Element element = xmlTreeBuilder.stack.lastElement();
        assertNotNull(element);
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("h1");
        xmlTreeBuilder.stack.push(new Element("h1"));
        xmlTreeBuilder.process(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("example comment");
        xmlTreeBuilder.process(comment);
        Node node = xmlTreeBuilder.stack.lastElement().childNodes().first();
        assertTrue(node instanceof Comment);
        assertEquals("example comment", ((Comment) node).getData());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Example text");
        xmlTreeBuilder.process(character);
        Node node = xmlTreeBuilder.stack.lastElement().childNodes().first();
        assertTrue(node instanceof TextNode);
        assertEquals("Example text", ((TextNode) node).getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.process(doctype);
        Node node = xmlTreeBuilder.stack.lastElement().childNodes().first();
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).getName());
    }
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("h1");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("h1", element.nodeName());
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("example comment");
        xmlTreeBuilder.insert(commentToken);
        Node node = xmlTreeBuilder.stack.lastElement().childNodes().first();
        assertTrue(node instanceof Comment);
        assertEquals("example comment", ((Comment) node).getData());
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Example text");
        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.stack.lastElement().childNodes().first();
        assertTrue(node instanceof TextNode);
        assertEquals("Example text", ((TextNode) node).getWholeText());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctype);
        Node node = xmlTreeBuilder.stack.lastElement().childNodes().first();
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).getName());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("h1");
        xmlTreeBuilder.stack.push(new Element("h1"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<h1>Title</h1>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("h1", ((Element) nodes.get(0)).nodeName());
    }
}