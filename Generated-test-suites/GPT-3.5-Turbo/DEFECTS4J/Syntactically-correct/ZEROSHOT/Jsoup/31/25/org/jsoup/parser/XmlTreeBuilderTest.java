package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    private String input;
    private String baseUri;
    private ParseErrorList errors;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
        input = "";
        baseUri = "";
        errors = new ParseErrorList();
    }
    
    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(xmlTreeBuilder.stack.get(0), doc);
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element el = xmlTreeBuilder.insert(startTag);
        
        assertEquals(el.tagName(), "tag");
        assertEquals(el.baseUri(), baseUri);
        assertFalse(startTag.isSelfClosing());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element el = new Element("tag");
        xmlTreeBuilder.stack.add(el);
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("Sample comment");
        xmlTreeBuilder.insert(comment);
        
        assertEquals(doc.childNodes().get(0), comment);
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Sample character");
        xmlTreeBuilder.insert(character);
        
        assertEquals(doc.childNodes().get(0), character);
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctype);
        DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
        
        assertEquals(doctypeNode.getName(), "doctype");
        assertEquals(doctypeNode.getPublicIdentifier(), "");
        assertEquals(doctypeNode.getSystemIdentifier(), "");
        assertEquals(doctypeNode.baseUri(), baseUri);
    }
}