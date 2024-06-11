import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean_withValidDocument() {
        String html = "<html><body><p>This is a valid document.</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("This is a valid document.", cleanDocument.body().text());
    }

    @Test
    public void testClean_withNullDocument() {
        Document cleanDocument = cleaner.clean(null);
        
        assertNull(cleanDocument);
    }

    @Test
    public void testIsValid_withValidDocument() {
        String html = "<html><body><p>This is a valid document.</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_withInvalidDocument() {
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }

    @Test
    public void testIsValid_withNullDocument() {
        boolean isValid = cleaner.isValid(null);
        
        assertFalse(isValid);
    }

    @Test
    public void testClean_withNestedTags() {
        String html = "<html><body><p><span>This is a nested tag.</span></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("This is a nested tag.", cleanDocument.body().text());
    }

    @Test
    public void testCopySafeNodes_withNullSource() {
        Document cleanDocument = Document.createShell("");
        Element destElement = cleanDocument.body();
        
        int numDiscarded = cleaner.copySafeNodes(null, destElement);
        
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_withNullDestination() {
        String html = "<html><body><p>This is a paragraph.</p></body></html>";
        Document source = Jsoup.parse(html);
        
        int numDiscarded = cleaner.copySafeNodes(source.body(), null);
        
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_withSafeTagsAndAttributes() {
        String html = "<html><body><p class='paragraph'>This is a paragraph with a class attribute.</p></body></html>";
        Document source = Jsoup.parse(html);
        Document cleanDocument = Document.createShell("");
        Element destElement = cleanDocument.body();
        
        int numDiscarded = cleaner.copySafeNodes(source.body(), destElement);
        
        assertEquals(0, numDiscarded);
        assertEquals(1, destElement.children().size());
    }

    @Test
    public void testCopySafeNodes_withUnsafeTags() {
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document source = Jsoup.parse(html);
        Document cleanDocument = Document.createShell("");
        Element destElement = cleanDocument.body();
        
        int numDiscarded = cleaner.copySafeNodes(source.body(), destElement);
        
        assertEquals(1, numDiscarded);
        assertEquals(0, destElement.children().size());
    }

    @Test
    public void testCopySafeNodes_withTextNodes() {
        String html = "<html><body><p>This is a paragraph.</p></body></html>";
        Document source = Jsoup.parse(html);
        Document cleanDocument = Document.createShell("");
        Element destElement = cleanDocument.body();
        
        int numDiscarded = cleaner.copySafeNodes(source.body(), destElement);
        
        assertEquals(0, numDiscarded);
        assertEquals("This is a paragraph.", destElement.text());
    }

    @Test
    public void testCreateSafeElement_withSafeAttributes() {
        String html = "<html><body><p class='paragraph'>This is a paragraph with a class attribute.</p></body></html>";
        Document source = Jsoup.parse(html);
        Element sourceElement = source.body().child(0);
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        
        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_withUnsafeAttributes() {
        String html = "<html><body><p onclick='alert(\"XSS\")'>This is a paragraph.</p></body></html>";
        Document source = Jsoup.parse(html);
        Element sourceElement = source.body().child(0);
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        
        assertNotNull(elementMeta.el);
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_withEnforcedAttributes() {
        String html = "<html><body><p>This is a paragraph.</p></body></html>";
        Document source = Jsoup.parse(html);
        Element sourceElement = source.body().child(0);
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        
        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertTrue(elementMeta.el.hasAttr("class"));
    }
}