import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() throws Exception {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(input, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertSame(xmlTreeBuilder.getDocument(), xmlTreeBuilder.getCurrentElement());
        assertNotNull(xmlTreeBuilder.getStack());
        assertFalse(xmlTreeBuilder.getStack().isEmpty());
        assertSame(xmlTreeBuilder.getDocument(), xmlTreeBuilder.getStack().peek());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        xmlTreeBuilder.insert(startTag);
        
        Element element = xmlTreeBuilder.getDocument().body().child(0);
        assertEquals("div", element.nodeName());
        assertTrue(element instanceof Element);
        assertEquals(xmlTreeBuilder.getBaseUri(), element.baseUri());
        assertNull(element.parent());
        assertEquals(0, element.siblingIndex());
        assertTrue(element.children().isEmpty());
        assertTrue(xmlTreeBuilder.getStack().contains(element));
    }

    @Test
    public void testProcessCommentToken() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        
        Node node = xmlTreeBuilder.getDocument().body().childNode(0);
        assertTrue(node instanceof Comment);
        assertEquals("This is a comment", ((Comment) node).getData());
        assertEquals(xmlTreeBuilder.getBaseUri(), node.baseUri());
        assertNull(node.parent());
        assertEquals(0, node.siblingIndex());
    }

    @Test
    public void testProcessCharacterToken() {
        Token.Character characterToken = new Token.Character("Some text");
        xmlTreeBuilder.insert(characterToken);
        
        Node node = xmlTreeBuilder.getDocument().body().childNode(0);
        assertTrue(node instanceof TextNode);
        assertEquals("Some text", ((TextNode) node).getWholeText());
        assertEquals(xmlTreeBuilder.getBaseUri(), node.baseUri());
        assertNull(node.parent());
        assertEquals(0, node.siblingIndex());
    }

    @Test
    public void testProcessDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html", null, null);
        xmlTreeBuilder.insert(doctypeToken);
        
        DocumentType documentType = xmlTreeBuilder.getDocument().childNode(0);
        assertTrue(documentType instanceof DocumentType);
        assertEquals("html", documentType.nodeName());
        assertNull(documentType.parent());
        assertEquals(0, documentType.siblingIndex());
        assertEquals(xmlTreeBuilder.getBaseUri(), documentType.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        Element element1 = new Element("div", "");
        Element element2 = new Element("p", "");
        Element element3 = new Element("div", "");
        Element element4 = new Element("span", "");
        
        xmlTreeBuilder.getStack().push(element1);
        xmlTreeBuilder.getStack().push(element2);
        xmlTreeBuilder.getStack().push(element3);
        xmlTreeBuilder.getStack().push(element4);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertFalse(xmlTreeBuilder.getStack().contains(element1));
        assertFalse(xmlTreeBuilder.getStack().contains(element3));
        assertFalse(xmlTreeBuilder.getStack().contains(element4));
        assertTrue(xmlTreeBuilder.getStack().contains(element2));
    }
}

