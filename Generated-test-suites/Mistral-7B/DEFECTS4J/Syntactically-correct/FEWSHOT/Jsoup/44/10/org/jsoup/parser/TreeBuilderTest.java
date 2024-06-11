package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {
    private TreeBuilder builder;
    private ParseErrorList errors;

    @Before
    public void setup() {
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false; // don't process tokens in tests
            }
        };
        errors = new ParseErrorList();
    }

    @Test
    public void testParseWithNullInput() {
        assertNull(builder.parse(null, ""));
    }

    @Test
    public void testParseWithEmptyInput() {
        Document doc = builder.parse("", "");
        assertNotNull(doc);
        assertEquals("", doc.body().text());
    }

    @Test
    public void testParseWithInputAndBaseUri() {
        String input = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseWithInputAndBaseUriAndErrors() {
        String input = "<html><head><title>Test</title></head><body><p>Hello World<unsupportedTag></p></body></html>";
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri, errors);
        assertNotNull(doc);
        assertFalse(errors.hasErrors());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseWithNullBaseUri() {
        String input = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        assertNull(builder.parse(input, null));
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullErrors() {
        String input = "<html><head><title>Test</title></head><body><p>Hello World<unsupportedTag></p></body></html>";
        builder.parse(input, "", (ParseErrorList) null);
    }

    @Test
    public void testProcessStartTag() {
        Element element = new Element("div");
        element.setAttribute("class", "test");
        builder.stack.add(element);
        Token token = new Token.StartTag().name("div");
        builder.start.reset();
        builder.start.nameAttr("div", new Attributes().put("class", "test"));
        assertTrue(builder.processStartTag(builder.start.name()));
        assertEquals(element, builder.currentElement());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        Element element = new Element("div");
        element.setAttribute("class", "test");
        builder.stack.add(element);
        Token token = new Token.StartTag().name("div").attributes(new Attributes().put("class", "test"));
        assertTrue(builder.processStartTag(token.name(), token.attributes()));
        assertEquals(element, builder.currentElement());
    }

    @Test
    public void testProcessEndTag() {
        Element element = new Element("div");
        element.setAttribute("class", "test");
        builder.stack.add(element);
        Token token = new Token.EndTag().name("div");
        assertTrue(builder.processEndTag(token.name()));
        assertEquals(builder.stack.size() - 1, builder.stack.size());
        assertNull(builder.currentElement());
    }
}