
import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.safety.*;
import org.jsoup.nodes.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test case 1: clean a document with no tags or attributes
        Document dirtyDocument1 = Document.createShell("");
        Document cleanDocument1 = cleaner.clean(dirtyDocument1);
        assertEquals(0, cleanDocument1.getElementsByTag("body").size());

        // Test case 2: clean a document with safe tags and attributes
        Document dirtyDocument2 = Jsoup.parse("<html><head></head><body><p>HelloWorld</p></body></html>");
        Document cleanDocument2 = cleaner.clean(dirtyDocument2);
        assertEquals(1, cleanDocument2.getElementsByTag("p").size());

        // Test case 3: clean a document with unsafe tags and attributes
        Document dirtyDocument3 = Jsoup.parse("<html><head></head><body><script>alert('HelloWorld');</script></body></html>");
        Document cleanDocument3 = cleaner.clean(dirtyDocument3);
        assertEquals(0, cleanDocument3.getElementsByTag("script").size());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test case 1: validate a document with no tags or attributes
        Document dirtyDocument1 = Document.createShell("");
        boolean isValid1 = cleaner.isValid(dirtyDocument1);
        assertTrue(isValid1);

        // Test case 2: validate a document with safe tags and attributes
        Document dirtyDocument2 = Jsoup.parse("<html><head></head><body><p>HelloWorld</p></body></html>");
        boolean isValid2 = cleaner.isValid(dirtyDocument2);
        assertTrue(isValid2);

        // Test case 3: validate a document with unsafe tags and attributes
        Document dirtyDocument3 = Jsoup.parse("<html><head></head><body><script>alert('HelloWorld');</script></body></html>");
        boolean isValid3 = cleaner.isValid(dirtyDocument3);
        assertFalse(isValid3);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test case 1: copy safe nodes from the source to the destination
        Document dirtyDocument1 = Jsoup.parse("<html><head></head><body><p>HelloWorld</p></body></html>");
        Element source1 = dirtyDocument1.body();
        Document cleanDocument1 = Document.createShell("");
        Element destination1 = cleanDocument1.body();
        int numDiscarded1 = cleaner.copySafeNodes(source1, destination1);
        assertEquals(0, numDiscarded1);
        assertEquals(1, destination1.getElementsByTag("p").size());

        // Test case 2: copy safe nodes from an element with unsafe tags and attributes
        Document dirtyDocument2 = Jsoup.parse("<html><head></head><body><script>alert('HelloWorld');</script></body></html>");
        Element source2 = dirtyDocument2.body();
        Document cleanDocument2 = Document.createShell("");
        Element destination2 = cleanDocument2.body();
        int numDiscarded2 = cleaner.copySafeNodes(source2, destination2);
        assertEquals(1, numDiscarded2);
        assertEquals(0, destination2.getElementsByTag("script").size());

        // Test case 3: copy safe nodes from a text node
        Document dirtyDocument3 = Jsoup.parse("<html><head></head><body><p>HelloWorld</p></body></html>");
        Element source3 = dirtyDocument3.body().getElementsByTag("p").first().textNodes().first();
        Document cleanDocument3 = Document.createShell("");
        Element destination3 = cleanDocument3.body();
        int numDiscarded3 = cleaner.copySafeNodes(source3, destination3);
        assertEquals(0, numDiscarded3);
        assertEquals(1, destination3.textNodes().size());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test case 1: create a safe element with no attributes
        Element source1 = new Element(Tag.valueOf("p"), "");
        ElementMeta meta1 = cleaner.createSafeElement(source1);
        Element dest1 = meta1.el;
        assertEquals("p", dest1.tagName());
        assertEquals(0, dest1.attributes().size());

        // Test case 2: create a safe element with safe attributes
        Element source2 = new Element(Tag.valueOf("p"), "");
        source2.attr("class", "test");
        ElementMeta meta2 = cleaner.createSafeElement(source2);
        Element dest2 = meta2.el;
        assertEquals("p", dest2.tagName());
        assertEquals(1, dest2.attributes().size());
        assertTrue(dest2.hasAttr("class"));

        // Test case 3: create a safe element with unsafe attributes
        Element source3 = new Element(Tag.valueOf("p"), "");
        source3.attr("onclick", "alert('HelloWorld');");
        ElementMeta meta3 = cleaner.createSafeElement(source3);
        Element dest3 = meta3.el;
        assertEquals("p", dest3.tagName());
        assertEquals(0, dest3.attributes().size());
        assertFalse(dest3.hasAttr("onclick"));
    }

}

