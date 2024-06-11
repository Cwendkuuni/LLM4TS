import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test case 1: Clean a document with no body
        Document doc1 = Jsoup.parse("<html><head></head></html>");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        Document cleanDoc1 = cleaner1.clean(doc1);
        Assert.assertEquals("<html><head></head><body></body></html>", cleanDoc1.outerHtml());

        // Test case 2: Clean a document with body
        Document doc2 = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        Document cleanDoc2 = cleaner2.clean(doc2);
        Assert.assertEquals("<html><head></head><body><div>Hello World!</div></body></html>", cleanDoc2.outerHtml());
    }

    @Test
    public void testIsValid() {
        // Test case 1: Valid document
        Document doc1 = Jsoup.parse("<html><head></head><body></body></html>");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        boolean isValid1 = cleaner1.isValid(doc1);
        Assert.assertTrue(isValid1);

        // Test case 2: Invalid document
        Document doc2 = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        boolean isValid2 = cleaner2.isValid(doc2);
        Assert.assertFalse(isValid2);
    }

    @Test
    public void testCopySafeNodes() {
        // Test case 1: Copy safe nodes from source to destination element
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        Element source1 = Jsoup.parseBodyFragment("<div>Hello World!</div>").body();
        Element dest1 = new Element("body");
        int discarded1 = cleaner1.copySafeNodes(source1, dest1);
        Assert.assertEquals(0, discarded1);
        Assert.assertEquals("<body><div>Hello World!</div></body>", dest1.outerHtml());

        // Test case 2: Copy safe nodes from source with unsafe tag to destination element
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        Element source2 = Jsoup.parseBodyFragment("<script>alert('XSS')</script>").body();
        Element dest2 = new Element("body");
        int discarded2 = cleaner2.copySafeNodes(source2, dest2);
        Assert.assertEquals(1, discarded2);
        Assert.assertEquals("<body></body>", dest2.outerHtml());

        // Test case 3: Copy safe nodes from source to destination element with enforced attributes
        Cleaner cleaner3 = new Cleaner(Whitelist.relaxed());
        Element source3 = Jsoup.parseBodyFragment("<a href='example.com' onclick='alert('XSS')'>Link</a>").body();
        Element dest3 = new Element("body");
        int discarded3 = cleaner3.copySafeNodes(source3, dest3);
        Assert.assertEquals(0, discarded3);
        Assert.assertEquals("<body><a href=\"example.com\">Link</a></body>", dest3.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        // Test case 1: Create safe element with no attributes discarded
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        Element source1 = Jsoup.parseBodyFragment("<div>Hello World!</div>").body().child(0);
        Cleaner.ElementMeta meta1 = cleaner1.createSafeElement(source1);
        Assert.assertEquals("<div>Hello World!</div>", meta1.el.outerHtml());
        Assert.assertEquals(0, meta1.numAttribsDiscarded);

        // Test case 2: Create safe element with attributes discarded
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        Element source2 = Jsoup.parseBodyFragment("<img src='image.jpg' alt=''>").body().child(0);
        Cleaner.ElementMeta meta2 = cleaner2.createSafeElement(source2);
        Assert.assertEquals("<img src=\"image.jpg\">", meta2.el.outerHtml());
        Assert.assertEquals(1, meta2.numAttribsDiscarded);

        // Test case 3: Create safe element with enforced attributes
        Cleaner cleaner3 = new Cleaner(Whitelist.relaxed());
        Element source3 = Jsoup.parseBodyFragment("<a href='example.com'>Link</a>").body().child(0);
        Cleaner.ElementMeta meta3 = cleaner3.createSafeElement(source3);
        Assert.assertEquals("<a href=\"example.com\">Link</a>", meta3.el.outerHtml());
        Assert.assertEquals(0, meta3.numAttribsDiscarded);
    }
}