package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader input = new StringReader("<test>data</test>");
        Document result = builder.parse(input, "https://www.example.com");
        assertNotNull(result);
    }

    @Test
    public void testParseWithString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<test>data</test>";
        Document result = builder.parse(input, "https://www.example.com");
        assertNotNull(result);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader input = new StringReader("<test>data</test>");
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse(input, "https://www.example.com", errors, settings);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.currentElement().outputSettings().syntax());
    }

    @Test
    public void testProcessWithStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("test", "", "");
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessWithEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.EndTag("test");
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessWithComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Comment("comment");
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessWithCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character("data", false);
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessWithCDataCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character("data", true);
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessWithDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype token = new Token.Doctype("html", "", "");
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test(expected = AssertionError.class)
    public void testProcessWithUnexpectedTokenType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.EOF();
        builder.process(token);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = new Element("test");
        builder.insertNode(node);
        assertEquals(node, builder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test", "", "");
        Element result = builder.insert(startTag);
        assertNotNull(result);
    }

    @Test
    public void testInsertEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("test");
        builder.insert(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        builder.insert(commentToken);
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character token = new Token.Character("data", false);
        builder.insert(token);
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertCDataCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character token = new Token.Character("data", true);
        builder.insert(token);
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype token = new Token.Doctype("html", "", "");
        builder.insert(token);
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element("test"));
        builder.stack.add(new Element("test"));
        Token.EndTag endTag = new Token.EndTag("test");
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<test>data</test>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> result = builder.parseFragment(inputFragment, "https://www.example.com", errors, settings);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}