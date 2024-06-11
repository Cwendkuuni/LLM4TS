package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.EOF;
import org.jsoup.parser.Token.EndTag;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);

        Document doc = xmlTreeBuilder.getDocument();
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
        assertTrue(xmlTreeBuilder.getStack().contains(doc));
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);

        Token.StartTag startTag = new StartTag("tag", null);
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", element.tagName());
        assertEquals(null, element.baseUri());
        assertEquals(0, element.attributes().size());
        assertFalse(element.isKnownTag());
        assertFalse(element.isSelfClosing());
        assertTrue(xmlTreeBuilder.getStack().contains(element));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);

        Token.StartTag startTag = new StartTag("tag", null);
        Element element = xmlTreeBuilder.insert(startTag);

        Token.EndTag endTag = new EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.getStack().contains(element));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);

        Token.Comment commentToken = new Comment("comment", "");
        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        Comment comment = (Comment) childNodes.get(0);
        assertEquals("comment", comment.getData());
        assertEquals("", comment.baseUri());
        assertFalse(comment.bogus());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);

        Token.Character characterToken = new Character("character");
        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals("character", textNode.getWholeText());
        assertEquals("", textNode.baseUri());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);

        Token.Doctype doctype = new Doctype("name", "public", "system");
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNodes.get(0);
        assertEquals("name", documentType.getName());
        assertEquals("public", documentType.getPublicIdentifier());
        assertEquals("system", documentType.getSystemIdentifier());
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList parseErrorList = new ParseErrorList();

        List<Node> childNodes = xmlTreeBuilder.parseFragment("<div>fragment</div>", "", parseErrorList);

        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element element = (Element) childNodes.get(0);
        assertEquals("div", element.tagName());
        assertEquals("", element.baseUri());
        assertEquals(0, element.attributes().size());
        assertFalse(element.isKnownTag());
        assertFalse(element.isSelfClosing());
    }
}