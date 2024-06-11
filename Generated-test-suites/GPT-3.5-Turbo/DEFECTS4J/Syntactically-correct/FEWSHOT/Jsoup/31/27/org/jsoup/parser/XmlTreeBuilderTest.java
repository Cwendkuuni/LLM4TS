package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;
    private Element doc;
    private String baseUri = "http://example.com";

    @Before
    public void setup() {
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", baseUri, null);
        doc = builder.stack.peek();
    }

    @Test
    public void testInitialiseParse() {
        assertEquals(doc, builder.stack.peek());
    }

    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("div");
        assertTrue(builder.process(startTagToken));

        Token endTagToken = new Token.EndTag("div");
        assertTrue(builder.process(endTagToken));

        Token commentToken = new Token.Comment("This is a comment");
        assertTrue(builder.process(commentToken));

        Token characterToken = new Token.Character("This is a text node");
        assertTrue(builder.process(characterToken));

        Token doctypeToken = new Token.Doctype("html");
        assertTrue(builder.process(doctypeToken));

        Token eofToken = new Token.EOF();
        assertTrue(builder.process(eofToken));
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div", baseUri);
        builder.insertNode(element);
        assertEquals(element, doc.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = builder.insert(startTag);
        assertEquals("div", element.nodeName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.insert(commentToken);
        Comment comment = (Comment) doc.childNode(0);
        assertEquals("This is a comment", comment.getData());
        assertEquals(baseUri, comment.baseUri());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("This is a text node");
        builder.insert(characterToken);
        TextNode textNode = (TextNode) doc.childNode(0);
        assertEquals("This is a text node", textNode.getWholeText());
        assertEquals(baseUri, textNode.baseUri());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        builder.insert(doctypeToken);
        DocumentType doctype = (DocumentType) doc.childNode(0);
        assertEquals("html", doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertEquals(baseUri, doctype.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        builder.stack.push(new Element("div", baseUri));
        builder.popStackToClose(endTag);
        assertTrue(builder.stack.isEmpty());
    }
}