package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals("https://www.example.com", xmlTreeBuilder.baseUri);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testProcess_startTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = mock(Token.StartTag.class);
        Element element = new Element(Tag.valueOf("div"));
        
        when(startTag.name()).thenReturn("div");
        when(startTag.isSelfClosing()).thenReturn(false);
        
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("parent")));
        
        xmlTreeBuilder.insert(startTag);
        
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals(element, xmlTreeBuilder.stack.get(1));
    }
    
    @Test
    public void testProcess_endTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = mock(Token.EndTag.class);
        
        when(endTag.name()).thenReturn("div");
        
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div")));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("parent")));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcess_comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = mock(Token.Comment.class);
        
        String commentData = "Sample Comment";
        String baseUri = "https://www.example.com";
        
        Comment comment = new Comment(commentData, baseUri);
        Node insert = comment;
        
        when(commentToken.getData()).thenReturn(commentData);
        when(commentToken.bogus()).thenReturn(false);
        
        xmlTreeBuilder.baseUri = baseUri;
        
        xmlTreeBuilder.insert(commentToken);
        
        verify(commentToken, times(1)).getData();
        verify(commentToken, times(1)).bogus();
        
        assertEquals(2, xmlTreeBuilder.doc.childNodes().size());
        assertEquals(insert, xmlTreeBuilder.doc.childNodes().get(1));
    }
    
    @Test
    public void testProcess_character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = mock(Token.Character.class);
        
        String characterData = "Sample Character";
        String baseUri = "https://www.example.com";
        
        Node node = new TextNode(characterData, baseUri);
        
        when(characterToken.getData()).thenReturn(characterData);
        
        xmlTreeBuilder.baseUri = baseUri;
        
        xmlTreeBuilder.insert(characterToken);
        
        verify(characterToken, times(1)).getData();
        
        assertEquals(2, xmlTreeBuilder.doc.childNodes().size());
        assertEquals(node, xmlTreeBuilder.doc.childNodes().get(1));
    }
    
    @Test
    public void testProcess_doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = mock(Token.Doctype.class);
        
        String name = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.0 Transitional//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/loose.dtd";
        String baseUri = "https://www.example.com";
        
        DocumentType doctypeNode = new DocumentType(name, publicIdentifier, systemIdentifier, baseUri);
        
        when(doctype.getName()).thenReturn(name);
        when(doctype.getPublicIdentifier()).thenReturn(publicIdentifier);
        when(doctype.getSystemIdentifier()).thenReturn(systemIdentifier);
        
        xmlTreeBuilder.baseUri = baseUri;
        
        xmlTreeBuilder.insert(doctype);
        
        verify(doctype, times(1)).getName();
        verify(doctype, times(1)).getPublicIdentifier();
        verify(doctype, times(1)).getSystemIdentifier();
        
        assertEquals(2, xmlTreeBuilder.doc.childNodes().size());
        assertEquals(doctypeNode, xmlTreeBuilder.doc.childNodes().get(1));
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        String inputFragment = "<div></div>";
        String baseUri = "https://www.example.com";
        
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(inputFragment, baseUri, errors);
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(1, nodes.size());
    }
}