package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseErrorList;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private String input;
    private String baseUri;
    private ParseErrorList errors;
    private Document doc;

    @Before
    public void setUp() {
        input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        baseUri = "http://example.com";
        errors = new ParseErrorList();
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        treeBuilder.initialiseParse(input, baseUri, errors);
    }

    @Test
    public void testParse() {
        treeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertNotNull(doc.title());
        assertNotNull(doc.head());
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullInput() {
        treeBuilder.parse(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        treeBuilder.parse(input, null);
    }

    @Test
    public void testParseWithErrors() {
        Validate.notNull(input, "input must not be null");
        Validate.notNull(baseUri, "baseUri must not be null");

        errors = new ParseErrorList();
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.parse(input, baseUri);

        assertNotNull(errors.size());
    }

    @Test
    public void testProcessStartTag() {
        treeBuilder.processStartTag("html");
        Element html = treeBuilder.currentElement();
        assertNotNull(html);
        assertNotNull(html.attributes());
        assertNull(html.children());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        treeBuilder.processStartTag("title", new Attributes() {{
            put("id", "test");
            put("class", "foo");
        }});

        Element title = treeBuilder.currentElement();
        assertNotNull(title);
        assertNotNull(title.attributes());
        assertNotNull(title.children());
        assertNull(title.text());

        assertEquals("test", title.id());
        assertEquals("foo", title.classes().get(0));
    }

    @Test
    public void testProcessEndTag() {
        treeBuilder.processStartTag("html");
        treeBuilder.processStartTag("head");
        treeBuilder.processEndTag("head");

        Element html = treeBuilder.currentElement();
        assertNotNull(html);
        Element head = html.child(0);
        assertNotNull(head);
        assertNull(head.children());
    }

    @Test
    public void testCurrentElement() {
        treeBuilder.processStartTag("html");
        treeBuilder.processStartTag("head");
        treeBuilder.processEndTag("head");
        treeBuilder.processStartTag("body");

        Element html = treeBuilder.currentElement();
        assertNotNull(html);
        Element head = html.child(0);
        assertNotNull(head);
        Element body = html.child(1);
        assertNotNull(body);

        assertNull(html.previousSibling());
        assertNull(head.previousSibling());
        assertNull(body.previousSibling());

        assertNull(head.nextSibling());
        assertNull(body.nextSibling());
        assertNull(html.nextSibling());
    }
}