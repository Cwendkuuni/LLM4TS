package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "name", "attribute");
        Tag tag = Tag.valueOf("name");
        Element el = new Element(tag, "", startTag.attributes);
        Element result = xmlTreeBuilder.insert(startTag);
        assertEquals(el.tag(), result.tag());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag", "name");
        xmlTreeBuilder.insert(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        Comment comment = new Comment(commentToken.getData(), "");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(comment.getData(), xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("character");
        TextNode node = new TextNode(characterToken.getData(), "");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(node.getWholeText(), xmlTreeBuilder.currentElement().childNode(0).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype d = new Token.Doctype("doctype", "pubid", "sysid");
        DocumentType doctypeNode = new DocumentType(d.getName(), d.getPublicIdentifier(), d.getSystemIdentifier(), "");
        xmlTreeBuilder.insert(d);
        assertEquals(doctypeNode.getName(), xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag", "name");
        Element element = new Element(Tag.valueOf("name"), "", null);
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
}