package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    private Element root;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.Doctype doctype;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = mock(Document.class);
        root = mock(Element.class);
        xmlTreeBuilder.stack.push(doc);
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = mock(ParseErrorList.class);
        xmlTreeBuilder.initialiseParse("", "", errors);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.contains(doc));
    }

    @Test
    public void testProcessStartTag() {
        startTag = new Token.StartTag("tag", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("tag", element.nodeName());
        assertEquals(doc, element.ownerDocument());
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testProcessEndTag() {
        endTag = new Token.EndTag("tag");
        Element element = mock(Element.class);
        xmlTreeBuilder.stack.push(element);
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.contains(doc));
    }

    @Test
    public void testProcessComment() {
        commentToken = new Token.Comment("comment");
        Comment comment = xmlTreeBuilder.createCommentNode(commentToken);
        xmlTreeBuilder.insertNode(comment);
        assertEquals(comment, doc.childNodes().get(0));
    }

    @Test
    public void testProcessCharacter() {
        characterToken = new Token.Character("text");
        TextNode textNode = xmlTreeBuilder.createCharacterNode(characterToken);
        xmlTreeBuilder.insertNode(textNode);
        assertEquals(textNode, doc.childNodes().get(0));
    }

    @Test
    public void testProcessDoctype() {
        doctype = new Token.Doctype("name", "publicId", "systemId");
        DocumentType doctypeNode = xmlTreeBuilder.createDoctypeNode(doctype);
        xmlTreeBuilder.insertNode(doctypeNode);
        assertEquals(doctypeNode, doc.childNodes().get(0));
    }

}