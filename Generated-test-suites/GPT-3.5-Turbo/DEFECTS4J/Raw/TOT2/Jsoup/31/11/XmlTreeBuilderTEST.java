import org.jsoup.parser.*;
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
        // Test for correct initialization and adding document to the stack
        String input = "<html></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(xmlTreeBuilder.currentElement(), xmlTreeBuilder.doc);
        assertEquals(xmlTreeBuilder.stack.peek(), xmlTreeBuilder.doc);
    }

    @Test
    public void testInsertNode() {
        // Test for correct insertion of node into the current element
        Element element = new Element(Tag.valueOf("div"), "http://example.com", new Attributes());
        xmlTreeBuilder.insertNode(element);

        assertEquals(xmlTreeBuilder.currentElement().childNode(0), element);
    }

    @Test
    public void testInsertStartTag() {
        // Test for correct insertion of StartTag and handling self-closing tags
        Token.StartTag startTag = new Token.StartTag("div", new Attributes(), true);
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals(element.tagName(), "div");
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        // Test for correct insertion of Comment node
        Token.Comment commentToken = new Token.Comment("This is a comment", "http://example.com");
        xmlTreeBuilder.insert(commentToken);

        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        // Test for correct insertion of Character node
        Token.Character characterToken = new Token.Character("This is a character", "http://example.com");
        xmlTreeBuilder.insert(characterToken);

        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        // Test for correct insertion of Doctype node
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "http://example.com");
        xmlTreeBuilder.insert(doctypeToken);

        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        // Test for correctly popping the stack to close element with specified end tag
        Token.EndTag endTag = new Token.EndTag("div");
        Element element = new Element(Tag.valueOf("div"), "http://example.com", new Attributes());
        xmlTreeBuilder.stack.push(element);

        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
}