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
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assert stack contains the document element
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "myDiv");

        xmlTreeBuilder.process(startTag);

        // Assert current element contains the newly created element
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("div", currentElement.nodeName());
        assertEquals("http://example.com", currentElement.baseUri());
        assertEquals("myDiv", currentElement.attr("id"));
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("div");

        // Create a div element and push it onto the stack
        Element divElement = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(divElement);

        xmlTreeBuilder.process(endTag);

        // Assert the div element is removed from the stack
        assertFalse(xmlTreeBuilder.stack.contains(divElement));
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("This is a comment");

        xmlTreeBuilder.process(comment);

        // Assert the comment is inserted as a child node of the current element
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals(1, currentElement.childNodes().size());
        assertTrue(currentElement.childNode(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) currentElement.childNode(0)).getData());
    }

    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("This is some text");

        xmlTreeBuilder.process(character);

        // Assert the character data is inserted as a child node of the current element
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals(1, currentElement.childNodes().size());
        assertTrue(currentElement.childNode(0) instanceof TextNode);
        assertEquals("This is some text", ((TextNode) currentElement.childNode(0)).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");

        xmlTreeBuilder.process(doctype);

        // Assert the doctype node is inserted as a child node of the current element
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals(1, currentElement.childNodes().size());
        assertTrue(currentElement.childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) currentElement.childNode(0);
        assertEquals("html", documentType.nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", documentType.getPublicId());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", documentType.getSystemId());
    }

    @Test
    public void testPopStackToClose_ElementFound() {
        Token.EndTag endTag = new Token.EndTag("div");

        // Create and push an element onto the stack
        Element divElement = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(divElement);

        xmlTreeBuilder.popStackToClose(endTag);

        // Assert the element is removed from the stack
        assertFalse(xmlTreeBuilder.stack.contains(divElement));
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        Token.EndTag endTag = new Token.EndTag("div");

        // Create and push another element onto the stack
        Element spanElement = new Element(Tag.valueOf("span"), "http://example.com");
        xmlTreeBuilder.stack.add(spanElement);

        xmlTreeBuilder.popStackToClose(endTag);

        // Assert the stack remains unchanged
        assertTrue(xmlTreeBuilder.stack.contains(spanElement));
    }

}