package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Element element = new Element(token.name);
                    stack.add(element);
                    doc.body().add(element);
                    return true;
                }
                return false;
            }
        };

        StringBuilder input = new StringBuilder();
        input.append("<html><body><div><p>Hello World</p></div></body></html>");

        Document doc = builder.parse(input.toString(), "http://example.com");
        assertEquals("html", doc.tagName());
        assertEquals(5, doc.childNodeSize());

        Element html = doc.body();
        assertEquals("body", html.tagName());
        assertEquals(4, html.childNodeSize());

        Element div = html.getFirstChild();
        assertEquals("div", div.tagName());
        assertEquals(1, div.childNodeSize());

        Element p = div.getFirstChild();
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Attributes attrs = new Attributes();
                    token.setAttribute(attrs);
                    Element element = new Element(token.name, attrs);
                    stack.add(element);
                    doc.body().add(element);
                    return true;
                }
                return false;
            }
        };

        StringBuilder input = new StringBuilder();
        input.append("<html><body><div id='div1' class='class1'><p id='p1'>Hello World</p></div></body></html>");

        Document doc = builder.parse(input.toString(), "http://example.com");
        assertEquals("html", doc.tagName());
        assertEquals(5, doc.childNodeSize());

        Element html = doc.body();
        assertEquals("body", html.tagName());
        assertEquals(4, html.childNodeSize());

        Element div = html.getFirstChild();
        assertEquals("div", div.tagName());
        assertEquals(1, div.childNodeSize());

        Element p = div.getFirstChild();
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());

        assertEquals("div", div.tagName());
        assertEquals("class1", div.classAttribute());
        assertEquals("id='div1'", div.attributes().toString());

        assertEquals("p", p.tagName());
        assertEquals("class1", p.classAttribute());
        assertEquals("id='p1'", p.attributes().toString());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    String name = token.name;
                    Element element = currentElement();
                    if (element != null && element.tagName().equals(name)) {
                        stack.remove(element);
                        doc.body().remove(element);
                        return true;
                    }
                    return false;
                }
                return false;
            }
        };

        StringBuilder input = new StringBuilder();
        input.append("<html><body><div><p>Hello World</p></div></body></html>");

        Document doc = builder.parse(input.toString(), "http://example.com");
        assertEquals("html", doc.tagName());
        assertEquals(5, doc.childNodeSize());

        Element html = doc.body();
        assertEquals("body", html.tagName());
        assertEquals(4, html.childNodeSize());

        Element div = html.getFirstChild();
        assertEquals("div", div.tagName());
        assertEquals(1, div.childNodeSize());

        Element p = div.getFirstChild();
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());

        builder.processEndTag("p");
        assertEquals(3, html.childNodeSize());

        builder.processEndTag("div");
        assertEquals(2, html.childNodeSize());

        builder.processEndTag("html");
        assertEquals(1, doc.childNodeSize());
    }

    @Test(expected = AssertionError.class)
    public void testProcessEndTagMismatch() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    String name = token.name;
                    Element element = currentElement();
                    if (element != null && element.tagName().equals(name)) {
                        stack.remove(element);
                        doc.body().remove(element);
                        return true;
                    }
                    throw new AssertionError("Mismatched end tag: " + name);
                }
                return false;
            }
        };

        StringBuilder input = new StringBuilder();
        input.append("<html><body><div><p>Hello World</p></p></div></body></html>");

        Document doc = builder.parse(input.toString(), "http://example.com");
    }
}