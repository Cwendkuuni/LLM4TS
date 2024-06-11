package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for TreeBuilder.
 *
 * @author your_name
 */
public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private CharacterReader characterReader;
    private Tokeniser tokeniser;
    private Document doc;
    private ArrayList<Element> stack;
    private ParseErrorList errors;

    @Before
    public void setup() {
        characterReader = mock(CharacterReader.class);
        tokeniser = mock(Tokeniser.class);
        doc = mock(Document.class);
        stack = new ArrayList<Element>(32);
        errors = ParseErrorList.noTracking();
        treeBuilder = new TreeBuilder() {
            @Override
            protected void initialiseParse(String input, String baseUri, ParseErrorList errors) {
                TreeBuilder.super.initialiseParse(input, baseUri, errors);
                when(characterReader.read()).thenCallRealMethod();
                when(tokeniser.read()).thenCallRealMethod();
                when(doc.createElement(anyString())).then(invocation -> new Element("dummy"));
            }

            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        treeBuilder.initialiseParse("input", "baseUri", errors);
    }

    @Test
    public void testParseWithNullInput() {
        Validate.notNull(treeBuilder, "TreeBuilder must not be null");
        try {
            treeBuilder.parse(null, "baseUri");
            fail("Expected ParseException not thrown.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithNullBaseUri() {
        Validate.notNull(treeBuilder, "TreeBuilder must not be null");
        try {
            treeBuilder.parse("input", null);
            fail("Expected ParseException not thrown.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithEmptyInput() {
        Validate.notNull(treeBuilder, "TreeBuilder must not be null");
        Document doc = treeBuilder.parse("", "baseUri");
        assertNull(doc);
        assertEquals(0, stack.size());
    }

    @Test
    public void testProcessStartTagWithNullName() {
        Validate.notNull(treeBuilder, "TreeBuilder must not be null");
        treeBuilder.processStartTag(null, new Attributes());
        fail("Expected IllegalStateException not thrown.");
    }

    @Test
    public void testProcessStartTagWithEmptyName() {
        Validate.notNull(treeBuilder, "TreeBuilder must not be null");
        StartTag startTag = new StartTag("", new Attributes());
        treeBuilder.processStartTag(startTag.name());
        assertFalse(treeBuilder.process(startTag));
        assertEquals(1, stack.size());
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("class", "class1 class2");
        startTag = new StartTag(name, attrs);
        treeBuilder.processStartTag(name, attrs);
        Element element = stack.get(stack.size() - 1);
        assertEquals(name, element.tagName());
        assertEquals(attrs.toString(), element.attributes().toString());
        assertTrue(treeBuilder.process(startTag));
        assertEquals(1, stack.size());
    }

    @Test
    public void testProcessEndTagWithNullName() {
        Validate.notNull(treeBuilder, "TreeBuilder must not be null");
        treeBuilder.processEndTag(null);
        fail("Expected IllegalStateException not thrown.");
    }

    @Test
    public void testProcessEndTagWithEmptyName() {
        Validate.notNull(treeBuilder, "TreeBuilder must not be null");
        EndTag endTag = new EndTag("");
        treeBuilder.processEndTag(endTag.name());
        assertFalse(treeBuilder.process(endTag));
        assertEquals(1, stack.size());
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";
        treeBuilder.processStartTag(name);
        EndTag endTag = new EndTag(name);
        treeBuilder.processEndTag(name);
        assertEquals(0, stack.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testProcessEndTagWithoutStartTag() {
        treeBuilder.processEndTag("div");
    }

    @Test
    public void testParseWithErrors() {
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse("input", "baseUri", errors);
        assertTrue(doc != null || errors.size() > 0);
        assertEquals(errors.size(), stack.size());
    }
}