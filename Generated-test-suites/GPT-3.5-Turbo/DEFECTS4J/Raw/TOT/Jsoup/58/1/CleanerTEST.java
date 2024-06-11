import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @After
    public void tearDown() {
        whitelist = null;
        cleaner = null;
    }

    @Test
    public void testClean_returnsCleanedDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document dirtyDocument = new Document(html);
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html>\n" +
                     " <head></head>\n" +
                     " <body>\n" +
                     "  <h1>Hello, World!</h1>\n" +
                     " </body>\n" +
                     "</html>", cleanedDocument.toString());
    }

    @Test
    public void testClean_doesNotModifyDirtyDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document dirtyDocument = new Document(html);
        cleaner.clean(dirtyDocument);

        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", dirtyDocument.toString());
    }

    @Test
    public void testIsValid_returnsTrueForValidDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document validDocument = new Document(html);

        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testIsValid_returnsFalseForInvalidDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1><script>alert('XSS');</script></body></html>";
        Document invalidDocument = new Document(html);

        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes_copiesSafeTagsAndAttributes() {
        String html = "<html><head><title>Test</title></head><body><h1 class=\"header\">Hello, World!</h1></body></html>";
        Document dirtyDocument = new Document(html);
        Element sourceElement = dirtyDocument.body().child(0);
        Element destElement = new Element("div");

        cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals("<div>\n" +
                     " <h1 class=\"header\">Hello, World!</h1>\n" +
                     "</div>", destElement.toString());
    }

    @Test
    public void testCopySafeNodes_discardsUnsafeTagsAndAttributes() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = new Document(html);
        Element sourceElement = dirtyDocument.body().child(1);
        Element destElement = new Element("div");

        cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals("<div></div>", destElement.toString());
    }

    @Test
    public void testCopySafeNodes_copiesTextNodes() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document dirtyDocument = new Document(html);
        Element sourceElement = dirtyDocument.body().child(0);
        Element destElement = new Element("div");

        cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals("<div>\n" +
                     " <h1>Hello, World!</h1>\n" +
                     "</div>", destElement.toString());
    }

    @Test
    public void testCopySafeNodes_copiesDataNodesForSafeTags() {
        String html = "<html><head><title>Test</title></head><body><script>var x = 10;</script></body></html>";
        Document dirtyDocument = new Document(html);
        Element sourceElement = dirtyDocument.body().child(0);
        Element destElement = new Element("div");

        cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals("<div>\n" +
                     " <script>var x = 10;</script>\n" +
                     "</div>", destElement.toString());
    }

    @Test
    public void testCopySafeNodes_discardsDataNodesForUnsafeTags() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1><script>var x = 10;</script></body></html>";
        Document dirtyDocument = new Document(html);
        Element sourceElement = dirtyDocument.body().child(1);
        Element destElement = new Element("div");

        cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals("<div></div>", destElement.toString());
    }

    @Test
    public void testCreateSafeElement_createsSafeElementWithSafeAttributes() {
        String html = "<html><head><title>Test</title></head><body><h1 class=\"header\">Hello, World!</h1></body></html>";
        Document dirtyDocument = new Document(html);
        Element sourceElement = dirtyDocument.body().child(0);

        Cleaner.ElementMeta safeElementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("h1", safeElementMeta.el.tagName());
        assertEquals(1, safeElementMeta.el.attributes().size());
        assertTrue(safeElementMeta.el.attributes().hasKey("class"));
        assertEquals("header", safeElementMeta.el.attributes().get("class"));
        assertEquals(0, safeElementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_discardsUnsafeAttributes() {
        String html = "<html><head><title>Test</title></head><body><h1 onClick=\"alert('XSS')\">Hello, World!</h1></body></html>";
        Document dirtyDocument = new Document(html);
        Element sourceElement = dirtyDocument.body().child(0);

        Cleaner.ElementMeta safeElementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("h1", safeElementMeta.el.tagName());
        assertEquals(0, safeElementMeta.el.attributes().size());
        assertEquals(1, safeElementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_addsEnforcedAttributes() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document dirtyDocument = new Document(html);
        Element sourceElement = dirtyDocument.body().child(0);

        Cleaner.ElementMeta safeElementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("h1", safeElementMeta.el.tagName());
        assertEquals(1, safeElementMeta.el.attributes().size());
        assertTrue(safeElementMeta.el.attributes().hasKey("style"));
        assertEquals("font-size:20px", safeElementMeta.el.attributes().get("style"));
        assertEquals(0, safeElementMeta.numAttribsDiscarded);
    }
}