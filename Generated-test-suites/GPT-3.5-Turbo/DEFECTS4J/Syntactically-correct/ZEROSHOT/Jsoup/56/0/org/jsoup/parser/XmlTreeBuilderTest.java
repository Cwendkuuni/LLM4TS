import org.jsoup.nodes.*;
import org.jsoup.parser.*;
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
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParse() {
        String input = "<root><child>Text</child></root>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof Element);
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.tagName());
        
        assertEquals(1, root.childNodes().size());
        assertTrue(root.childNodes().get(0) instanceof Element);
        Element child = (Element) root.childNodes().get(0);
        assertEquals("child", child.tagName());
        
        assertEquals(1, child.childNodes().size());
        assertTrue(child.childNodes().get(0) instanceof TextNode);
        TextNode textNode = (TextNode) child.childNodes().get(0);
        assertEquals("Text", textNode.text());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<root><child>Text</child></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        assertEquals(input, xmlTreeBuilder.inputHtml());
        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertEquals(settings, xmlTreeBuilder.settings());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        Document doc = (Document) xmlTreeBuilder.stack.get(0);
        assertTrue(doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
    }
    
    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("root");
        Token token = new Token(startTag);
        
        assertTrue(xmlTreeBuilder.process(token));
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        Document doc = (Document) xmlTreeBuilder.stack.get(0);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof Element);
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.tagName());
    }
    
    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("root");
        Token token = new Token(endTag);
        
        assertTrue(xmlTreeBuilder.process(token));
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("This is a comment");
        Token token = new Token(commentToken);
        
        assertTrue(xmlTreeBuilder.process(token));
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        Document doc = (Document) xmlTreeBuilder.stack.get(0);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof Comment);
        Comment comment = (Comment) doc.childNodes().get(0);
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testProcess_Character() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("Text");
        Token token = new Token(characterToken);
        
        assertTrue(xmlTreeBuilder.process(token));
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        Document doc = (Document) xmlTreeBuilder.stack.get(0);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof TextNode);
        TextNode textNode = (TextNode) doc.childNodes().get(0);
        assertEquals("Text", textNode.text());
    }
    
    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("");
        doctype.systemIdentifier("");
        Token token = new Token(doctype);
        
        assertTrue(xmlTreeBuilder.process(token));
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        Document doc = (Document) xmlTreeBuilder.stack.get(0);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
        assertEquals("html", doctypeNode.nodeName());
    }
    
    @Test
    public void testInsertNode() {
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("parent"), "http://example.com"));
        Element parent = xmlTreeBuilder.currentElement();
        Element child = new Element(Tag.valueOf("child"), "http://example.com");
        
        xmlTreeBuilder.insertNode(child);
        
        assertEquals(1, parent.childNodes().size());
        assertEquals(child, parent.childNodes().get(0));
    }
    
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("element");
        startTag.attributes(new Attributes());
        Token token = new Token(startTag);
        
        Element el = xmlTreeBuilder.insert(startTag);
        
        assertEquals("element", el.tagName());
        assertEquals("http://example.com", el.baseUri());
        assertNotNull(el.attributes());
        assertEquals(0, el.childNodes().size());
    }
    
    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("This is a comment");
        Token token = new Token(commentToken);
        
        xmlTreeBuilder.insert(token);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        Document doc = (Document) xmlTreeBuilder.stack.get(0);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof Comment);
        Comment comment = (Comment) doc.childNodes().get(0);
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("Text");
        Token token = new Token(characterToken);
        
        xmlTreeBuilder.insert(token);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        Document doc = (Document) xmlTreeBuilder.stack.get(0);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof TextNode);
        TextNode textNode = (TextNode) doc.childNodes().get(0);
        assertEquals("Text", textNode.text());
    }
    
    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("");
        doctype.systemIdentifier("");
        Token token = new Token(doctype);
        
        xmlTreeBuilder.insert(token);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        Document doc = (Document) xmlTreeBuilder.stack.get(0);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNodes().get(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
        assertEquals("html", doctypeNode.nodeName());
    }
    
    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("parent1"), "http://example.com"));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("child1"), "http://example.com"));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("child2"), "http://example.com"));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("parent2"), "http://example.com"));
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("child1");
        Token token = new Token(endTag);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Element);
        assertTrue(xmlTreeBuilder.stack.get(1) instanceof Element);
        assertEquals("parent1", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals("parent2", xmlTreeBuilder.stack.get(1).nodeName());
    }
    
    @Test
    public void testParseFragment() {
        String input = "<root><child>Text</child></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        
        assertEquals(1, root.childNodes().size());
        assertTrue(root.childNodes().get(0) instanceof Element);
        Element child = (Element) root.childNodes().get(0);
        assertEquals("child", child.tagName());
        
        assertEquals(1, child.childNodes().size());
        assertTrue(child.childNodes().get(0) instanceof TextNode);
        TextNode textNode = (TextNode) child.childNodes().get(0);
        assertEquals("Text", textNode.text());
    }
}