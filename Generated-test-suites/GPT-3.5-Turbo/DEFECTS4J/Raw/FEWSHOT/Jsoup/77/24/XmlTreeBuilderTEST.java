package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Document parsedDoc = xmlTreeBuilder.parse(null, "");
        assertNull(parsedDoc);

        parsedDoc = xmlTreeBuilder.parse(new StringReader("xml"), "http://example.com");
        assertNull(parsedDoc);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        Document parsedDoc = xmlTreeBuilder.parse("", "http://example.com");
        assertNull(parsedDoc);

        parsedDoc = xmlTreeBuilder.parse("xml", "http://example.com");
        assertNull(parsedDoc);
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("input");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        assertTrue(xmlTreeBuilder.process(new StartTag("tag", null)));

        assertTrue(xmlTreeBuilder.process(new EndTag("tag")));

        assertTrue(xmlTreeBuilder.process(new Comment("comment")));

        assertTrue(xmlTreeBuilder.process(new Character("data")));

        assertTrue(xmlTreeBuilder.process(new Doctype("doctype")));

        assertTrue(xmlTreeBuilder.process(new Token.EOF()));
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), "");

        xmlTreeBuilder.insertNode(element);

        assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        StartTag startTag = new StartTag("tag", null);

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", element.tagName());
        assertEquals("", element.baseUri());
        assertTrue(element.attributes().isEmpty());

        assertFalse(element.tag().isKnownTag());
    }

    @Test
    public void testInsertStartTagSelfClosing() {
        StartTag startTag = new StartTag("tag", null);
        startTag.setSelfClosing(true);

        Element element = xmlTreeBuilder.insert(startTag);

        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        Comment commentToken = new Comment("comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(commentToken.getData(), xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        Character characterToken = new Character("data");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(characterToken.getData(), xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertCDataCharacter() {
        Character characterToken = new Character("data");
        characterToken.setCData(true);

        xmlTreeBuilder.insert(characterToken);

        CDataNode cDataNode = (CDataNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(characterToken.getData(), cDataNode.getWholeData());
    }

    @Test
    public void testInsertDoctype() {
        Doctype doctype = new Doctype("doctype");

        xmlTreeBuilder.insert(doctype);

        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(doctype.getName(), documentType.name());
        assertEquals(doctype.getPublicIdentifier(), documentType.publicId());
        assertEquals(doctype.getSystemIdentifier(), documentType.systemId());
        assertEquals(doctype.getPubSysKey(), documentType.pubSysKey());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("tag"), "");
        Element element2 = new Element(Tag.valueOf("tag"), "");
        Element element3 = new Element(Tag.valueOf("tag"), "");

        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Element element1 = new Element(Tag.valueOf("tag1"), "");
        Element element2 = new Element(Tag.valueOf("tag2"), "");
        Element element3 = new Element(Tag.valueOf("tag3"), "");

        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(3, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "input";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList(), ParseSettings.preserveCase);

        assertTrue(nodes.isEmpty());
    }
}