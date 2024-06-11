package org.jsoup.safety;

import static org.junit.Assert.*;
import static org.jsoup.helper.W3C.*;

import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Document dirtyDocument = new Document(parse("<html><body><div>This is a <script>malicious</script> test.</div></body></html>"));;
        Cleaner cleaner = new Cleaner(whitelist);
        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><body></body></html>", clean.toString());
    }

    @Test
    public void testCleanWithEmptyDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("http://example.com");
        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><body></body></html>", clean.toString());
    }

    @Test
    public void testCleanWithAllowedTags() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Document dirtyDocument = new Document(parse("<html><body><p id='test'><a href='http://example.com'>Link</a></p></body></html>"));
        Cleaner cleaner = new Cleaner(whitelist);
        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p id='test'><a href='http://example.com'></a></p></body></html>", clean.toString());
    }

    @Test
    public void testCleanWithAllowedAttributes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml().addAttribute("*,*,class,id");
        Document dirtyDocument = new Document(parse("<html><body><p id='test' class='test'><a href='http://example.com' id='link'>Link</a></p></body></html>"));
        Cleaner cleaner = new Cleaner(whitelist);
        Document clean = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p id='test' class='test'><a href='http://example.com' id='link'></a></p></body></html>", clean.toString());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Document dirtyDocument = new Document(parse("<html><body><p>This is a clean document.</p></body></html>"));
        Cleaner cleaner = new Cleaner(whitelist);

        assertTrue(cleaner.isValid(dirtyDocument));

        Document invalidDocument = new Document(parse("<html><body><p>This is an <script>invalid</script> document.</p></body></html>"));
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testIsValidWithAllowedTags() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document(parse("<html><body><p><a href='http://example.com'>Link</a></p></body></html>"));
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithAllowedAttributes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml().addAttribute("*,*,class,id");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document(parse("<html><body><p id='test' class='test'><a href='http://example.com' id='link'>Link</a></p></body></html>"));
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanWithNullDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        cleaner.clean(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Element root = element("html", new Attributes(),
                element("body", new Attributes(),
                        element("p", new Attributes(),
                                element("a", new Attributes().put("href", "http://example.com"), text("Link")))));

        Element destination = element("html", new Attributes(), element("body", new Attributes()));
        int numDiscarded = Cleaner.copySafeNodes(root, destination);

        assertEquals(1, numDiscarded);
        assertEquals("<html><body><p><a href='http://example.com'></a></p></body></html>", destination.toString());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml().addAttribute("*,*,class,id");
        Element sourceEl = element("p", new Attributes().put("id", "test").put("class", "test"), text("Test"));
        Element dest = Cleaner.createSafeElement(sourceEl);

        assertEquals("p", dest.tagName());
        assertEquals("test", dest.className());
        assertEquals("test", dest.id());
        assertEquals("Test", dest.text());
    }
}