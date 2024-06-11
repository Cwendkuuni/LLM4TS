package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {

    private TreeBuilder builder;
    private Tokeniser tokeniser;
    private Document doc;

    @Before
    public void setUp() {
        tokeniser = mock(Tokeniser.class);
        builder = mock(TreeBuilder.class);
        when(builder.process(mock(Token.class))).thenAnswer(invocation -> true);
        doc = new Document("");
    }

    @Test
    public void testParseWithNullInput() {
        assertNull(builder.parse(null, ""));
    }

    @Test
    public void testParseWithEmptyInput() {
        builder.initialiseParse("", "", ParseErrorList.noTracking());
        builder.runParser();
        assertEquals(0, doc.body().childSize());
    }

    @Test
    public void testParseWithInput() {
        String input = "<html><head></head><body><div></div></body></html>";
        builder.initialiseParse(input, "", ParseErrorList.noTracking());
        builder.runParser();

        Element root = doc.rootElement();
        assertEquals("html", root.tagName());
        Element html = doc.select("html").first();
        assertEquals(2, html.childSize());
        Element head = html.select("head").first();
        assertEquals(0, head.childSize());
        Element body = html.select("body").first();
        assertEquals(1, body.childSize());
        Element div = body.select("div").first();
        assertEquals(0, div.childSize());
    }

    @Test
    public void testProcessStartTag() {
        String name = "html";
        assertEquals(true, builder.processStartTag(name));
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "testDiv");

        when(tokeniser.read()).thenReturn(new Token("div", new Attributes()));

        Element currentElement = mock(Element.class);
        when(builder.currentElement()).thenReturn(currentElement);

        assertEquals(true, builder.processStartTag(name, attrs));
        assertEquals("div", currentElement.tagName());
        assertEquals("testDiv", currentElement.attr("id"));
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";
        assertEquals(true, builder.processEndTag(name));
        assertEquals(null, builder.currentElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTagWithNullName() {
        builder.processStartTag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTagWithEmptyName() {
        builder.processStartTag("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTagWithNullAttributes() {
        builder.processStartTag("div", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessEndTagWithNullName() {
        builder.processEndTag(null);
    }

    @Test
    public void testProcessStartTagWithInvalidToken() {
        Token token = new Token("invalid", new Attributes());
        when(tokeniser.read()).thenReturn(token);
        assertFalse(builder.process(token));
        assertEquals(0, doc.body().childSize());
    }
}