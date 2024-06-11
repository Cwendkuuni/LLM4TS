package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.XmlTreeBuilder;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;
    private Document doc;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
        doc = builder.parse(new StringReader("<root><tag attr=\"value\"></tag></root>"), "http://localhost");
    }

    @After
    public void tearDown() {
        doc = null;
        builder = null;
    }

    // Test for defaultSettings() method
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    // Test for parse() methods
    @Test
    public void testParseWithReaderAndBaseUri() {
        Document doc = builder.parse(new StringReader("<root><tag attr=\"value\"></tag></root>"), "http://localhost");
        assertNotNull(doc);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        Document doc = builder.parse("<root><tag attr=\"value\"></tag></root>", "http://localhost");
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        builder.parse(new StringReader("<root></root>"), null);
    }

    // Test for initialiseParse() method
    @Test
    public void testInitialiseParse() {
        String input = "<root><tag attr=\"value\"></tag></root>";
        String baseUri = "http://localhost";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(new StringReader(input), baseUri, errors, settings);

        assertEquals(1, stack.size());
        assertEquals(doc, stack.peek());
    }

    // Test for process() method
    @Test
    public void testProcessStartTag() {
        Token startTag = new Token.StartTag("tag", null, null, 0, 0, 0, 0);
        Element tagElement = builder.insert(startTag);
        assertTrue(tagElement != null);
    }

    @Test
    public void testProcessEndTag() {
        Token endTag = new Token.EndTag("tag", null, null, 0, 0, 0, 0);
        builder.popStackToClose(endTag);
        assertFalse(stack.isEmpty());
    }

    // Test for insertNode() method
    @Test
    public void testInsertNode() {
        Node node = new Element("tag", "http://localhost");
        builder.insertNode(node);
        assertEquals(node, currentElement().getLastChild());
    }

    @Test
    public void testInsertComment() {
        Token commentToken = new Token.Comment("comment");
        Comment comment = new Comment("comment");
        builder.insert(commentToken);
        assertEquals(comment, doc.childNodes().get(0));
    }

    @Test
    public void testInsertCharacter() {
        Token character = new Token.Character('a');
        TextNode textNode = new TextNode("a");
        builder.insert(character);
        assertEquals(textNode, doc.childNodes().get(1));
    }

    @Test
    public void testInsertDoctype() {
        Token doctype = new Token.Doctype("doctype");
        DocumentType doctypeNode = new DocumentType("doctype", null, null);
        builder.insert(doctype);
        assertEquals(doctypeNode, doc.childNodes().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateNullInput() {
        Validate.validateNullInput((Object) null);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag attr=\"value\"></tag>";
        List<Node> nodes = builder.parseFragment(inputFragment, "http://localhost", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(1, nodes.size());
        assertEquals(new Element("tag", "http://localhost"), nodes.get(0));
    }

    @Test
    public void testPopStackToCloseNullEndTag() {
        Token endTag = null;
        builder.popStackToClose(endTag);
        assertFalse(stack.isEmpty());
    }
}