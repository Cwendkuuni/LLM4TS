import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.parser.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = null;
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        StringReader input = new StringReader("<xml><tag>text</tag></xml>");
        String baseUri = "http://example.com";
        doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("<xml><tag>text</tag></xml>", doc.toString());
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml><tag>text</tag></xml>";
        String baseUri = "http://example.com";
        doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("<xml><tag>text</tag></xml>", doc.toString());
    }
    
    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("<xml><tag>text</tag></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals("xml", xmlTreeBuilder.currentElement().nodeName());
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "name");
        Element el = xmlTreeBuilder.insert(startTag);
        assertNotNull(el);
        assertEquals("tag", el.tagName());
        assertEquals("name", el.nodeName());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.process(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("div", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals("comment", xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("text", false);
        xmlTreeBuilder.insert(character);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertEquals("text", xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd");
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("html", doctypeNode.getName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctypeNode.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctypeNode.getSystemIdentifier());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("div", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>text</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element el = (Element) nodes.get(0);
        assertEquals("tag", el.tagName());
        assertEquals("text", el.text());
    }
}