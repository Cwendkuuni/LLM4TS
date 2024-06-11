
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.*;
import org.jsoup.nodes.*;

import java.util.Iterator;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList parseErrorList;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parseErrorList = new ParseErrorList();
    }
    
    @Test
    public void testInitialiseParse() {
        // Typical use case
        String input = "<tag>Some content</tag>";
        String baseUri = "http://example.com";
        xmlTreeBuilder.initialiseParse(input, baseUri, parseErrorList);
        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        assertEquals(parseErrorList, xmlTreeBuilder.errors);
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        
        // Error scenario
        input = null;
        baseUri = "http://example.com";
        xmlTreeBuilder.initialiseParse(input, baseUri, parseErrorList);
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }
    
    @Test
    public void testProcess() {
        // Typical use case - StartTag
        Token startTagToken = new Token.StartTag();
        boolean result = xmlTreeBuilder.process(startTagToken);
        assertTrue(result);
        
        // Typical use case - EndTag
        Token endTagToken = new Token.EndTag();
        result = xmlTreeBuilder.process(endTagToken);
        assertTrue(result);
        
        // Typical use case - Comment
        Token commentToken = new Token.Comment();
        result = xmlTreeBuilder.process(commentToken);
        assertTrue(result);
        
        // Typical use case - Character
        Token characterToken = new Token.Character();
        result = xmlTreeBuilder.process(characterToken);
        assertTrue(result);
        
        // Typical use case - Doctype
        Token doctypeToken = new Token.Doctype();
        result = xmlTreeBuilder.process(doctypeToken);
        assertTrue(result);
        
        // Error scenario - Unknown token type
        Token unknownToken = new Token.Unknown();
        try {
            xmlTreeBuilder.process(unknownToken);
            fail("Expected Validate.fail() to be called");
        } catch (RuntimeException e) {
            assertEquals("Unexpected token type: TokenType.Unknown", e.getMessage());
        }
    }
    
    @Test
    public void testInsertNode() {
        // Typical use case
        Element parentElement = new Element(Tag.valueOf("parent"), "http://example.com");
        xmlTreeBuilder.stack.push(parentElement);
        
        Element childElement = new Element(Tag.valueOf("child"), "http://example.com");
        xmlTreeBuilder.insertNode(childElement);
        
        assertTrue(parentElement.children().contains(childElement));
        
        // Edge case - Node is null
        xmlTreeBuilder.insertNode(null);
        
        // Edge case - Node is already an child of the parent element
        Element existingElement = new Element(Tag.valueOf("existing"), "http://example.com");
        parentElement.appendChild(existingElement);
        
        xmlTreeBuilder.insertNode(existingElement);
        
        assertEquals(1, parentElement.children().size());
    }
    
    @Test
    public void testInsert_StartTag() {
        // Typical use case
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        startTag.attributes.put("attr1", "value1");
        startTag.isSelfClosing(false);
        Element result = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(result);
        assertEquals(Tag.valueOf("tag"), result.tag());
        assertEquals("http://example.com", result.baseUri());
        assertEquals(1, result.attributes().size());
        assertEquals("value1", result.attr("attr1"));
        assertFalse(result.tag().isKnownTag());
        assertFalse(result.tag().isSelfClosing());
        assertTrue(xmlTreeBuilder.stack.contains(result));
        
        // Edge case - Self-closing tag
        startTag.isSelfClosing(true);
        result = xmlTreeBuilder.insert(startTag);
        
        assertTrue(result.tag().isSelfClosing());
        assertFalse(result.children().contains(result));
        
        // Edge case - Known tag
        startTag = new Token.StartTag();
        startTag.name("p");
        result = xmlTreeBuilder.insert(startTag);
        
        assertTrue(result.tag().isKnownTag());
        assertFalse(result.children().contains(result));
    }
    
    @Test
    public void testInsert_Comment() {
        // Typical use case
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("Some comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        
        // Edge case - Comment data is null
        commentToken.data(null);
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testInsert_Character() {
        // Typical use case
        Token.Character characterToken = new Token.Character();
        characterToken.data("Some text");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        
        // Edge case - Character data is null
        characterToken.data(null);
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testInsert_Doctype() {
        // Typical use case
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name("html");
        doctypeToken.publicIdentifier("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctypeToken.systemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        
        xmlTreeBuilder.insert(doctypeToken);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        
        // Edge case - Doctype name is null
        doctypeToken.name(null);
        
        xmlTreeBuilder.insert(doctypeToken);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose() {
        // Typical use case
        Element element1 = new Element(Tag.valueOf("div"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("p"), "http://example.com");
        Element element3 = new Element(Tag.valueOf("span"), "http://example.com");
        
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("p");
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertFalse(xmlTreeBuilder.stack.contains(element2));
        assertTrue(xmlTreeBuilder.stack.contains(element1));
        assertTrue(xmlTreeBuilder.stack.contains(element3));
        
        // Edge case - Stack is empty
        xmlTreeBuilder.stack.clear();
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
}

