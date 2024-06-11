import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test typical use case
        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().append("<p>Hello, <b>World!</b></p>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("<html><head></head><body><p>Hello, <b>World!</b></p></body></html>", cleanedDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        // Test typical use case - valid document
        Document dirtyDocument1 = Document.createShell("http://example.com");
        dirtyDocument1.body().append("<p>Hello, <b>World!</b></p>");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        Assert.assertTrue(cleaner1.isValid(dirtyDocument1));

        // Test typical use case - invalid document
        Document dirtyDocument2 = Document.createShell("http://example.com");
        dirtyDocument2.body().append("<p><iframe src='http://malicious.com'></iframe></p>");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        Assert.assertFalse(cleaner2.isValid(dirtyDocument2));

        // Test edge case - empty document
        Document dirtyDocument3 = new Document("");
        Cleaner cleaner3 = new Cleaner(Whitelist.basic());
        Assert.assertTrue(cleaner3.isValid(dirtyDocument3));

        // Test error scenario - null document
        Document dirtyDocument4 = null;
        Cleaner cleaner4 = new Cleaner(Whitelist.basic());
        Assert.assertThrows(NullPointerException.class, () -> cleaner4.isValid(dirtyDocument4));
    }

    @Test
    public void testCopySafeNodes() {
        // Test typical use case
        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().append("<p>Hello, <b>World!</b></p>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document clean = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), clean.body());

        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<body><p>Hello, <b>World!</b></p></body>", clean.body().outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        // Test typical use case
        String sourceTag = "p";
        Element sourceEl = new Element(Tag.valueOf(sourceTag), "");
        sourceEl.attr("class", "content");
        sourceEl.attr("id", "paragraph");

        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;

        Assert.assertEquals("<p class=\"content\" id=\"paragraph\"></p>", dest.outerHtml());
        Assert.assertEquals(0, meta.numAttribsDiscarded);

        // Test edge case - no safe attributes
        String sourceTag2 = "div";
        Element sourceEl2 = new Element(Tag.valueOf(sourceTag2), "");
        sourceEl2.attr("onclick", "alert('hello')");
        sourceEl2.attr("onload", "loadImage()");

        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        Cleaner.ElementMeta meta2 = cleaner2.createSafeElement(sourceEl2);
        Element dest2 = meta2.el;

        Assert.assertEquals("<div></div>", dest2.outerHtml());
        Assert.assertEquals(2, meta2.numAttribsDiscarded);

        // Test error scenario - null element
        Element sourceEl3 = null;

        Cleaner cleaner3 = new Cleaner(Whitelist.basic());
        Assert.assertThrows(NullPointerException.class, () -> cleaner3.createSafeElement(sourceEl3));
    }

}