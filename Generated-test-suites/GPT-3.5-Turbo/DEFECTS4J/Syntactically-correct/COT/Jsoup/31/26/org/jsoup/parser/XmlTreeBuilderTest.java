import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;

import java.util.Iterator;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testInitialiseParse() {
        // Test the initialisation of parse
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Check that the document is placed onto the stack
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessStartTag() {
        // Test the process method with a start tag
        Token startTag = new Token.StartTag("tag", "name", true);
        xmlTreeBuilder.process(startTag);

        // Check that the tag is inserted into the stack
        Element element = (Element) xmlTreeBuilder.stack.peek();
        assertEquals("tag", element.tagName());
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        // Test the process method with an end tag
        Token endTag = new Token.EndTag("tag", "name");
        xmlTreeBuilder.process(endTag);

        // Check that the stack is properly popped
        Stack<Element> stack = xmlTreeBuilder.stack;
        assertEquals(0, stack.size());
    }

    @Test
    public void testProcessComment() {
        // Test the process method with a comment
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.process(commentToken);

        // Check that the comment node is inserted
        Node comment = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(comment instanceof Comment);
        assertEquals("This is a comment", comment.getWholeText());
    }

    @Test
    public void testProcessCharacter() {
        // Test the process method with a character
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.process(characterToken);

        // Check that the character node is inserted
        Node character = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(character instanceof TextNode);
        assertEquals("This is a character", character.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        // Test the process method with a doctype
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
        xmlTreeBuilder.process(doctype);

        // Check that the doctype node is inserted
        Node doctypeNode = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(doctypeNode instanceof DocumentType);
        assertEquals("html", ((DocumentType) doctypeNode).getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", ((DocumentType) doctypeNode).getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", ((DocumentType) doctypeNode).getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        // Test the popStackToClose method
        Tag tag = Tag.valueOf("tag");
        Element element1 = new Element(tag, "http://example.com", null);
        Element element2 = new Element(tag, "http://example.com", null);
        Element element3 = new Element(tag, "http://example.com", null);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("tag", "name");
        xmlTreeBuilder.popStackToClose(endTag);

        // Check that the stack is properly popped
        Stack<Element> stack = xmlTreeBuilder.stack;
        assertEquals(0, stack.size());
    }

    @Test
    public void testInsertNode() {
        // Test the insertNode method
        Tag tag = Tag.valueOf("tag");
        Element element = new Element(tag, "http://example.com", null);
        xmlTreeBuilder.stack.add(element);

        TextNode node = new TextNode("Some text", "http://example.com");
        xmlTreeBuilder.insertNode(node);

        // Check that the node is inserted
        Node insertedNode = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(insertedNode instanceof TextNode);
        assertEquals("Some text", insertedNode.getWholeText());
    }

    @Test
    public void testInsertStartTag() {
        // Test the insert method with a start tag
        Token.StartTag startTag = new Token.StartTag("tag", "name", false);
        Element element = xmlTreeBuilder.insert(startTag);

        // Check that the element is inserted and added to the stack
        assertEquals("tag", element.tagName());
        assertEquals("name", element.nodeName());
        assertFalse(element.isSelfClosing());
        assertEquals(element, xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testInsertComment() {
        // Test the insert method with a comment
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        // Check that the comment node is inserted
        Node comment = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(comment instanceof Comment);
        assertEquals("This is a comment", comment.getWholeText());
    }

    @Test
    public void testInsertCharacter() {
        // Test the insert method with a character
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.insert(characterToken);

        // Check that the character node is inserted
        Node character = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(character instanceof TextNode);
        assertEquals("This is a character", character.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        // Test the insert method with a doctype
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
        xmlTreeBuilder.insert(doctype);

        // Check that the doctype node is inserted
        Node doctypeNode = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(doctypeNode instanceof DocumentType);
        assertEquals("html", ((DocumentType) doctypeNode).getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", ((DocumentType) doctypeNode).getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", ((DocumentType) doctypeNode).getSystemIdentifier());
    }
}