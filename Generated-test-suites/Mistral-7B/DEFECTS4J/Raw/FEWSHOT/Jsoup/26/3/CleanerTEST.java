package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.helper.ElementFilter;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {
    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.fromHtml("<html><body><h1>Test</h1><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element root = cleanDocument.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("<html><body>", root.outerHtml());
        assertFalse(root.hasText());
        assertFalse(root.childElements().hasText());
        assertFalse(root.childElements().select("script").hasText());
        assertFalse(root.childElements().select("script").isEmpty());
    }

    @Test
    public void testCleanEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.fromHtml("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("", cleanDocument.outerHtml());
    }

    @Test
    public void testCleanSafeDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.fromHtml("<html><body><p>Safe content</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><p>Safe content</p></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.fromHtml("<html><body><h1>Test</h1></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));

        Document dirtyDocumentWithScript = Document.fromHtml("<html><body><h1>Test</h1><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocumentWithScript));
    }

    @Test
    public void testIsValidWithWhitelist() {
        Whitelist whitelist = new Whitelist() {
            @Override
            protected boolean isSafeTag(String name, Element context, Element el) {
                return name.equals("h1") || name.equals("p");
            }

            @Override
            protected boolean isSafeAttribute(String tag, Element context, Attribute attr) {
                return false;
            }
        };
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.fromHtml("<html><body><h1 id='test' class='test' onclick='alert(\"XSS\")'>Test</h1></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));

        Document dirtyDocumentWithScript = Document.fromHtml("<html><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocumentWithScript));

        Document dirtyDocumentWithH1 = Document.fromHtml("<html><body><h1>Test</h1></body></html>");
        assertTrue(cleaner.isValid(dirtyDocumentWithH1));

        Document dirtyDocumentWithP = Document.fromHtml("<html><body><p>Test</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocumentWithP));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("div");
        source.appendText("Test");

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendChild(source);

        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("Test", dest.text());
        assertEquals("1", String.valueOf(numDiscarded));
    }

    @Test
    public void testCopySafeNodesWithScriptTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("script");
        source.attr("src", "javascript:alert('XSS')");

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendChild(source);

        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertNull(dest.children());
    }

    @Test
    public void testCopySafeNodesWithEmptyTextNode() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        TextNode sourceText = new TextNode(" ");

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendChild(sourceText);

        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(sourceText, dest);
        assertEquals("", dest.outerHtml());
        assertEquals("0", String.valueOf(numDiscarded));
    }

    @Test
    public void testCopySafeNodesWithMultipleTextNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        TextNode sourceText1 = new TextNode("Test1 ");
        TextNode sourceText2 = new TextNode("Test2");

        Element source = new Element("div");
        source.appendChild(sourceText1);
        source.appendChild(sourceText2);

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendChild(source);

        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        ElementFilter textFilter = new ElementFilter("text");
        List<Element> textList = textFilter.filter(dest.childElements());

        assertEquals(2, textList.size());
        assertEquals("Test1 Test2", textList.get(0).outerHtml());
        assertEquals("", textList.get(1).outerHtml());
        assertEquals("2", String.valueOf(numDiscarded));
    }

    @Test
    public void testCopySafeNodesWithAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("div");
        source.attr("class", "test");
        source.attr("id", "testID");

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendChild(source);

        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("class='test'", dest.attr("class"));
        assertEquals("testID", dest.attr("id"));
        assertEquals("0", String.valueOf(numDiscarded));
    }

    @Test
    public void testCopySafeNodesWithAttributesAndScriptTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("script");
        source.attr("src", "javascript:alert('XSS')");
        source.attr("id", "testID");

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendChild(source);

        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertNull(dest.children());
        assertNull(dest.attr("id"));
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("div");
        source.attr("class", "test");
        source.attr("id", "testID");
        source.attr("onclick", "alert('XSS')");

        Element dest = new Element(Tag.DIV, null, new Attributes());
        int numDiscarded = cleaner.createSafeElement(source, dest).numAttribsDiscarded;

        assertEquals("class", dest.attributeNames().get(0));
        assertEquals("test", dest.attribute(dest.attributeNames().get(0)).value());
        assertEquals(2, dest.attributeNames().size());
        assertEquals("0", String.valueOf(numDiscarded));
    }

    @Test
    public void testCreateSafeElementWithScriptTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("script");
        source.attr("src", "javascript:alert('XSS')");

        Element dest = new Element(Tag.DIV, null, new Attributes());
        int numDiscarded = cleaner.createSafeElement(source, dest).numAttribsDiscarded;

        assertEquals("", dest.outerHtml());
        assertEquals(1, numDiscarded);
    }
}