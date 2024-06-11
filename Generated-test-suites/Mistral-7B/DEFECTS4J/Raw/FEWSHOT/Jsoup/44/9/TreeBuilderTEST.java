package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TreeBuilderTest {

    @Test
    public void testParseEmptyInput() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        Document doc = builder.parse("", "http://example.com");
        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testParseSingleTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    processStartTag(token.name());
                    return true;
                }
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                Element element = new Element(name);
                stack.add(element);
                doc.body().append(element);
                return true;
            }
        };

        Document doc = builder.parse("<html/>", "http://example.com");
        Element html = doc.body();
        assertNotNull(html);
        assertTrue(html.tagName().equals("html"));
        assertTrue(html.children().isEmpty());
    }

    @Test
    public void testParseMultipleTags() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    processStartTag(token.name());
                    return true;
                }
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                Element element = new Element(name);
                stack.add(element);
                doc.body().append(element);
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                Element parent = currentElement();
                if (parent != null && parent.tagName().equals(name)) {
                    stack.remove(stack.size() - 1);
                    return true;
                }
                return false;
            }
        };

        Document doc = builder.parse("<html><body><p>Hello, world!</p></body></html>", "http://example.com");
        Element html = doc.body();
        assertNotNull(html);
        assertTrue(html.tagName().equals("body"));
        Element p = html.firstChild();
        assertNotNull(p);
        assertTrue(p.tagName().equals("p"));
        assertTrue(p.text().equals("Hello, world!"));
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullInput() {
        new TreeBuilder().parse(null, "http://example.com");
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullBaseUri() {
        new TreeBuilder().parse("", null);
    }
}