package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CleanerTest {
    private static final Whitelist DEFAULT_WHITELIST = Whitelist.simpleTextOnlyWhitelist();
    private static final String TEST_HTML = "<html><head></head><body><div id='test' class='test' data-test='test'><p>Test <span id='span'>Content</span></p></div></body></html>";

    @Test
    public void testCreateCleanerWithNullWhitelistThrowsException() {
        assertThrows(NullPointerException.class, () -> new Cleaner(null));
    }

    @Test
    public void testCreateCleaner() {
        Whitelist customWhitelist = Whitelist.simpleTextAndHtmlWhitelist();
        Cleaner cleaner = new Cleaner(customWhitelist);
        assertEquals(customWhitelist, cleaner.whitelist);
    }

    @Test
    public void testCleanDocumentWithNullDocumentThrowsException() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        assertThrows(NullPointerException.class, () -> cleaner.clean(null));
    }

    @Test
    public void testCleanDocumentWithEmptyDocumentReturnsEmptyDocument() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Document.createShell("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals(0, cleanDocument.body().childNodes().size());
    }

    @Test
    public void testCleanDocumentWithValidDocumentReturnsCleanDocument() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse(TEST_HTML);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);

        Element cleanBody = cleanDocument.body();
        assertEquals(1, cleanBody.childNodes().size());
        Element cleanDiv = cleanBody.childNodes().get(0);
        assertEquals("div", cleanDiv.tagName());
        assertEquals("test", cleanDiv.classes().get(0));
        assertEquals(0, cleanDiv.attributes().size());
        assertEquals("Test Content", cleanDiv.text());

        Element span = cleanDiv.getElementsByTag("span").get(0);
        assertEquals("span", span.tagName());
        assertEquals(0, span.attributes().size());
        assertEquals("Content", span.text());
    }

    @Test
    public void testCleanDocumentWithInvalidDocumentReturnsCleanDocumentButValidationFailed() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals(false, cleaner.isValid(dirtyDocument));

        Element body = cleanDocument.body();
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testIsValidDocumentWithNullDocumentReturnsFalse() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        assertFalse(cleaner.isValid(null));
    }

    @Test
    public void testIsValidDocumentWithEmptyDocumentReturnsTrue() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Document.createShell("");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidDocumentWithValidDocumentReturnsTrue() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse(TEST_HTML);
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidDocumentWithInvalidDocumentReturnsFalse() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidDocumentWithWhitelistRemovesScriptTag() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testIsValidDocumentWithWhitelistRemovesScriptElement() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script id='test'></script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testIsValidDocumentWithWhitelistRemovesStyleTag() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head><style>body {color: red;}</style></head><body></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element head = cleanDocument.head();
        assertEquals(0, head.childNodes().size());
    }

    @Test
    public void testIsValidDocumentWithWhitelistRemovesStyleElement() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><style id='test'></style></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testIsValidDocumentWithWhitelistRemovesScriptAttribute() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script src='test.js'></script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testIsValidDocumentWithWhitelistRemovesScriptSrcAttribute() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script src='test.js'></script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testIsValidDocumentWithWhitelistRemovesEventAttributes() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><button onclick='alert(\"XSS\")'>Test</button></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        Element button = body.getElementsByTag("button").get(0);
        assertEquals(0, button.attributes().size());
    }

    @Test
    public void testIsValidDocumentWithWhitelistRemovesEventAttributeValue() {
        Cleaner cleaner = new Cleaner(DEFAULT_WHITELIST);
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><button onclick='alert(\"XSS\")'>Test</button></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        Element button = body.getElementsByTag("button").get(0);
        assertEquals(0, button.children().size());
    }

    @Test
    public void testIsValidDocumentWithCustomWhitelistAllowsSpecificAttributes() {
        Set<Attribute> allowedAttributes = new HashSet<>();
        allowedAttributes.add(new Attribute("data-test", "test"));
        Whitelist customWhitelist = Whitelist.simpleTextAndHtmlWithAttributes(allowedAttributes);
        Cleaner cleaner = new Cleaner(customWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><head></head><body id='test' data-test='test'><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertEquals("test", body.id());
        assertEquals(1, body.childNodes().size());
        Element script = body.childNodes().get(0);
        assertEquals(0, script.childNodes().size());
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleanDocumentWithCustomWhitelistAllowsSpecificElements() {
        Whitelist customWhitelist = Whitelist.simpleTextAndHtmlWithTags("div", "span");
        Cleaner cleaner = new Cleaner(customWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script><div id='test' class='test' data-test='test'><p>Test <span id='span'>Content</span></p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();

        Element div = body.getElementsByTag("div").get(0);
        assertEquals("div", div.tagName());
        assertEquals("test", div.classes().get(0));
        assertEquals("test", div.attributes().get("data-test"));
        assertEquals(1, div.childNodes().size());
        Element span = div.getElementsByTag("span").get(0);
        assertEquals("span", span.tagName());
        assertEquals("Content", span.text());
    }

    @Test
    public void testCleanDocumentWithCustomWhitelistRemovesDisallowedElements() {
        Whitelist customWhitelist = Whitelist.simpleTextAndHtmlWithTags("div", "span");
        Cleaner cleaner = new Cleaner(customWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script><script id='test'></script><div id='test' class='test' data-test='test'><p>Test <span id='span'>Content</span></p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();

        assertEquals(1, body.childNodes().size());
        Element div = body.getElementsByTag("div").get(0);
        assertEquals("div", div.tagName());
        assertEquals("test", div.classes().get(0));
        assertEquals("test", div.attributes().get("data-test"));
        assertEquals(1, div.childNodes().size());
        Element span = div.getElementsByTag("span").get(0);
        assertEquals("span", span.tagName());
        assertEquals("Content", span.text());
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleanDocumentWithCustomWhitelistRemovesDisallowedAttributes() {
        Whitelist customWhitelist = Whitelist.simpleTextAndHtmlWithTags("div", "span");
        Cleaner cleaner = new Cleaner(customWhitelist);

        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script><div id='test' class='test' data-test='test' data-other='other'><p>Test <span id='span' class='otherSpan'>Content</span></p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();

        Element div = body.getElementsByTag("div").get(0);
        assertEquals("div", div.tagName());
        assertEquals("test", div.classes().get(0));
        assertEquals(0, div.attributes().size());
        assertEquals(1, div.childNodes().size());
        Element span = div.getElementsByTag("span").get(0);
        assertEquals("span", span.tagName());
        assertEquals("Content", span.text());
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    private void assertThrows(Class<? extends Throwable> expectedExceptionClass, Runnable runnable) {
        try {
            runnable.run();
            fail("Expected exception not thrown.");
        } catch (Throwable thrownException) {
            if (expectedExceptionClass.isInstance(thrownException)) {
                return;
            }
            throw new RuntimeException("Unexpected exception thrown: " + thrownException.getClass().getName(), thrownException);
        }
    }
}