import org.jsoup.parser.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Test initialisation of parsing with input, baseUri, and errors
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(xmlTreeBuilder.stack.size(), 1);

        // Test if errors are initialized properly
        assertNotNull(errors);
    }

    @Test
    public void testProcess() {
        // Test processing different types of tokens
        Token startTagToken = new Token.StartTag("div");
        assertTrue(xmlTreeBuilder.process(startTagToken));

        Token endTagToken = new Token.EndTag("div");
        assertTrue(xmlTreeBuilder.process(endTagToken));

        Token commentToken = new Token.Comment("This is a comment");
        assertTrue(xmlTreeBuilder.process(commentToken));

        Token characterToken = new Token.Character("Text");
        assertTrue(xmlTreeBuilder.process(characterToken));

        Token doctypeToken = new Tokent.Doctype("html", "PUBLIC", "-//W3C//DTD XHTML 1.0 Strict//EN", "");
        assertTrue(xmlTreeBuilder.process(doctypeToken));
    }

    @Test
    public void testInsertNode() {
        // Test inserting a Node into the current element
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(element);

        TextNode textNode = new TextNode("Text", "http://example.com");
        xmlTreeBuilder.insertNode(textNode);

        assertEquals(element.childNodeSize(), 1);
        assertEquals(element.childNode(0), textNode);
    }

    @Test
    public void testInsertStartTag() {
        // Test inserting a StartTag and creating a new element
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals(element.nodeName(), "div");
    }

    @Test
    public void testInsertComment() {
        // Test inserting a Comment node
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        assertEquals(xmlTreeBuilder.currentElement().childNodeSize(), 1);
    }

    @Test
    public void testInsertCharacter() {
        // Test inserting a Character node
        Token.Character characterToken = new Token.Character("Text");
        xmlTreeBuilder.insert(characterToken);

        assertEquals(xmlTreeBuilder.currentElement().childNodeSize(), 1);
    }

    @Test
    public void testInsertDoctype() {
        // Test inserting a Doctype node
        Token.Doctype doctypeToken = new Token.Doctype("html", "PUBLIC", "-//W3C//DTD XHTML 1.0 Strict//EN", "");
        xmlTreeBuilder.insert(doctypeToken);

        assertEquals(xmlTreeBuilder.currentElement().childNodeSize(), 1);
    }

    @Test
    public void testPopStackToClose() {
        // Test popping the stack to close an element
        Token.EndTag endTag = new Token.EndTag("div");
        Element divElement = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(divElement);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(xmlTreeBuilder.stack.size(), 0);
    }

}