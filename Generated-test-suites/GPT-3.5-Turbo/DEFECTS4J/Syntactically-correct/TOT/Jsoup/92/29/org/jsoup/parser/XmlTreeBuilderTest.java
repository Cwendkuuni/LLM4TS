import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setup() {
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
        String input = "<xml><tag>content</tag></xml>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, parser);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(1, xmlTreeBuilder.doc.childNodeSize());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testParse() {
        String input = "<xml><tag>content</tag></xml>";
        String baseUri = "http://example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals("xml", document.childNode(0).nodeName());
        assertEquals(1, document.childNode(0).childNodeSize());
    }
    
    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "attr=value");
        
        boolean result = xmlTreeBuilder.process(startTag);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("tag", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals("value", xmlTreeBuilder.stack.get(0).attributes().get("attr"));
    }
    
    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        
        xmlTreeBuilder.stack.add(new Element("tag"));
        
        boolean result = xmlTreeBuilder.process(endTag);
        
        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        
        boolean result = xmlTreeBuilder.process(comment);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }
    
    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("abc");
        
        boolean result = xmlTreeBuilder.process(character);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("abc", ((TextNode)xmlTreeBuilder.currentElement().childNodes().get(0)).getWholeText());
    }
    
    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd");
        
        boolean result = xmlTreeBuilder.process(doctype);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
    }
    
    @Test
    public void testInsertNode() {
        Node node = new TextNode("text");
        
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
    }
    
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "attr=value");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.nodeName());
        assertEquals("value", element.attributes().get("attr"));
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testInsert_SelfClosingTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "attr=value", true);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.nodeName());
        assertEquals("value", element.attributes().get("attr"));
        assertTrue(element.tag().isSelfClosing());
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testInsert_Comment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(comment);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
    }
    
    @Test
    public void testInsert_Character() {
        Token.Character character = new Token.Character("text");
        
        xmlTreeBuilder.insert(character);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
    }
    
    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose_NotFound() {
        Token.EndTag endTag = new Token.EndTag("tag");
        
        xmlTreeBuilder.stack.add(new Element("anotherTag"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testPopStackToClose_Found() {
        Token.EndTag endTag = new Token.EndTag("tag");
        
        Element element = new Element("tag");
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.stack.add(new Element("anotherTag"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>fragment</tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("tag", nodes.get(0).nodeName());
        assertEquals("fragment", ((TextNode)nodes.get(0).childNodes().get(0)).getWholeText());
    }
    
    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<tag>fragment</tag>";
        Element context = new Element("context");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("tag", nodes.get(0).nodeName());
        assertEquals("fragment", ((TextNode)nodes.get(0).childNodes().get(0)).getWholeText());
    }
}