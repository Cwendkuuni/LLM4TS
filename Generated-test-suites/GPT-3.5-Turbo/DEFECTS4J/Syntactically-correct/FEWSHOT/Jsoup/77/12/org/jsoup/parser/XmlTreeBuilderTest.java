package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder treeBuilder;
    private ParseErrorList errors;
    private ParseSettings settings;
    
    @Before
    public void setup() {
        treeBuilder = new XmlTreeBuilder();
        errors = ParseErrorList.tracking(100);
        settings = ParseSettings.preserveCase;
    }
    
    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, treeBuilder.defaultSettings());
    }
    
    @Test
    public void testParseReaderString() {
        String xml = "<root><element attr='value'>Text</element></root>";
        Reader reader = new StringReader(xml);
        Document document = treeBuilder.parse(reader, "http://example.com");
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        
        Element root = document.selectFirst("root");
        assertNotNull(root);
        
        Element element = document.selectFirst("element");
        assertNotNull(element);
        assertEquals("value", element.attr("attr"));
        assertEquals("Text", element.text());
    }
    
    @Test
    public void testParseStringString() {
        String xml = "<root><element attr='value'>Text</element></root>";
        Document document = treeBuilder.parse(xml, "http://example.com");
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        
        Element root = document.selectFirst("root");
        assertNotNull(root);
        
        Element element = document.selectFirst("element");
        assertNotNull(element);
        assertEquals("value", element.attr("attr"));
        assertEquals("Text", element.text());
    }
    
    @Test
    public void testInitialiseParse() {
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", errors, settings);
        
        assertFalse(treeBuilder.stack.isEmpty());
        assertTrue(treeBuilder.doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
    }
    
    @Test
    public void testProcess() {
        Token startTag = new Token.StartTag("element");
        Token endTag = new Token.EndTag("element");
        Token comment = new Token.Comment("Comment");
        Token character = new Token.Character("Text");
        Token doctype = new Token.Doctype("doctype");
        
        assertTrue(treeBuilder.process(startTag));
        assertTrue(treeBuilder.process(endTag));
        assertTrue(treeBuilder.process(comment));
        assertTrue(treeBuilder.process(character));
        assertTrue(treeBuilder.process(doctype));
    }
    
    @Test
    public void testInsertNode() {
        Document document = new Document("");
        Element root = document.appendElement("root");
        
        treeBuilder.insertNode(root);
        
        assertEquals(root, treeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        startTag.attributes().put("attr", "value");
        
        Element element = treeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attr("attr"));
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("Comment");
        
        treeBuilder.insert(commentToken);
        
        Comment comment = (Comment) treeBuilder.currentElement().childNode(0);
        assertNotNull(comment);
        assertEquals("Comment", comment.getData());
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character token = new Token.Character("Text");
        
        treeBuilder.insert(token);
        
        TextNode textNode = (TextNode) treeBuilder.currentElement().childNode(0);
        assertNotNull(textNode);
        assertEquals("Text", textNode.getWholeText());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype");
        doctype.setPublicIdentifier("public");
        doctype.setSystemIdentifier("system");
        
        treeBuilder.insert(doctype);
        
        DocumentType documentType = (DocumentType) treeBuilder.currentElement().childNode(0);
        assertNotNull(documentType);
        assertEquals("doctype", documentType.nodeName());
        assertEquals("public", documentType.attr("publicIdentifier"));
        assertEquals("system", documentType.attr("systemIdentifier"));
    }
    
    @Test
    public void testPopStackToClose() {
        Document document = new Document("");
        Element root = document.appendElement("root");
        Element element1 = root.appendElement("element");
        Element element2 = root.appendElement("element");
        
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);
        
        Token.EndTag endTag = new Token.EndTag("element");
        treeBuilder.popStackToClose(endTag);
        
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(element1, treeBuilder.stack.get(0));
    }
    
    @Test
    public void testParseFragment() {
        String xml = "<element>Text</element>";
        List<Node> nodes = treeBuilder.parseFragment(xml, "http://example.com", errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.nodeName());
        assertEquals("Text", element.text());
    }
}