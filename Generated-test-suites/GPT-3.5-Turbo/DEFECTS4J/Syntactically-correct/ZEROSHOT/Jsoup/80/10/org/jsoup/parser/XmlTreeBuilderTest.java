import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
    }
    
    @Test
    public void testParse() {
        Reader input = new StringReader("<xml><tag>Hello</tag></xml>");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("<xml><tag>Hello</tag></xml>", document.outerHtml());
    }
    
    @Test
    public void testParseString() {
        String input = "<xml><tag>Hello</tag></xml>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("<xml><tag>Hello</tag></xml>", document.outerHtml());
    }
    
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml><tag>Hello</tag></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertFalse(element.tag().isSelfClosing());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
        assertNotNull(element.attributes());
    }
    
    @Test
    public void testProcessSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "", true);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertTrue(element.tag().isSelfClosing());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
        assertNotNull(element.attributes());
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        Node node = xmlTreeBuilder.doc.childNodes().get(0);
        
        assertNotNull(node);
        assertEquals(NodeType.COMMENT, node.nodeType());
        assertEquals("This is a comment", ((Comment) node).getData());
    }
    
    @Test
    public void testInsertCDATA() {
        Token.Character token = new Token.Character("This is a CDATA section", true);
        
        xmlTreeBuilder.insert(token);
        
        Node node = xmlTreeBuilder.doc.childNodes().get(0);
        
        assertNotNull(node);
        assertEquals(NodeType.CDATA_SECTION, node.nodeType());
        assertEquals("This is a CDATA section", ((CDataNode) node).getWholeText());
    }
    
    @Test
    public void testInsertText() {
        Token.Character token = new Token.Character("Hello, world!");
        
        xmlTreeBuilder.insert(token);
        
        Node node = xmlTreeBuilder.doc.childNodes().get(0);
        
        assertNotNull(node);
        assertEquals(NodeType.TEXT, node.nodeType());
        assertEquals("Hello, world!", ((TextNode) node).getWholeText());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "public", "system", "pubSysKey");
        
        xmlTreeBuilder.insert(doctype);
        
        Node node = xmlTreeBuilder.doc.childNodes().get(0);
        
        assertNotNull(node);
        assertEquals(NodeType.DOCUMENT_TYPE, node.nodeType());
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("public", doctypeNode.publicId());
        assertEquals("system", doctypeNode.systemId());
        assertEquals("pubSysKey", doctypeNode.pubSysKey());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);
        Element element = new Element("tag");
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>Hello</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("tag", nodes.get(0).nodeName());
        assertEquals("Hello", nodes.get(0).childNodes().get(0).wholeText());
    }
}