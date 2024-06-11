import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testConstructor() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertNotNull(cleaner);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Dirty document</p></div></body></html>");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <div>\n   <p>Dirty document</p>\n  </div>\n </body>\n</html>", cleanDocument.html());
    }

    @Test
    public void testCleanWithNull() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertThrows(NullPointerException.class, () -> {
            cleaner.clean(null);
        });
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Dirty document</p></div></body></html>");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidWithNull() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertThrows(NullPointerException.class, () -> {
            cleaner.isValid(null);
        });
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        Assert.assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Assert.assertNotNull(meta);
        Assert.assertEquals("p", meta.el.tagName());
        Assert.assertEquals(0, meta.numAttribsDiscarded);
    }

}