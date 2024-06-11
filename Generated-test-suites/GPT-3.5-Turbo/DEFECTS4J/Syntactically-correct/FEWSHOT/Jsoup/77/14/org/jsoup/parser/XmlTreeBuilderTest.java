package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList parseErrorList;
    private ParseSettings parseSettings;
    private Document doc;
    private String baseUri;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parseErrorList = ParseErrorList.noTracking();
        parseSettings = ParseSettings.preserveCase;
        baseUri = "http://www.example.com";
        doc = new Document(baseUri);
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        StringReader input = new StringReader("");
        Document parsedDoc = xmlTreeBuilder.parse(input, baseUri);

        assertEquals(doc, parsedDoc);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "";
        Document parsedDoc = xmlTreeBuilder.parse(input, baseUri);

        assertEquals(doc, parsedDoc);
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("");
        xmlTreeBuilder.initialiseParse(input, baseUri, parseErrorList, parseSettings);

        assertEquals(doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", parseSettings);
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals(Tag.valueOf("tag", parseSettings), element.tag());
        assertFalse(element.isBlock());
        assertFalse(element.isInline());
        assertFalse(element.isKnownTag());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);

        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        Comment comment = new Comment("comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(comment, xmlTreeBuilder.stack.get(0).childNodes().get(0));
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("text");
        TextNode textNode = new TextNode("text");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(textNode, xmlTreeBuilder.stack.get(0).childNodes().get(0));
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        DocumentType doctypeNode = new DocumentType("html", "", "");
        
        xmlTreeBuilder.insert(doctypeToken);

        assertEquals(doctypeNode, xmlTreeBuilder.stack.get(0).childNodes().get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>text</tag>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parseErrorList, parseSettings);

        assertEquals(1, nodes.size());
        assertEquals(new Element(Tag.valueOf("tag", parseSettings), baseUri), nodes.get(0));
    }
}