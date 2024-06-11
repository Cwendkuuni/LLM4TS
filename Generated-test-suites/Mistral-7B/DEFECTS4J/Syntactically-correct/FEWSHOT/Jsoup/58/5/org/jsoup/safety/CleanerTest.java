package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = createDocument("<html><body><script>alert('XSS')</script></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = createDocument("<html><body><script>alert('XSS')</script></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidValidDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = createDocument("<html><body><p>Hello, world!</p></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleanDocumentWithEmptyBody() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = createDocument("<html><body></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testCleanDocumentWithWhitelistedElements() {
        Whitelist whitelist = new Whitelist()
                .addTag("p");
        Document dirtyDocument = createDocument("<html><body><p>Hello, world!</p><script>alert('XSS')</script></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals(1, cleanDocument.body().childNodeSize());
        Assert.assertTrue(cleanDocument.body().get(0) instanceof Element);
        Element pElement = (Element) cleanDocument.body().get(0);
        Assert.assertEquals("p", pElement.tagName());
    }

    @Test
    public void testCleanDocumentWithWhitelistedAttributes() {
        Whitelist whitelist = new Whitelist()
                .addTag("p")
                .addAttribute("class", true);
        Document dirtyDocument = createDocument("<html><body><p class='test'>Hello, world!</p></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals(1, cleanDocument.body().childNodeSize());
        Element pElement = (Element) cleanDocument.body().get(0);
        Assert.assertEquals("p", pElement.tagName());
        Assert.assertTrue(pElement.hasAttr("class"));
        Assert.assertEquals("test", pElement.classAttr());
    }

    @Test
    public void testCleanDocumentWithEnforcedAttributes() {
        Whitelist whitelist = new Whitelist()
                .addTag("p")
                .addAttribute("class", true)
                .addEnforcedAttribute("class", "test");
        Document dirtyDocument = createDocument("<html><body><p class='test'>Hello, world!</p></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals(1, cleanDocument.body().childNodeSize());
        Element pElement = (Element) cleanDocument.body().get(0);
        Assert.assertEquals("p", pElement.tagName());
        Assert.assertTrue(pElement.hasAttr("class"));
        Assert.assertEquals("test", pElement.classAttr());
    }

    @Test
    public void testCleanDocumentWithWhitelistedDataNodes() {
        Whitelist whitelist = new Whitelist()
                .addTag("script")
                .addTag("img");
        Document dirtyDocument = createDocument("<html><body><script>alert('XSS')</script><img src='image.png'></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals(1, cleanDocument.body().childNodeSize());
        Node node = cleanDocument.body().get(0);
        Assert.assertTrue(node instanceof Element || node instanceof DataNode);
        if (node instanceof Element) {
            Element imgElement = (Element) node;
            Assert.assertEquals("img", imgElement.tagName());
        } else {
            DataNode imgDataNode = (DataNode) node;
            Assert.assertEquals("img", imgDataNode.tagName());
        }
    }

    private Document createDocument(String html) {
        ParseErrorList parseErrors = new ParseErrorList();
        Parser parser = new Parser(parseErrors);
        Document document = parser.parseBodyFragment(html, "");
        Assert.assertEquals(0, parseErrors.size());
        return document;
    }
}