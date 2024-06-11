package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.MockingCharacterReader;
import org.jsoup.helper.MockingParseErrorList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test class for TreeBuilder
 */
public class TreeBuilderTest {

    private TreeBuilder builder;
    private Document doc;
    private MockingCharacterReader reader;
    private MockingParseErrorList errors;

    @Before
    public void setUp() {
        reader = new MockingCharacterReader();
        errors = new MockingParseErrorList();
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                doc.body().appendElement(token.name());
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                doc.body().elementSibling(doc.body().childNodes().size() - 1).remove();
                return true;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        builder.initialiseParse("input", "baseUri", errors);
        assertNotNull(builder.doc);
        assertNotNull(builder.tokeniser);
        assertNotNull(builder.stack);
        assertNotNull(builder.errors);
    }

    @Test
    public void testParseWithNullInput() {
        assertThrows(NullPointerException.class, () -> builder.parse(null, "baseUri"));
    }

    @Test
    public void testParseWithNullBaseUri() {
        assertThrows(NullPointerException.class, () -> builder.parse("input", null));
    }

    @Test
    public void testParseWithNullErrors() {
        assertThrows(NullPointerException.class, () -> builder.parse("input", "baseUri", null));
    }

    @Test
    public void testParse() {
        builder.parse("input", "baseUri");
        assertNotNull(builder.doc);
    }

    @Test
    public void testRunParser() {
        builder.initialiseParse("input", "baseUri", errors);
        builder.runParser();
    }

    @Test(expected = IllegalStateException.class)
    public void testProcessStartTagWithNullName() {
        builder.processStartTag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTagWithEmptyName() {
        builder.processStartTag("", new Attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTagWithNullAttributes() {
        builder.processStartTag("name", null);
    }

    @Test
    public void testProcessStartTag() {
        builder.initialiseParse("input", "baseUri", errors);
        builder.processStartTag("tag", new Attributes());
        Element element = builder.currentElement();
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        builder.initialiseParse("input", "baseUri", errors);
        builder.processStartTag("tag", new Attributes());
        builder.processEndTag("tag");
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessEndTagWithNullName() {
        builder.processEndTag(null);
        assertFalse(builder.stack.isEmpty());
    }

    @Test
    public void testCurrentElement() {
        builder.initialiseParse("input", "baseUri", errors);
        builder.processStartTag("tag", new Attributes());
        Element element = builder.currentElement();
        assertEquals("tag", element.tagName());
        builder.processEndTag("tag");
        assertNull(builder.currentElement());
    }
}