import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Test case 1: Empty document
        Document dirtyDocument1 = new Document("");
        Document cleanDocument1 = cleaner.clean(dirtyDocument1);
        Assert.assertEquals("<html>\n <head></head>\n <body></body>\n</html>", cleanDocument1.toString());

        // Test case 2: Document with safe tags
        String html2 = "<html>\n <head></head>\n <body><p>This is a safe paragraph.</p></body>\n</html>";
        Document dirtyDocument2 = Jsoup.parse(html2);
        Document cleanDocument2 = cleaner.clean(dirtyDocument2);
        Assert.assertEquals(html2, cleanDocument2.toString());

        // Test case 3: Document with unsafe tags
        String html3 = "<html>\n <head></head>\n <body><script>alert('XSS!');</script></body>\n</html>";
        Document dirtyDocument3 = Jsoup.parse(html3);
        Document cleanDocument3 = cleaner.clean(dirtyDocument3);
        Assert.assertEquals("<html>\n <head></head>\n <body></body>\n</html>", cleanDocument3.toString());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Test case 1: Empty document
        Document dirtyDocument1 = new Document("");
        boolean isValid1 = cleaner.isValid(dirtyDocument1);
        Assert.assertTrue(isValid1);

        // Test case 2: Document with safe tags
        String html2 = "<html>\n <head></head>\n <body><p>This is a safe paragraph.</p></body>\n</html>";
        Document dirtyDocument2 = Jsoup.parse(html2);
        boolean isValid2 = cleaner.isValid(dirtyDocument2);
        Assert.assertTrue(isValid2);

        // Test case 3: Document with unsafe tags
        String html3 = "<html>\n <head></head>\n <body><script>alert('XSS!');</script></body>\n</html>";
        Document dirtyDocument3 = Jsoup.parse(html3);
        boolean isValid3 = cleaner.isValid(dirtyDocument3);
        Assert.assertFalse(isValid3);
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Test case 1: Empty element
        Element root1 = new Element(Tag.valueOf("div"));
        Document destination1 = Document.createShell("");
        int numDiscarded1 = cleaner.copySafeNodes(root1, destination1.body());
        Assert.assertEquals(0, numDiscarded1);
        Assert.assertEquals("<div></div>", destination1.body().toString());

        // Test case 2: Element with safe tags
        Element root2 = new Element(Tag.valueOf("div"));
        root2.appendChild(new Element(Tag.valueOf("p")).text("This is a safe paragraph."));
        Document destination2 = Document.createShell("");
        int numDiscarded2 = cleaner.copySafeNodes(root2, destination2.body());
        Assert.assertEquals(0, numDiscarded2);
        Assert.assertEquals("<div>\n <p>This is a safe paragraph.</p>\n</div>", destination2.body().toString());

        // Test case 3: Element with unsafe tags
        Element root3 = new Element(Tag.valueOf("div"));
        root3.appendChild(new Element(Tag.valueOf("script")).text("alert('XSS!');"));
        Document destination3 = Document.createShell("");
        int numDiscarded3 = cleaner.copySafeNodes(root3, destination3.body());
        Assert.assertEquals(1, numDiscarded3);
        Assert.assertEquals("<div></div>", destination3.body().toString());
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Test case 1: Safe element
        Element sourceEl1 = new Element(Tag.valueOf("div"));
        sourceEl1.attributes().put("id", "division");
        ElementMeta meta1 = cleaner.createSafeElement(sourceEl1);
        Assert.assertEquals("div", meta1.el.tagName());
        Assert.assertEquals("", meta1.el.baseUri());
        Assert.assertEquals(1, meta1.numAttribsDiscarded);

        // Test case 2: Unsafe element
        Element sourceEl2 = new Element(Tag.valueOf("script"));
        sourceEl2.attributes().put("src", "evil.js");
        ElementMeta meta2 = cleaner.createSafeElement(sourceEl2);
        Assert.assertEquals("script", meta2.el.tagName());
        Assert.assertEquals("", meta2.el.baseUri());
        Assert.assertEquals(1, meta2.numAttribsDiscarded);
    }
}