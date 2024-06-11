package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.IOException;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() throws IOException {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xml = "<root><element>Hello World</element></root>";
        Document doc = parser.parse(xml, null);
        assertNotNull(doc);
        assertEquals("root", doc.tagName());

        Element root = doc.getElementsByTag("root").first();
        Element element = root.getElementsByTag("element").first();
        assertEquals("element", element.tagName());
        assertEquals("Hello World", element.text());
    }

    @Test(expected = ParseException.class)
    public void testParseWithError() throws IOException {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xml = "<root><element/><element/><!>";
        parser.parse(xml, null);
        fail("ParseException expected");
    }

    @Test
    public void testParseFragment() throws IOException {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xmlFragment = "<element>Hello</element>";
        List<Node> fragment = parser.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.defaultSettings());

        Element element = (Element) fragment.get(0);
        assertEquals("element", element.tagName());
        assertEquals("Hello", element.text());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.StartTag.class, "<root>", null, null, null, null, 1, 1, 0, null);
        parser.process(token);

        Document doc = parser.doc;
        Element root = (Element) doc.child(0);
        assertEquals("root", root.tagName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.EndTag.class, "</root>", null, null, null, null, 1, 1, 0, null);
        parser.process(token);

        assertEquals(0, parser.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.Comment.class, "-- This is a comment --", null, null, null, null, 1, 1, 0, null);
        parser.process(token);

        Comment comment = (Comment) parser.stack.get(parser.stack.size() - 1);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.Character.class, "Hello", null, null, null, null, 1, 1, 0, null);
        parser.process(token);

        Text text = (Text) parser.stack.get(parser.stack.size() - 1);
        assertEquals("Hello", text.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.Doctype.class, "<!DOCTYPE root SYSTEM \"file:root.dtd\">", null, null, null, null, 1, 1, 0, null);
        parser.process(token);

        DocumentType doctype = (DocumentType) parser.stack.get(parser.stack.size() - 1);
        assertEquals("root", doctype.nodeName());
        assertEquals("file:root.dtd", doctype.systemId());
    }

    @Test
    public void testProcessSelfClosingTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new Token(Token.StartTag.class, "<root/>", null, null, null, null, 1, 1, 0, null);
        parser.process(token);

        Element root = (Element) parser.stack.get(parser.stack.size() - 1);
        assertEquals("root", root.tagName());
        assertEquals(0, parser.stack.size());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        parser.stack.add(new Element("root"));
        parser.stack.add(new Element("element"));
        parser.stack.add(new Element("element"));
        parser.popStackToClose(new Token.EndTag("element"));
        assertEquals(1, parser.stack.size());
        assertEquals("root", parser.stack.get(0).nodeName());
    }

    @Test
    public void testParseSettings() {
        ParseSettings settings = new ParseSettings();
        assertEquals(ParseSettings.Syntax.xml, settings.syntax());
    }

    @Test
    public void testParseWithInvalidSettings() {
        ParseSettings settings = new ParseSettings();
        settings.syntax(Document.OutputSettings.Syntax.html);
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xml = "<root></root>";

        try {
            parser.parse(xml, null, ParseErrorList.noTracking(), settings);
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Error parsing XML: not well-formed (invalid token)", e.getMessage());
        }
    }
}