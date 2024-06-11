package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TreeBuilderTest {

    @Test
    public void testParseWithEmptyInput() {
        ParseErrorList errors = new ParseErrorList();
        Document doc = new TreeBuilder().parse("", "", errors);
        assertNull(doc);
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseWithValidInput() {
        ParseErrorList errors = new ParseErrorList();
        String input = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = new TreeBuilder().parse(input, "", errors);
        assertNotNull(doc);
        assertEquals(1, doc.body().childNodeSize());
        Element pElement = doc.body().child(0);
        assertEquals(1, pElement.childNodeSize());
        assertEquals("Hello World", pElement.text());
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseWithErrors() {
        ParseErrorList errors = new ParseErrorList();
        String input = "<html><head title=foo><body<p>Hello World</p>";
        Document doc = new TreeBuilder().parse(input, "", errors);
        assertNull(doc);
        assertEquals(1, errors.size());
        assertEquals("tag name = 'head' is invalid: expected [HTMLElement], but was [HEADElement]", errors.get(0).toString());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Element element = new Element(token.name());
                    stack.add(element);
                    doc.body().appendElement(element);
                    return true;
                }
                return false;
            }
        };
        treeBuilder.initialiseParse(" <html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "", ParseErrorList.noTracking());
        treeBuilder.runParser();
        assertNotNull(treeBuilder.doc);
        assertEquals(1, treeBuilder.doc.body().childNodeSize());
        Element pElement = treeBuilder.doc.body().child(0);
        assertEquals(1, pElement.childNodeSize());
        assertEquals("Hello World", pElement.text());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    String name = token.name();
                    Element parent = currentElement();
                    if (parent != null) {
                        Element child = parent.child(0);
                        parent.removeChild(0);
                        parent.appendChild(child);
                    }
                    return true;
                }
                return false;
            }
        };
        treeBuilder.initialiseParse(" <html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "", ParseErrorList.noTracking());
        treeBuilder.runParser();
        assertNotNull(treeBuilder.doc);
        assertEquals(0, treeBuilder.doc.body().childNodeSize());
    }
}