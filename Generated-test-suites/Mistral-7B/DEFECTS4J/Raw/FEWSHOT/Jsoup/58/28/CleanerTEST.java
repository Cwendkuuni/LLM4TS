package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><head></head><body><div id='test'>Hello <script>alert('XSS');</script></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><head><title>Test</title></head><body><div id='test'>Hello <script>alert('XSS');</script></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);

        dirtyDocument = createDocument("<html><head></head><body><div id='test'>Hello World</div></body></html>");
        isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCleanDocumentWithEmptyBody() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><head></head><body></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testCleanDocumentWithEmptyBodyAndScriptTag() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><head></head><body><script>alert('XSS');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testCleanDocumentWithBody() {
        Whitelist whitelist = new Whitelist.SimpleElementFilter() {
            @Override
            public boolean isSafe(String tag, Element context) {
                return tag.equals("div");
            }
        };
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><head></head><body><div id='test'>Hello World</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(1, cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().get(0) instanceof Element);
        assertEquals("div", cleanDocument.body().get(0).nodeName());
    }

    @Test
    public void testCleanDocumentWithBodyAndScriptTag() {
        Whitelist whitelist = new Whitelist.SimpleElementFilter() {
            @Override
            public boolean isSafe(String tag, Element context) {
                return tag.equals("script") && context.nodeName().equals("head");
            }
        };
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><head><title>Test</title></head><body><script>alert('XSS');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testCleanDocumentWithBodyAndScriptTagInBody() {
        Whitelist whitelist = new Whitelist.SimpleElementFilter() {
            @Override
            public boolean isSafe(String tag, Element context) {
                return tag.equals("script") && context.nodeName().equals("head");
            }
        };
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><head><title>Test</title></head><body><script>alert('XSS');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testCleanDocumentWithBodyAndScriptTagInBodyAndSafeScriptTag() {
        Whitelist whitelist = new Whitelist.SimpleElementFilter() {
            @Override
            public boolean isSafe(String tag, Element context) {
                return tag.equals("script") && (context.nodeName().equals("head") || context.nodeName().equals("body"));
            }
        };
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><head><title>Test</title></head><body><script>alert('XSS');</script><script src='safe.js'></script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(1, cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().get(0) instanceof Element);
        assertEquals("script", cleanDocument.body().get(0).nodeName());
    }

    @Test
    public void testCleanDocumentWithNestedScriptTag() {
        Whitelist whitelist = new Whitelist.SimpleElementFilter() {
            @Override
            public boolean isSafe(String tag, Element context) {
                return tag.equals("script") && context.nodeName().equals("head");
            }
        };
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><head><title>Test</title><script><script>alert('XSS');</script></script></head><body></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    private Document createDocument(String html) {
        try {
            Document doc = Jsoup.parse(html);
            return doc;
        } catch (Parser.MalformedAttributeException e) {
            throw new RuntimeException(e);
        }
    }
}