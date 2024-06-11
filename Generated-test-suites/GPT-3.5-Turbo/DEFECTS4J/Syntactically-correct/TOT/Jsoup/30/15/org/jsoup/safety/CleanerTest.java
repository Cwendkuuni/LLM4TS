import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<html><head></head><body><p>Hello World</p></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertTrue(cleanDocument instanceof Document);
        assertEquals(0, cleanDocument.select("head").size());
        assertEquals(1, cleanDocument.select("body").size());
        assertEquals(1, cleanDocument.select("p").size());
        assertEquals("Hello World", cleanDocument.select("p").get(0).text());
    }

    @Test
    public void testClean_EmptyDocument() {
        Document emptyDocument = Jsoup.parse("");
        Document cleanDocument = cleaner.clean(emptyDocument);
        assertNotNull(cleanDocument);
        assertTrue(cleanDocument instanceof Document);
        assertEquals(0, cleanDocument.select("head").size());
        assertEquals(1, cleanDocument.select("body").size());
        assertEquals(0, cleanDocument.select("p").size());
    }

    @Test
    public void testClean_NullDocument() {
        Document cleanDocument = cleaner.clean(null);
        assertNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Document invalidDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS');</script></body></html>");
        boolean isValid = cleaner.isValid(invalidDocument);
        assertFalse(isValid);
    }

    @Test
    public void testIsValid_EmptyDocument() {
        Document emptyDocument = Jsoup.parse("");
        boolean isValid = cleaner.isValid(emptyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_NullDocument() {
        boolean isValid = cleaner.isValid(null);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);
        assertEquals(1, cleanDocument.select("p").size());
        assertEquals("Hello World", cleanDocument.select("p").get(0).text());
    }

    @Test
    public void testCopySafeNodes_InvalidTag() {
        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(Jsoup.parse("<html><head></head><body><script>alert('XSS');</script></body></html>").body(), cleanDocument.body());
        assertEquals(1, numDiscarded);
        assertEquals(0, cleanDocument.select("script").size());
    }

    @Test
    public void testCreateSafeElement() {
        Document cleanDocument = Document.createShell("");
        Element sourceElement = Jsoup.parse("<div id=\"safe\" class=\"safe\"><p>Hello World</p></div>").select("div").get(0);
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destinationElement = elementMeta.el;
        assertEquals("div", destinationElement.tagName());
        assertEquals("safe", destinationElement.id());
        assertEquals("safe", destinationElement.className());
        assertEquals(1, destinationElement.select("p").size());
        assertEquals("Hello World", destinationElement.select("p").get(0).text());
    }

    @Test
    public void testCreateSafeElement_UnsafeAttribute() {
        Document cleanDocument = Document.createShell("");
        Element sourceElement = Jsoup.parse("<div id=\"safe\" class=\"safe\" onclick=\"alert('XSS')\"><p>Hello World</p></div>").select("div").get(0);
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destinationElement = elementMeta.el;
        assertEquals("div", destinationElement.tagName());
        assertEquals("safe", destinationElement.id());
        assertEquals("safe", destinationElement.className());
        assertNull(destinationElement.attr("onclick"));
        assertEquals(1, destinationElement.select("p").size());
        assertEquals("Hello World", destinationElement.select("p").get(0).text());
    }

    @Test
    public void testCreateSafeElement_EnforcedAttributes() {
        Document cleanDocument = Document.createShell("");
        Element sourceElement = Jsoup.parse("<a href=\"https://example.com\">Link</a>").select("a").get(0);
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destinationElement = elementMeta.el;
        assertEquals("a", destinationElement.tagName());
        assertEquals("https://example.com", destinationElement.attr("href"));
        assertEquals(1, destinationElement.select("a").size());
        assertEquals("Link", destinationElement.select("a").get(0).text());
    }
}