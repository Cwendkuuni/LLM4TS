package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }
    
    @Test
    public void testClean() {
        // Typical use case
        String dirtyHtml = "<div><p>Dirty HTML</p></div>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);
        Document cleanDoc = cleaner.clean(dirtyDoc);
        
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div>\n" +
                "   <p>\n" +
                "    Dirty HTML\n" +
                "   </p>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>", cleanDoc.toString());

        // Edge case: empty document
        Document emptyDoc = cleaner.clean(new Document(""));
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                " </body>\n" +
                "</html>", emptyDoc.toString());
    }
    
    @Test
    public void testIsValid() {
        // Typical use case
        String validHtml = "<div><p>Valid HTML</p></div>";
        Document validDoc = Jsoup.parse(validHtml);
        boolean isValid = cleaner.isValid(validDoc);
        
        Assert.assertTrue(isValid);

        // Typical use case with invalid document
        String invalidHtml = "<div><script>alert('XSS');</script></div>";
        Document invalidDoc = Jsoup.parse(invalidHtml);
        boolean isInvalid = cleaner.isValid(invalidDoc);
        
        Assert.assertFalse(isInvalid);
    }
    
    @Test
    public void testCopySafeNodes() {
        // Typical use case
        String dirtyHtml = "<div><p>Dirty HTML</p></div>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);
        Document cleanDoc = Document.createShell(dirtyDoc.baseUri());
        
        int numDiscarded = cleaner.copySafeNodes(dirtyDoc.body(), cleanDoc.body());
        
        Assert.assertEquals(0, numDiscarded);

        // Edge case: Element with unsafe tag and unsafe attribute
        String unsafeHtml = "<div onclick=\"alert('XSS');\"><p>Unsafe HTML</p></div>";
        Document unsafeDoc = Jsoup.parse(unsafeHtml);
        Document cleanUnsafeDoc = Document.createShell(unsafeDoc.baseUri());
        
        int numUnsafeDiscarded = cleaner.copySafeNodes(unsafeDoc.body(), cleanUnsafeDoc.body());
        
        Assert.assertEquals(1, numUnsafeDiscarded);

        // Edge case: Text Node
        String textHtml = "This is a text node";
        Document textDoc = Jsoup.parse(textHtml);
        Document cleanTextDoc = Document.createShell(textDoc.baseUri());
        
        int numTextDiscarded = cleaner.copySafeNodes(textDoc.body(), cleanTextDoc.body());
        
        Assert.assertEquals(0, numTextDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        // Typical use case
        ElementMeta meta = cleaner.createSafeElement(new Element(Tag.valueOf("div"), ""));
        
        Assert.assertEquals("div", meta.el.tagName());
        Assert.assertEquals(0, meta.numAttribsDiscarded);

        // Edge case: Element with unsafe attributes
        Element unsafeElement = new Element(Tag.valueOf("div"), "");
        unsafeElement.attr("onclick", "alert('XSS');");
        
        ElementMeta unsafeMeta = cleaner.createSafeElement(unsafeElement);
        
        Assert.assertEquals("div", unsafeMeta.el.tagName());
        Assert.assertEquals(1, unsafeMeta.numAttribsDiscarded);
    }
}