import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.Reader;
import java.io.StringReader;
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
    public void testInitialiseParse() {
        Reader input = new StringReader("");
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("");
        String baseUri = "http://example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "";
        String baseUri = "http://example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        
        xmlTreeBuilder.process(startTag);
        
        assertTrue(xmlTreeBuilder.currentElement().nodeName().equals("tag"));
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("tag");
        
        xmlTreeBuilder.process(endTag);
        
        assertFalse(xmlTreeBuilder.stack.contains(xmlTreeBuilder.currentElement()));
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment();
        comment.comment("test comment");
        
        xmlTreeBuilder.process(comment);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character();
        character.data("test data");
        
        xmlTreeBuilder.process(character);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        
        xmlTreeBuilder.process(doctype);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element("elem");
        
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        startTag.attributes(new Attributes());
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.nodeName());
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.comment("test comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character();
        character.data("test data");
        
        xmlTreeBuilder.insert(character);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        
        xmlTreeBuilder.insert(doctype);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("tag");
        
        Element element = new Element("tag");
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertFalse(xmlTreeBuilder.stack.contains(element));
    }
    
    @Test
    public void testParseFragmentWithInputFragmentStringAndBaseUri() {
        String inputFragment = "<tag>content</tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", ((Element) nodes.get(0)).nodeName());
    }
    
    @Test
    public void testParseFragmentWithInputFragmentStringAndElementContext() {
        String inputFragment = "<tag>content</tag>";
        Element context = new Element("parent");
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", ((Element) nodes.get(0)).nodeName());
    }
}