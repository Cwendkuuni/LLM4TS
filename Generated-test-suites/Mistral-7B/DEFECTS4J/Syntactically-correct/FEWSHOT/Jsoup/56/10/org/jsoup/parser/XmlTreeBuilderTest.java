package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testParse() {
        String xml = "<root><child attr=\"value\">Hello, World!</child></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://example.com/");

        assertNotNull(doc);
        assertEquals("root", doc.tagName());
        Element root = doc.child(0);
        assertEquals("child", root.tagName());
        assertEquals("http://example.com/", root.baseUri());
        assertEquals("value", root.attribute(0).getName());
        assertEquals("value", root.attribute(0).getValue());
        assertEquals("Hello, World!", root.child(0).text());
    }

    @Test
    public void testParseWithEmptyInput() {
        Document doc = XmlTreeBuilder.parse("", "http://example.com/");
        assertNotNull(doc);
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testParseWithErrors() {
        String invalidXml = "<root><child><!-- -->";
        ParseErrorList errors = ParseErrorList.getEmpty();
        Document doc = XmlTreeBuilder.parse(invalidXml, "http://example.com/", errors, ParseSettings.preserveCase);

        assertNotNull(doc);
        assertEquals(1, errors.size());
        assertEquals("Error parsing XML: unclosed comment", errors.get(0).getMessage());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer();
        ParseSettings settings = ParseSettings.preserveCase;
        tokenizer.setParser(builder);
        tokenizer.setSettings(settings);

        String xml = "<root><child attr=\"value\"/></root>";
        List<Token> tokens = tokenizer.tokenize(xml);

        assertEquals(13, tokens.size()); // 1 StartTag root, 1 EndTag root, 1 StartTag child, 1 EndTag child, 1 Attribute, 1 Character, 6 whitespace tokens, and 1 EOF token

        Token startTagChild = tokens.get(2);
        Element child = builder.insert(startTagChild);

        assertEquals("child", child.tagName());
        assertEquals("http://example.com/", child.baseUri());
        assertSame(stack.class, builder.stack.getClass());
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer();
        ParseSettings settings = ParseSettings.preserveCase;
        tokenizer.setParser(builder);
        tokenizer.setSettings(settings);

        String xml = "<root><child><!-- --></child></root>";
        List<Token> tokens = tokenizer.tokenize(xml);

        assertEquals(14, tokens.size()); // 1 StartTag root, 1 EndTag root, 1 StartTag child, 1 EndTag child, 1 Comment, 1 StartTag child, 1 EndTag child, 2 Attribute, 1 Character, 2 whitespace tokens, and 1 EOF token

        Token endTagChild = tokens.get(10);
        builder.popStackToClose(endTagChild.asEndTag());

        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><child attr=\"value\">Hello, World!</child></root>";
        Document doc = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com/", ParseErrorList.getEmpty(), ParseSettings.preserveCase);

        assertNotNull(doc);
        assertEquals("root", doc.tagName());
        Element root = doc.child(0);
        assertEquals("child", root.tagName());
        assertEquals("http://example.com/", root.baseUri());
        assertEquals("value", root.attribute(0).getName());
        assertEquals("value", root.attribute(0).getValue());
        assertEquals("Hello, World!", root.child(0).text());
    }

    @Test
    public void testParseFragmentWithErrors() {
        String xmlFragment = "<root><child><!-- -->";
        ParseErrorList errors = ParseErrorList.getEmpty();
        Document doc = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com/", errors, ParseSettings.preserveCase);

        assertNotNull(doc);
        assertEquals(1, errors.size());
        assertEquals("Error parsing XML: unclosed comment", errors.get(0).getMessage());
    }
}