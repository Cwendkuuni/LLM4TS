package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {
    private TreeBuilder builder;
    private ParseErrorList errors;

    @Before
    public void setup() {
        errors = new ParseErrorList();
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        builder.initialiseParse("<html><head><title>Test</title></head><body>", "http://www.test.com", errors);
    }

    @Test
    public void testParse() {
        Document doc = builder.parse("<html><head><title>Test</title></head><body>", "http://www.test.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
    }

    @Test
    public void testProcessStartTag() {
        assertTrue(builder.processStartTag("html"));
        assertEquals("html", currentElement().tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        builder.processStartTag("div", new Attributes() {{
            setAttribute("class", "test");
            setAttribute("id", "myDiv");
        }});
        Element element = currentElement();
        assertEquals("div", element.tagName());
        assertEquals("test", element.classes());
        assertEquals("myDiv", element.id());
    }

    @Test
    public void testProcessEndTag() {
        builder.processStartTag("html");
        builder.processStartTag("head");
        builder.processStartTag("title");
        builder.processEndTag("title");
        builder.processEndTag("head");
        builder.processEndTag("html");

        assertNull(currentElement());
    }
}