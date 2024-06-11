package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder builder;
    private Element doc;
    private Deque<Element> stack;
    private String baseUri;
    
    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
        doc = new Element("document");
        stack = new LinkedList<>();
        stack.add(doc);
        baseUri = "http://example.com";
        
        try {
            Field docField = builder.getClass().getDeclaredField("doc");
            docField.setAccessible(true);
            docField.set(builder, doc);
            
            Field stackField = builder.getClass().getDeclaredField("stack");
            stackField.setAccessible(true);
            stackField.set(builder, stack);
            
            Field baseUriField = builder.getClass().getDeclaredField("baseUri");
            baseUriField.setAccessible(true);
            baseUriField.set(builder, baseUri);
            
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errorList = new ParseErrorList();
        builder.initialiseParse("input", "baseUri", errorList);

        assertNull(errorList.getAllErrors());
        assertEquals("baseUri", builder.getBaseUri());
        assertEquals(doc, stack.getFirst());
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token.StartTag("div");
        Element result = builder.insert(token.asStartTag());

        assertNotNull(result);
        assertEquals("div", result.nodeName());
        assertEquals(baseUri, result.baseUri());
        assertEquals(0, result.childNodeSize());
        assertEquals(0, stack.size());

        token = new Token.StartTag("img");
        result = builder.insert(token.asStartTag());

        assertNotNull(result);
        assertEquals("img", result.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        Element element = new Element("div");
        stack.add(element);

        Token token = new Token.EndTag("div");
        builder.popStackToClose(token.asEndTag());

        assertEquals(0, stack.size());
    }

    @Test
    public void testProcessComment() {
        Token token = new Token.Comment("comment");
        builder.insert(token.asComment());

        assertEquals(1, doc.childNodeSize());
        Node node = doc.childNode(0);
        assertNotNull(node);
        assertEquals(NodeType.COMMENT, node.getNodeType());
        assertEquals("comment", ((Comment) node).getData());
    }

    @Test
    public void testProcessCharacter() {
        Token token = new Token.Character("text");
        builder.insert(token.asCharacter());

        assertEquals(1, doc.childNodeSize());
        Node node = doc.childNode(0);
        assertNotNull(node);
        assertEquals(NodeType.TEXT, node.getNodeType());
        assertEquals("text", ((TextNode) node).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token token = new Token.Doctype("name", "publicId", "systemId");
        builder.insert(token.asDoctype());

        assertEquals(1, doc.childNodeSize());
        Node node = doc.childNode(0);
        assertNotNull(node);
        assertEquals(NodeType.DOCTYPE, node.getNodeType());
        DocumentType doctype = (DocumentType) node;
        assertEquals("name", doctype.getName());
        assertEquals("publicId", doctype.getPublicIdentifier());
        assertEquals("systemId", doctype.getSystemIdentifier());
    }

    @Test
    public void testProcessEOF() {
        Token token = new Token.EOF();
        boolean result = builder.process(token);

        assertEquals(true, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessUnexpectedToken() {
        Token token = new Token("text");
        builder.process(token);
    }
}