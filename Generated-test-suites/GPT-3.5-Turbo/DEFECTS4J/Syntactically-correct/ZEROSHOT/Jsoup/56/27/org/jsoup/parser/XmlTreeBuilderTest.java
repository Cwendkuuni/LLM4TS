package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String input;
    private String baseUri;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        input = "<root><element1 attribute1=\"value1\"></element1><element2></element2></root>";
        baseUri = "http://example.com";
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(parseSettings);
        assertEquals(parseSettings, ParseSettings.preserveCase);
    }

    @Test
    public void testParse() {
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(settings, xmlTreeBuilder.getSettings());
        assertNotNull(xmlTreeBuilder.getStack());
        assertNotNull(xmlTreeBuilder.getDoc());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("element1", "attribute1=value1", false);
        Element element = xmlTreeBuilder.processStartTag(startTag);
        assertNotNull(element);
        assertEquals("element1", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
        assertTrue(element.hasAttr("attribute1"));
        assertEquals("value1", element.attr("attribute1"));
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("element1");
        Element element = new Element(Tag.valueOf("element1"), "http://example.com");
        xmlTreeBuilder.getStack().add(element);
        xmlTreeBuilder.processEndTag(endTag);
        assertFalse(xmlTreeBuilder.getStack().contains(element));
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        Comment commentNode = xmlTreeBuilder.processComment(comment);
        assertNotNull(commentNode);
        assertEquals(comment.getData(), commentNode.getData());
        assertEquals("http://example.com", commentNode.baseUri());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        TextNode textNode = xmlTreeBuilder.processCharacter(characterToken);
        assertNotNull(textNode);
        assertEquals(characterToken.getData(), textNode.getWholeText());
        assertEquals("http://example.com", textNode.baseUri());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        DocumentType documentType = xmlTreeBuilder.processDoctype(doctype);
        assertNotNull(documentType);
        assertEquals(doctype.getName(), documentType.getName());
        assertEquals(doctype.getPublicIdentifier(), documentType.getPublicId());
        assertEquals(doctype.getSystemIdentifier(), documentType.getSystemId());
        assertEquals("http://example.com", documentType.baseUri());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("element1"), "http://example.com");
        xmlTreeBuilder.getStack().add(element);
        Element parent = new Element(Tag.valueOf("parent"), "http://example.com");
        parent.appendChild(element);
        assertEquals(parent, element.parent());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("element1", "attribute1=value1", false);
        Element element = xmlTreeBuilder.insertStartTag(startTag);
        assertNotNull(element);
        assertEquals("element1", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
        assertTrue(element.hasAttr("attribute1"));
        assertEquals("value1", element.attr("attribute1"));
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        Comment commentNode = xmlTreeBuilder.insertComment(comment);
        assertNotNull(commentNode);
        assertEquals(comment.getData(), commentNode.getData());
        assertEquals("http://example.com", commentNode.baseUri());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        TextNode textNode = xmlTreeBuilder.insertCharacter(characterToken);
        assertNotNull(textNode);
        assertEquals(characterToken.getData(), textNode.getWholeText());
        assertEquals("http://example.com", textNode.baseUri());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        DocumentType documentType = xmlTreeBuilder.insertDoctype(doctype);
        assertNotNull(documentType);
        assertEquals(doctype.getName(), documentType.getName());
        assertEquals(doctype.getPublicIdentifier(), documentType.getPublicId());
        assertEquals(doctype.getSystemIdentifier(), documentType.getSystemId());
        assertEquals("http://example.com", documentType.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("element1");

        Element element1 = new Element(Tag.valueOf("element1"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("element2"), "http://example.com");

        xmlTreeBuilder.getStack().add(element2);
        xmlTreeBuilder.getStack().add(element1);

        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.getStack().contains(element1));
        assertTrue(xmlTreeBuilder.getStack().contains(element2));
    }

    @Test
    public void testParseFragment() {
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);

        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
        assertEquals(1, nodes.size());
    }

}