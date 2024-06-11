package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCleanerWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().childNodes().isEmpty());
    }

    @Test
    public void testCleanerWithBasicWhitelist() {
        Whitelist basicWhitelist = Whitelist.simpleTags().addTag("div");
        Cleaner cleaner = new Cleaner(basicWhitelist);

        Document dirtyDocument = Document.createFromString(
                "<html><body>" +
                        "<div id=\"content\">Hello World</div>" +
                        "<script>alert('XSS')</script>" +
                        "</body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><div id=\"content\">Hello World</div></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testCleanerWithCustomWhitelist() {
        Whitelist customWhitelist = new Whitelist.SimpleElementHandler() {
            @Override
            public void handle(String name, int index) {
                if ("p".equals(name)) {
                    addTag("p", null, null);
                } else if ("script".equals(name)) {
                    addTag("noscript", null, null);
                } else {
                    super.handle(name, index);
                }
            }
        };
        Cleaner cleaner = new Cleaner(customWhitelist);

        Document dirtyDocument = Document.createFromString(
                "<html><body>" +
                        "<p>Hello World</p>" +
                        "<script>alert('XSS')</script>" +
                        "</body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><noscript></noscript><p>Hello World</p></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testIsValidWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Document.createFromString("<html><body></body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);

        assertTrue(valid);
    }

    @Test
    public void testIsValidWithBasicWhitelist() {
        Whitelist basicWhitelist = Whitelist.simpleTags();
        Cleaner cleaner = new Cleaner(basicWhitelist);

        Document dirtyDocument = Document.createFromString(
                "<html><body>" +
                        "<div id=\"content\">Hello World</div>" +
                        "</body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);

        assertTrue(valid);
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Whitelist basicWhitelist = Whitelist.simpleTags();
        Cleaner cleaner = new Cleaner(basicWhitelist);

        Document dirtyDocument = Document.createFromString(
                "<html><body>" +
                        "<div id=\"content\">" +
                        "<script>alert('XSS')</script>" +
                        "</div>" +
                        "</body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);

        assertFalse(valid);
    }

    @Test
    public void testIsValidWithCustomWhitelist() {
        Whitelist customWhitelist = new Whitelist.SimpleElementHandler() {
            @Override
            public void handle(String name, int index) {
                if ("p".equals(name)) {
                    addTag("p", null, null);
                } else {
                    super.handle(name, index);
                }
            }
        };
        Cleaner cleaner = new Cleaner(customWhitelist);

        Document dirtyDocument = Document.createFromString(
                "<html><body>" +
                        "<p>Hello World</p>" +
                        "<script>alert('XSS')</script>" +
                        "</body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);

        assertFalse(valid);
    }

    @Test
    public void testCopySafeNodesWithEmptyChildren() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        Element source = new Element("div");
        Element dest = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertSame(dest, source);
        assertTrue(numDiscarded == 0);
    }

    @Test
    public void testCopySafeNodesWithSingleChildTextNode() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        Element source = new Element("div");
        source.appendChild(new TextNode("Hello World"));
        Element dest = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("Hello World", dest.text());
        assertTrue(numDiscarded == 0);
    }

    @Test
    public void testCopySafeNodesWithSingleChildElement() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTags());

        Element source = new Element("div");
        source.appendChild(new Element("p").text("Hello World"));
        Element dest = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("<p>Hello World</p>", dest.html());
        assertTrue(numDiscarded == 0);
    }

    @Test
    public void testCopySafeNodesWithMultipleChildren() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTags());

        Element source = new Element("div");
        source.appendChild(new Element("p").text("Hello World 1"));
        source.appendChild(new Element("p").text("Hello World 2"));
        Element dest = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("<p>Hello World 1</p><p>Hello World 2</p>", dest.html());
        assertTrue(numDiscarded == 0);
    }

    @Test
    public void testCopySafeNodesWithInvalidChild() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTags());

        Element source = new Element("div");
        source.appendChild(new Element("script").text("alert('XSS')"));
        Element dest = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertTrue(numDiscarded > 0);
        assertTrue(dest.childNodes().isEmpty());
    }

    @Test
    public void testCreateSafeElementWithEmptyAttributes() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTags());

        Element source = new Element("p").attr("id", "content");
        Element dest = new Element("p");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("id=\"content\"", dest.attributes().toString());
        assertTrue(numDiscarded == 0);
    }

    @Test
    public void testCreateSafeElementWithValidAttributes() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTags());

        Element source = new Element("p").attr("id", "content").attr("class", "my-class");
        Element dest = new Element("p");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("id=\"content\" class=\"my-class\"", dest.attributes().toString());
        assertTrue(numDiscarded == 0);
    }

    @Test
    public void testCreateSafeElementWithInvalidAttributes() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTags());

        Element source = new Element("p").attr("style", "background-image: url(xss.js)");
        Element dest = new Element("p");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertTrue(numDiscarded > 0);
        assertTrue(dest.childNodes().isEmpty());
    }

    @Test
    public void testCreateSafeElementWithEmptyTextNode() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTags());

        Element source = new Element("p");
        source.appendChild(new TextNode(""));
        Element dest = new Element("p");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertTrue(numDiscarded == 0);
        assertNull(dest.childNodes());
    }

    @Test
    public void testCreateSafeElementWithTextNode() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTags());

        Element source = new Element("p");
        source.appendChild(new TextNode("Hello World"));
        Element dest = new Element("p");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("Hello World", dest.text());
        assertTrue(numDiscarded == 0);
    }

    @Test
    public void testCreateSafeElementWithMultipleTextNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleTags());

        Element source = new Element("p");
        source.appendChild(new TextNode("Hello World 1"));
        source.appendChild(new TextNode("Hello World 2"));
        Element dest = new Element("p");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("Hello World 1Hello World 2", dest.text());
        assertTrue(numDiscarded == 0);
    }
}