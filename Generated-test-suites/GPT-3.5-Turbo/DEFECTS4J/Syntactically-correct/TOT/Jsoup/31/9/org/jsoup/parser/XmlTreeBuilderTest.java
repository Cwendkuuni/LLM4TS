package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInsertNode() {
        // Typical use case: inserting a valid node and verifying it is correctly appended to the current element
        Element element = new Element("div");
        TextNode textNode = new TextNode("Hello, World!");
        treeBuilder.insertNode(element);
        treeBuilder.insertNode(textNode);
        assertEquals(textNode, element.childNode(0));
    }

    @Test
    public void testInsert_StartTag() {
        // Typical use case: inserting a start tag, including attributes, and verifying it is correctly inserted and added to the stack
        Token.StartTag startTag = new Token.StartTag("a");
        startTag.attributes.put("href", "/example");
        Element element = treeBuilder.insert(startTag);
        assertEquals("a", element.nodeName());
        assertEquals("/example", element.attr("href"));
        assertEquals(element, treeBuilder.stack.peek());
        assertEquals(element, treeBuilder.currentElement());
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        // Typical use case: inserting a self-closing start tag and verifying it is correctly inserted, added to the stack, and tokeniser flag is acknowledged
        Token.StartTag startTag = new Token.StartTag("img");
        startTag.selfClosing = true;
        Element element = treeBuilder.insert(startTag);
        assertEquals("img", element.nodeName());
        assertEquals(element, treeBuilder.stack.peek());
        assertEquals(element, treeBuilder.currentElement());
        assertTrue(treeBuilder.tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testInsert_StartTag_Unknown() {
        // Error scenario: inserting a start tag with an unknown tag name, verifying it is correctly inserted, and tag is marked as self-closing
        Token.StartTag startTag = new Token.StartTag("unknown");
        Element element = treeBuilder.insert(startTag);
        assertEquals("unknown", element.nodeName());
        assertEquals(element, treeBuilder.stack.peek());
        assertEquals(element, treeBuilder.currentElement());
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        // Typical use case: inserting a comment and verifying it is correctly inserted
        Token.Comment commentToken = new Token.Comment("This is a comment");
        treeBuilder.insert(commentToken);
        assertEquals(commentToken.getData(), treeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_Character() {
        // Typical use case: inserting a character token and verifying it is correctly inserted
        Token.Character characterToken = new Token.Character("Some text");
        treeBuilder.insert(characterToken);
        assertEquals(characterToken.getData(), treeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_Doctype() {
        // Typical use case: inserting a doctype and verifying it is correctly inserted
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd");
        treeBuilder.insert(doctype);
        assertEquals(doctype.getName(), treeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        // Typical use case: popping up the stack to remove an element with a specific name
        Element element1 = new Element("p");
        Element element2 = new Element("div");
        Element element3 = new Element("span");
        treeBuilder.stack.push(element1);
        treeBuilder.stack.push(element2);
        treeBuilder.stack.push(element3);

        Token.EndTag endTag = new Token.EndTag("div");
        treeBuilder.popStackToClose(endTag);
        
        assertEquals(element3, treeBuilder.stack.peek());
    }

    @Test
    public void testPopStackToClose_NotFound() {
        // Edge case: end tag not found in the stack, nothing should be removed
        Element element1 = new Element("p");
        Element element2 = new Element("div");
        treeBuilder.stack.push(element1);
        treeBuilder.stack.push(element2);

        Token.EndTag endTag = new Token.EndTag("span");
        treeBuilder.popStackToClose(endTag);

        assertEquals(element2, treeBuilder.stack.peek());
    }

    @Test
    public void testProcess() {
        // Edge case: testing the process method with different token types
        Token.StartTag startTag = new Token.StartTag("a");
        Token.EndTag endTag = new Token.EndTag("a");
        Token.Comment comment = new Token.Comment("This is a comment");
        Token.Character character = new Token.Character("Some text");
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd");
        Token.EOF eof = new Token.EOF();

        assertTrue(treeBuilder.process(startTag));
        assertTrue(treeBuilder.process(endTag));
        assertTrue(treeBuilder.process(comment));
        assertTrue(treeBuilder.process(character));
        assertTrue(treeBuilder.process(doctype));
        assertTrue(treeBuilder.process(eof));
    }

}