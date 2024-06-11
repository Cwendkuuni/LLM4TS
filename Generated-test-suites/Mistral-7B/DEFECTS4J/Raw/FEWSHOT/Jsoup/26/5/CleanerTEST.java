package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

public class CleanerTest {
    @Test
    public void testCleanerInitialization() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Hello, world!</h1><p id='text'>This is an <script>unsafe</script> test.</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><h1 id='title'>Hello, world!</h1></body></html>", cleanDocument.body().html());
        assertFalse(cleanDocument.body().hasText());
        assertFalse(cleanDocument.body().hasAttr("id"));
        assertNull(cleanDocument.body().child(1));
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Hello, world!</h1><p id='text'>This is a <strong>safe</strong> test.</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));

        Document dirtyDocumentInvalid = Jsoup.parse("<html><body><h1 id='title'>Hello, world!</h1><p id='text'>This is an <script>unsafe</script> test.</p></body></html>");
        assertFalse(cleaner.isValid(dirtyDocumentInvalid));
    }

    @Test
    public void testCopySafeNodesHierarchy() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p><h1><a href='#'>Link</a></h1><p>Text</p></p></body></html>");
        Element dest = new Element(Tag.BODY, dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
        assertEquals("<html><body><p><h1><a href='#'>Link</a></h1></p></body></html>", dest.html());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodesAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p><div class='safe' id='id' title='title'><a href='link'><i>Text</i></a></div></p></body></html>");
        Element dest = new Element(Tag.BODY, dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
        Element div = dest.child(0);
        assertEquals("<div class='safe'>Text</div>", div.html());
        assertEquals("id", div.id());
        assertEquals("title", div.attr("title"));
        assertEquals("link", div.child(0).attr("href"));
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesUnsafeAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p><div class='safe' id='id' title='title' onclick='click()'><a href='link'><i>Text</i></a></div></p></body></html>");
        Element dest = new Element(Tag.BODY, dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
        Element div = dest.child(0);
        assertEquals("<div class='safe'>Text</div>", div.html());
        assertNull(div.attr("onclick"));
        assertEquals("id", div.id());
        assertEquals("title", div.attr("title"));
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodesNestedUnsafeElements() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p><div class='safe' id='id'><button onclick='click()'>Click me!</button></div></p></body></html>");
        Element dest = new Element(Tag.BODY, dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
        Element div = dest.child(0);
        assertEquals("<div class='safe'></div>", div.html());
        assertNull(div.child(0));
        assertEquals("id", div.id());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.DIV, null, null);
        sourceEl.classes().add("safe");
        sourceEl.attr("id", "id");
        sourceEl.attr("title", "title");

        Element dest = new Element(Tag.DIV, sourceEl.baseUri());
        int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);

        assertEquals("<div class='safe'>Text</div>", dest.html());
        assertEquals("id", dest.id());
        assertEquals("title", dest.attr("title"));
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElementAttributesNotAllowed() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.DIV, null, null);
        sourceEl.classes().add("safe");
        sourceEl.attr("id", "id");
        sourceEl.attr("title", "title");
        sourceEl.attr("onclick", "click()");

        Element dest = new Element(Tag.DIV, sourceEl.baseUri());
        int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);

        assertEquals("<div class='safe'></div>", dest.html());
        assertNull(dest.attr("onclick"));
        assertEquals("id", dest.id());
        assertEquals("title", dest.attr("title"));
        assertEquals(1, numDiscarded);
    }

    private static void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Object cannot be null");
        }
    }
}