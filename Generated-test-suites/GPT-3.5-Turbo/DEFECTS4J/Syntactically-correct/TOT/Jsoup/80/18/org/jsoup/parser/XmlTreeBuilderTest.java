import org.jsoup.nodes.*;
import org.jsoup.parser.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
        
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = treeBuilder.parse(new StringReader("<root></root>"), "https://www.example.com");
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = treeBuilder.parse("<root></root>", "https://www.example.com");
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = treeBuilder.defaultSettings();
        treeBuilder.initialiseParse(new StringReader("<root></root>"), "https://www.example.com", errors, settings);
        
        assertEquals(settings, treeBuilder.settings);
        assertFalse(treeBuilder.stack.isEmpty());
        assertTrue(treeBuilder.stack.get(0) instanceof Document);
        assertEquals(Document.OutputSettings.Syntax.xml, ((Document) treeBuilder.stack.get(0)).outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("root", new Attributes());
        treeBuilder.initialiseParse(new StringReader("<root></root>"), "https://www.example.com", ParseErrorList.noTracking(), treeBuilder.defaultSettings());
        Element el = treeBuilder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("root", el.tagName());
        assertEquals("https://www.example.com", el.baseUri());
        assertTrue(el.childNodes().isEmpty());
        assertFalse(treeBuilder.stack.isEmpty());
        assertEquals(el, treeBuilder.stack.get(treeBuilder.stack.size() - 1));
    }

    @Test
    public void testProcessSelfClosingStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("root", true, new Attributes());
        treeBuilder.initialiseParse(new StringReader("<root></root>"), "https://www.example.com", ParseErrorList.noTracking(), treeBuilder.defaultSettings());
        Element el = treeBuilder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("root", el.tagName());
        assertTrue(el.childNodes().isEmpty());
        assertFalse(treeBuilder.stack.isEmpty());
        assertEquals(el, treeBuilder.stack.get(treeBuilder.stack.size() - 1));
        assertTrue(el.tag().isKnownTag());
        assertTrue(el.tag().isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("root", new Attributes());
        treeBuilder.initialiseParse(new StringReader("<root></root>"), "https://www.example.com", ParseErrorList.noTracking(), treeBuilder.defaultSettings());
        Element el = treeBuilder.insert(startTag);
        assertFalse(treeBuilder.stack.isEmpty());
        
        Token.EndTag endTag = new Token.EndTag("root");
        treeBuilder.process(endTag);
        
        assertTrue(treeBuilder.stack.isEmpty());
        assertEquals(0, el.childNodes().size());
    }

    @Test
    public void testProcessCommentToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");
        treeBuilder.initialiseParse(new StringReader("<root></root>"), "https://www.example.com", ParseErrorList.noTracking(), treeBuilder.defaultSettings());
        treeBuilder.insert(comment);
        
        assertFalse(treeBuilder.stack.isEmpty());
        assertEquals(1, treeBuilder.currentElement().childNodes().size());
        assertTrue(treeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) treeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testProcessCharacterToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Some text");
        treeBuilder.initialiseParse(new StringReader("<root></root>"), "https://www.example.com", ParseErrorList.noTracking(), treeBuilder.defaultSettings());
        treeBuilder.insert(character);
        
        assertFalse(treeBuilder.stack.isEmpty());
        assertEquals(1, treeBuilder.currentElement().childNodes().size());
        assertTrue(treeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertEquals("Some text", ((TextNode) treeBuilder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testProcessDoctypeToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype(false, "html", "publicid", "sysid");
        treeBuilder.initialiseParse(new StringReader("<root></root>"), "https://www.example.com", ParseErrorList.noTracking(), treeBuilder.defaultSettings());
        treeBuilder.insert(doctype);
        
        assertFalse(treeBuilder.stack.isEmpty());
        assertEquals(1, treeBuilder.currentElement().childNodes().size());
        assertTrue(treeBuilder.currentElement().childNode(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) treeBuilder.currentElement().childNode(0)).getName());
        assertEquals("publicid", ((DocumentType) treeBuilder.currentElement().childNode(0)).getPublicIdentifier());
        assertEquals("sysid", ((DocumentType) treeBuilder.currentElement().childNode(0)).getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag1 = new Token.StartTag("div", new Attributes());
        Token.StartTag startTag2 = new Token.StartTag("span", new Attributes());
        Token.StartTag startTag3 = new Token.StartTag("div", new Attributes());
        treeBuilder.initialiseParse(new StringReader("<div><span></span><div></div></div>"), "https://www.example.com", ParseErrorList.noTracking(), treeBuilder.defaultSettings());
        treeBuilder.insert(startTag1);
        treeBuilder.insert(startTag2);
        treeBuilder.insert(startTag3);
        
        assertFalse(treeBuilder.stack.isEmpty());
        assertEquals(3, treeBuilder.stack.size());
        
        Token.EndTag endTag = new Token.EndTag("div");
        treeBuilder.popStackToClose(endTag);
        
        assertFalse(treeBuilder.stack.isEmpty());
        assertEquals(1, treeBuilder.stack.size());
        assertTrue(treeBuilder.stack.get(0) instanceof Element);
        assertEquals("span", ((Element) treeBuilder.stack.get(0)).tagName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        List<Node> nodes = treeBuilder.parseFragment("<div><span></span></div>", "https://www.example.com", ParseErrorList.noTracking(), treeBuilder.defaultSettings());
        
        assertFalse(nodes.isEmpty());
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", ((Element) nodes.get(0)).tagName());
        assertEquals("https://www.example.com", ((Element) nodes.get(0)).baseUri());
        assertFalse(((Element) nodes.get(0)).childNodes().isEmpty());
        assertTrue(((Element) nodes.get(0)).childNode(0) instanceof Element);
        assertEquals("span", ((Element) ((Element) nodes.get(0)).childNode(0)).tagName());
    }
}