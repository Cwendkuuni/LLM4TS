package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder treeBuilder;
    
    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        Document doc = treeBuilder.getDocument();
        assertEquals(baseUri, doc.baseUri());
        assertEquals(input, doc.html());
    }
    
    @Test
    public void testProcess_StartTag() {
        Token token = new Token.StartTag("div");
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        Element el = treeBuilder.currentElement();
        assertEquals("div", el.tagName());
        assertTrue(treeBuilder.getStack().contains(el));
    }
    
    @Test
    public void testProcess_EndTag() {
        Element div = new Element("div");
        treeBuilder.getStack().push(div);
        Token token = new Token.EndTag("div");
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        assertFalse(treeBuilder.getStack().contains(div));
    }
    
    @Test
    public void testProcess_Comment() {
        String data = "test comment";
        Token token = new Token.Comment(data);
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        Node node = treeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof Comment);
        Comment comment = (Comment) node;
        assertEquals(data, comment.getData());
    }
    
    @Test
    public void testProcess_Character() {
        String data = "test character";
        Token token = new Token.Character(data);
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        Node node = treeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof TextNode);
        TextNode textNode = (TextNode) node;
        assertEquals(data, textNode.text());
    }
    
    @Test
    public void testProcess_Doctype() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token token = new Token.Doctype(name, publicId, systemId);
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        Node node = treeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof DocumentType);
        DocumentType doctype = (DocumentType) node;
        assertEquals(name, doctype.getName());
        assertEquals(publicId, doctype.publicId());
        assertEquals(systemId, doctype.systemId());
    }
    
    @Test
    public void testInsertNode() {
        Element div = new Element("div");
        treeBuilder.getStack().push(div);
        TextNode node = new TextNode("test");
        
        treeBuilder.insertNode(node);
        
        Element el = treeBuilder.currentElement();
        assertEquals(1, el.childNodeSize());
        assertEquals(node, el.childNode(0));
    }
    
    @Test
    public void testInsert_StartTag() {
        String tagName = "div";
        Token.StartTag startTag = new Token.StartTag(tagName);
        
        Element el = treeBuilder.insert(startTag);
        
        assertEquals(tagName, el.tagName());
        assertTrue(treeBuilder.getStack().contains(el));
    }
    
    @Test
    public void testInsert_StartTag_SelfClosing() {
        String tagName = "br";
        Token.StartTag startTag = new Token.StartTag(tagName, true);
        
        Element el = treeBuilder.insert(startTag);
        
        assertEquals(tagName, el.tagName());
        assertTrue(treeBuilder.getStack().contains(el));
        assertTrue(el.tag().isKnownTag());
        assertTrue(el.tag().isSelfClosing());
        assertTrue(startTag.isSelfClosing());
        assertFalse(treeBuilder.getTokeniser().isAcknowledgeSelfClosingFlag());
    }

    @Test
    public void testInsert_Comment_Bogus() {
        String data = "!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"";
        Token.Comment commentToken = new Token.Comment(data, true);
        
        treeBuilder.insert(commentToken);
        
        Element el = treeBuilder.currentElement();
        assertEquals(1, el.childNodeSize());
        assertTrue(el.childNode(0) instanceof XmlDeclaration);
        XmlDeclaration declaration = (XmlDeclaration) el.childNode(0);
        assertEquals(data.substring(1), declaration.getDeclaration());
        assertEquals(data.startsWith("!"), declaration.isDeclarationType());
    }
    
    @Test
    public void testInsert_Character() {
        String data = "test";
        Token.Character characterToken = new Token.Character(data);
        
        treeBuilder.insert(characterToken);
        
        Element el = treeBuilder.currentElement();
        assertEquals(1, el.childNodeSize());
        assertTrue(el.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) el.childNode(0);
        assertEquals(data, textNode.text());
    }
    
    @Test
    public void testInsert_Doctype() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype doctype = new Token.Doctype(name, publicId, systemId);
        
        treeBuilder.insert(doctype);
        
        Element el = treeBuilder.currentElement();
        assertEquals(1, el.childNodeSize());
        assertTrue(el.childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) el.childNode(0);
        assertEquals(name, doctypeNode.getName());
        assertEquals(publicId, doctypeNode.publicId());
        assertEquals(systemId, doctypeNode.systemId());
    }
    
    @Test
    public void testPopStackToClose() {
        Element div1 = new Element("div");
        Element div2 = new Element("div");
        Element div3 = new Element("div");
        treeBuilder.getStack().push(div1);
        treeBuilder.getStack().push(div2);
        treeBuilder.getStack().push(div3);
        Token.EndTag endTag = new Token.EndTag("div");
        
        treeBuilder.popStackToClose(endTag);
        
        assertFalse(treeBuilder.getStack().contains(div1));
        assertFalse(treeBuilder.getStack().contains(div2));
        assertTrue(treeBuilder.getStack().contains(div3));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<div>test</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element el = (Element) nodes.get(0);
        assertEquals("div", el.tagName());
        assertEquals("test", el.text());
    }
    
}