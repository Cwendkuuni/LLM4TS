package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.TreeBuilder;

public class TreeBuilderTest {

    @Test
    public void testParseWithEmptyInput() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        Document doc = treeBuilder.parse("", "http://www.example.com");
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertNull(doc.body());
    }

    @Test
    public void testParseWithSimpleInput() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                if ("html".equals(name)) {
                    return currentElement().setTagName("html");
                }
                if ("head".equals(name)) {
                    Element head = currentElement().appendElement("head");
                    return head.setTagName("head");
                }
                if ("title".equals(name)) {
                    return currentElement().setTagName("title");
                }
                if ("body".equals(name)) {
                    return currentElement().setTagName("body");
                }
                return super.processStartTag(name);
            }
        };
        Document doc = treeBuilder.parse("<html><head><title>Test</title></head><body></body></html>", "http://www.example.com");
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Test", doc.title().text());
        assertNull(doc.body().text());
    }

    @Test
    public void testParseWithErrors() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            public void processEndTag(String name) {
                super.processEndTag(name);
                if ("body".equals(name)) {
                    throw new RuntimeException("Body tag encountered without a corresponding start tag");
                }
            }
        };
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse("<html><head><title>Test</title></head></html>", "http://www.example.com", errors);
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertNull(doc.body());
        assertNotNull(errors.getErrorCount());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                Element element = new Element(name);
                stack.add(element);
                return element.setTagName(name);
            }
        };
        treeBuilder.initialiseParse("<html><head><title>Test</title></head><body><p>Test</p></body></html>", "http://www.example.com", null);
        Element root = treeBuilder.currentElement();
        assertNotNull(root);
        assertEquals("html", root.tagName());

        Element head = treeBuilder.currentElement();
        assertNotNull(head);
        assertEquals("head", head.tagName());

        Element title = treeBuilder.currentElement();
        assertNotNull(title);
        assertEquals("title", title.tagName());

        Element body = treeBuilder.currentElement();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        Element p = body.appendElement("p");
        assertNotNull(p);
        assertEquals("p", p.tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                Element element = new Element(name);
                stack.add(element);
                return element.setTagName(name);
            }

            @Override
            protected void processStartTagAttributes(Attributes attrs) {
                if ("img".equals(currentElement().tagName())) {
                    currentElement().attr("src", "test.jpg");
                    currentElement().attr("alt", "Test Image");
                }
            }
        };
        treeBuilder.initialiseParse("<img src=\"test.jpg\" alt=\"Test Image\" />", "http://www.example.com", null);
        Element img = treeBuilder.currentElement();
        assertNotNull(img);
        assertEquals("img", img.tagName());
        assertEquals("test.jpg", img.attr("src"));
        assertEquals("Test Image", img.attr("alt"));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                Element element = new Element(name);
                stack.add(element);
                return element.setTagName(name);
            }

            @Override
            protected boolean processEndTag(String name) {
                Element parent = currentElement().parent();
                stack.remove(stack.size() - 1);
                parent.appendChild(stack.get(stack.size() - 1));
                return true;
            }
        };
        treeBuilder.initialiseParse("<html><head><title>Test</title></head><body><p><img src=\"test.jpg\" alt=\"Test Image\"></p></body></html>", "http://www.example.com", null);
        Element root = treeBuilder.currentElement();
        assertNotNull(root);
        assertEquals("html", root.tagName());

        Element head = root.getElementsByTagName("head").get(0);
        assertNotNull(head);
        assertEquals("head", head.tagName());

        Element title = head.getElementsByTagName("title").get(0);
        assertNotNull(title);
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());

        Element body = root.getElementsByTagName("body").get(0);
        assertNotNull(body);
        assertEquals("body", body.tagName());

        Element p = body.getElementsByTagName("p").get(0);
        assertNotNull(p);
        assertEquals("p", p.tagName());

        Element img = p.getElementsByTagName("img").get(0);
        assertNotNull(img);
        assertEquals("img", img.tagName());
        assertEquals("test.jpg", img.attr("src"));
        assertEquals("Test Image", img.attr("alt"));
    }
}