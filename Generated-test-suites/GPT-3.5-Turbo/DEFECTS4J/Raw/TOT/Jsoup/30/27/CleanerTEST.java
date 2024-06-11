import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    // Test cases for the constructor

    @Test
    public void testConstructor_ValidWhitelist() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        assertNotNull(cleaner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullWhitelist() {
        new Cleaner(null);
    }

    // Test cases for the clean() method

    @Test
    public void testClean_ValidDocument() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<b>Hello World!</b>");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><b>Hello World!</b></body></html>", cleanDocument.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClean_NullDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        cleaner.clean(null);
    }

    // Test cases for the isValid() method

    @Test
    public void testIsValid_ValidDocument() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<b>Hello World!</b>");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS')</script>");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValid_NullDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        cleaner.isValid(null);
    }

    // Test cases for the copySafeNodes() private method
    // Edge Case: Empty Element

    @Test
    public void testCopySafeNodes_EmptyElement() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
        assertTrue(destination.childNodes().isEmpty());
    }

    // Test cases for the createSafeElement() private method

    @Test
    public void testCreateSafeElement_ValidElement() {
        Element sourceEl = new Element("a");
        sourceEl.attr("href", "https://www.example.com");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNotNull(meta);
        assertNotNull(meta.el);
        assertEquals("a", meta.el.tagName());
        assertEquals("https://www.example.com", meta.el.absUrl("href"));
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_InvalidElement() {
        Element sourceEl = new Element("script");
        sourceEl.attr("src", "https://www.example.com/script.js");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNotNull(meta);
        assertNotNull(meta.el);
        assertEquals("script", meta.el.tagName());
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_EnforcedAttributes() {
        Element sourceEl = new Element("p");
        sourceEl.attr("style", "color: red;");
        Whitelist whitelist = new Whitelist();
        whitelist.addEnforcedAttribute("p", "style", "font-size: 12px;");
        Cleaner cleaner = new Cleaner(whitelist);

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNotNull(meta);
        assertNotNull(meta.el);
        assertEquals("p", meta.el.tagName());
        assertEquals("color: red; font-size: 12px;", meta.el.attr("style"));
        assertEquals(0, meta.numAttribsDiscarded);
    }

    // Test cases for the ElementMeta class

    @Test
    public void testElementMeta_ValidMeta() {
        Element dest = new Element("a");
        int numAttribsDiscarded = 2;
        Cleaner.ElementMeta meta = new Cleaner.ElementMeta(dest, numAttribsDiscarded);

        assertNotNull(meta);
        assertNotNull(meta.el);
        assertEquals("a", meta.el.tagName());
        assertEquals(numAttribsDiscarded, meta.numAttribsDiscarded);
    }

    // Additional test cases can be added for other methods and edge/exceptional scenarios

}