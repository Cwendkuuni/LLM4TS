import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testClean() {
        String dirtyHtml = "<html><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDoc = Jsoup.parse(dirtyHtml);
        Document cleanDoc = cleaner.clean(dirtyDoc);
        
        assertEquals("<html>\n" +
                "<head></head>\n" +
                "<body>\n" +
                " <h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>", cleanDoc.toString());
    }

    @Test
    public void testIsValid_ValidDocument() {
        String validHtml = "<html><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document validDoc = Jsoup.parse(validHtml);
        boolean isValid = cleaner.isValid(validDoc);
        
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        String invalidHtml = "<html><body><script>alert('XSS');</script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document invalidDoc = Jsoup.parse(invalidHtml);
        boolean isValid = cleaner.isValid(invalidDoc);
        
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        String sourceHtml = "<html><body><div class=\"safe\"><p>This is safe content</p></div></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document sourceDoc = Jsoup.parse(sourceHtml);
        Document destDoc = Document.createShell("");
        cleaner.copySafeNodes(sourceDoc.body(), destDoc.body());
        
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div class=\"safe\">\n" +
                "   <p>This is safe content</p>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>", destDoc.toString());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        sourceEl.attr("href", "https://example.com");
        sourceEl.attr("target", "_blank");
        sourceEl.attr("class", "safe");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertEquals(org.jsoup.parser.Tag.valueOf("div"), elementMeta.el.tag());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}