import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

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
        // Assert expected stack size
        assertEquals(1, xmlTreeBuilder.stack.size());
        // Assert document is at top of stack
        assertSame(xmlTreeBuilder.doc, xmlTreeBuilder.stack.getLast());
    }

    @Test
    public void testProcess_StartTag() {
        // Set up test data
        Token.StartTag startTag = new Token.StartTag("div", "class", "container");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        
        boolean result = xmlTreeBuilder.process(startTag);
        
        // Assert true is returned
        assertTrue(result);
        // Assert under test element is inserted into current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(startTag, xmlTreeBuilder.currentElement().childNode(0));
        // Assert under test element is added to stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(startTag, xmlTreeBuilder.stack.getLast());
    }

    @Test
    public void testProcess_EndTag() {
        // Set up test data
        Token.EndTag endTag = new Token.EndTag("div");
        Element testElement = new Element(Tag.valueOf("div"), "http://example.com", null);
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        xmlTreeBuilder.stack.add(testElement);
        
        boolean result = xmlTreeBuilder.process(endTag);
        
        // Assert true is returned
        assertTrue(result);
        // Assert under test element is removed from stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(xmlTreeBuilder.currentElement(), xmlTreeBuilder.stack.getLast());
    }

    @Test
    public void testProcess_Comment() {
        // Set up test data
        Token.Comment comment = new Token.Comment("Test comment");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        
        boolean result = xmlTreeBuilder.process(comment);
        
        // Assert true is returned
        assertTrue(result);
        // Assert under test comment is inserted into current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(comment, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testProcess_Character() {
        // Set up test data
        Token.Character character = new Token.Character("Test character");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        
        boolean result = xmlTreeBuilder.process(character);
        
        // Assert true is returned
        assertTrue(result);
        // Assert under test character node is inserted into current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(character, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testProcess_Doctype() {
        // Set up test data
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        
        boolean result = xmlTreeBuilder.process(doctype);
        
        // Assert true is returned
        assertTrue(result);
        // Assert under test doctype node is inserted into current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(doctype, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertNode() {
        // Set up test data
        Element testElement = new Element(Tag.valueOf("div"), "http://example.com", null);
        xmlTreeBuilder.stack.add(testElement);
        Element parentElement = new Element(Tag.valueOf("body"), "http://example.com", null);
        parentElement.appendChild(testElement);
        
        xmlTreeBuilder.insertNode(testElement);
        
        // Assert under test element is appended to current element
        assertSame(parentElement, xmlTreeBuilder.currentElement());
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(testElement, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsert_StartTag() {
        // Set up test data
        Token.StartTag startTag = new Token.StartTag("div", "class", "container");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        // Assert returned element is the under test element
        assertSame(startTag, result);
        // Assert under test element is inserted into current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(startTag, xmlTreeBuilder.currentElement().childNode(0));
        // Assert under test element is added to stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(startTag, xmlTreeBuilder.stack.getLast());
    }

    @Test
    public void testInsert_Comment() {
        // Set up test data
        Token.Comment comment = new Token.Comment("Test comment");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        
        xmlTreeBuilder.insert(comment);
        
        // Assert under test comment is inserted into current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(comment, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsert_Character() {
        // Set up test data
        Token.Character character = new Token.Character("Test character");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        
        xmlTreeBuilder.insert(character);
        
        // Assert under test character node is inserted into current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(character, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsert_Doctype() {
        // Set up test data
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        
        xmlTreeBuilder.insert(doctype);
        
        // Assert under test doctype node is inserted into current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(doctype, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testPopStackToClose() {
        // Set up test data
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "http://example.com", null));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", null));
        Element testElement = new Element(Tag.valueOf("div"), "http://example.com", null);
        xmlTreeBuilder.stack.add(testElement);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert under test element is removed from stack
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertSame(xmlTreeBuilder.currentElement(), xmlTreeBuilder.stack.getLast());
    }
}