import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.jsoup.safety.Cleaner;

import static org.junit.Assert.*;

public class CleanerTest {

    private static Cleaner cleaner;

    @Test
    public void testClean() {
        cleaner = new Cleaner(Whitelist.basic());

        // Typical use case: dirty document with safe tags and attributes
        Document dirtyDocument1 = Jsoup.parseBodyFragment("<div><p>Hello, world!</p></div>");
        Document cleanDocument1 = cleaner.clean(dirtyDocument1);
        Element bodyElement1 = cleanDocument1.body();
        assertEquals("div", bodyElement1.child(0).tagName());
        assertEquals("p", bodyElement1.child(0).child(0).tagName());
        assertEquals("Hello, world!", bodyElement1.child(0).child(0).text());

        // Edge case: dirty document without any tags or attributes
        Document dirtyDocument2 = Jsoup.parseBodyFragment("");
        Document cleanDocument2 = cleaner.clean(dirtyDocument2);
        Element bodyElement2 = cleanDocument2.body();
        assertTrue(bodyElement2.children().isEmpty());

        // Error scenario: null dirty document
        Document dirtyDocument3 = null;
        try {
            cleaner.clean(dirtyDocument3);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testIsValid() {
        cleaner = new Cleaner(Whitelist.basic());

        // Typical use case: dirty document with only safe tags and attributes
        Document dirtyDocument1 = Jsoup.parseBodyFragment("<h1>Welcome</h1><p>How can I help you today?</p>");
        assertTrue(cleaner.isValid(dirtyDocument1));

        // Edge case: dirty document with a single unsafe tag
        Document dirtyDocument2 = Jsoup.parseBodyFragment("<h1>Welcome</h1><script>alert('XSS');</script>");
        assertFalse(cleaner.isValid(dirtyDocument2));

        // Error scenario: null dirty document
        Document dirtyDocument3 = null;
        try {
            cleaner.isValid(dirtyDocument3);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCopySafeNodes() {
        cleaner = new Cleaner(Whitelist.basic());

        // Typical use case: source element with safe tags and attributes
        Element source1 = Jsoup.parseBodyFragment("<div><p>Hello, world!</p></div>").body();
        Element destination1 = new Element("body");
        int discarded1 = cleaner.copySafeNodes(source1, destination1);
        assertEquals(0, discarded1);
        assertEquals("div", destination1.child(0).tagName());
        assertEquals("p", destination1.child(0).child(0).tagName());
        assertEquals("Hello, world!", destination1.child(0).child(0).text());

        // Edge case: source element with an unsafe tag
        Element source2 = Jsoup.parseBodyFragment("<div><script>alert('XSS');</script></div>").body();
        Element destination2 = new Element("body");
        int discarded2 = cleaner.copySafeNodes(source2, destination2);
        assertEquals(1, discarded2);
        assertEquals("div", destination2.child(0).tagName());
        assertTrue(destination2.child(0).children().isEmpty());

        // Error scenario: null source element
        Element source3 = null;
        Element destination3 = new Element("body");
        try {
            cleaner.copySafeNodes(source3, destination3);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCreateSafeElement() {
        cleaner = new Cleaner(Whitelist.basic());

        // Typical use case: source element with safe attributes
        Element source1 = Jsoup.parseBodyFragment("<div data-id='123'>Hello, world!</div>").body().child(0);
        Cleaner.ElementMeta meta1 = cleaner.createSafeElement(source1);
        Element destination1 = meta1.el;
        assertEquals("div", destination1.tagName());
        assertEquals("123", destination1.attr("data-id"));
        assertEquals("Hello, world!", destination1.text());
        assertEquals(0, meta1.numAttribsDiscarded);

        // Edge case: source element with unsafe attributes
        Element source2 = Jsoup.parseBodyFragment("<div onclick='alert(1)'>Hello, world!</div>").body().child(0);
        Cleaner.ElementMeta meta2 = cleaner.createSafeElement(source2);
        Element destination2 = meta2.el;
        assertEquals("div", destination2.tagName());
        assertTrue(destination2.attributes().isEmpty());
        assertEquals("Hello, world!", destination2.text());
        assertEquals(1, meta2.numAttribsDiscarded);

        // Error scenario: null source element
        Element source3 = null;
        try {
            cleaner.createSafeElement(source3);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }
}