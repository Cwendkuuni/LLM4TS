
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(input, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertSame(errors, xmlTreeBuilder.getErrors());
        assertEquals(xmlTreeBuilder.getDocument(), xmlTreeBuilder.getCurrentElement());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", null);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals(xmlTreeBuilder.getBaseUri(), element.baseUri());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        
        xmlTreeBuilder.insert(endTag);
        
        // Check that the tag is removed from the stack
        Iterator<Element> it = xmlTreeBuilder.getStack().descendingIterator();
        while (it.hasNext()) {
            Element next = it.next();
            assertNotEquals("tag", next.nodeName());
        }
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment", null);
        
        xmlTreeBuilder.insert(commentToken);
        
        // Check that a comment node is inserted into the current element
        Node lastChild = xmlTreeBuilder.getCurrentElement().lastChild();
        assertTrue(lastChild instanceof Comment);
        assertEquals("This is a comment", ((Comment) lastChild).getData());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a character", null);
        
        xmlTreeBuilder.insert(characterToken);
        
        // Check that a text node is inserted into the current element
        Node lastChild = xmlTreeBuilder.getCurrentElement().lastChild();
        assertTrue(lastChild instanceof TextNode);
        assertEquals("This is a character", ((TextNode) lastChild).getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", null);
        
        xmlTreeBuilder.insert(doctype);
        
        // Check that a doctype node is inserted into the current element
        Node lastChild = xmlTreeBuilder.getCurrentElement().lastChild();
        assertTrue(lastChild instanceof DocumentType);
        assertEquals("html", ((DocumentType) lastChild).getName());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element1 = new Element("div");
        Element element2 = new Element("p");
        Element element3 = new Element("tag");
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);
        xmlTreeBuilder.getStack().add(element3);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Check that the first occurrence of "tag" element is removed from the stack
        Iterator<Element> it = xmlTreeBuilder.getStack().descendingIterator();
        boolean found = false;
        while (it.hasNext()) {
            Element next = it.next();
            if (next.nodeName().equals("tag")) {
                found = true;
                break;
            }
        }
        
        assertFalse(found);
    }
}
