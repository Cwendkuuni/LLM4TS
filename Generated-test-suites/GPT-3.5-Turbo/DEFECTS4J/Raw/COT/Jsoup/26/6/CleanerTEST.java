import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test case 1: Clean an empty document
        Document dirtyDocument1 = Document.createShell("");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        Document result1 = cleaner1.clean(dirtyDocument1);
        assertTrue(result1.body().children().isEmpty());

        // Test case 2: Clean a document with allowed tags
        Document dirtyDocument2 = Jsoup.parse("<div><h1>Title</h1><p>Content</p></div>");
        Cleaner cleaner2 = new Cleaner(Whitelist.basicWithImages());
        Document result2 = cleaner2.clean(dirtyDocument2);
        assertEquals("<div><p>Content</p></div>", result2.body().html());

        // Test case 3: Clean a document with disallowed tags
        Document dirtyDocument3 = Jsoup.parse("<h1>Title</h1><p>Content</p>");
        Cleaner cleaner3 = new Cleaner(Whitelist.basic());
        Document result3 = cleaner3.clean(dirtyDocument3);
        assertTrue(result3.body().children().isEmpty());
    }

    @Test
    public void testIsValid() {
        // Test case 1: Valid document
        Document dirtyDocument1 = Jsoup.parse("<div><h1>Title</h1><p>Content</p></div>");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        boolean result1 = cleaner1.isValid(dirtyDocument1);
        assertTrue(result1);

        // Test case 2: Invalid document
        Document dirtyDocument2 = Jsoup.parse("<h1>Title</h1><p>Content</p>");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        boolean result2 = cleaner2.isValid(dirtyDocument2);
        assertFalse(result2);
    }

    @Test
    public void testCopySafeNodes() {
        // Test case 1: Copy safe nodes from source to destination
        Element source = new Element("div");
        Element dest = new Element("div");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        int result = cleaner.copySafeNodes(source, dest);
        assertEquals(0, result);
        assertTrue(dest.childNodes().isEmpty());

        // Test case 2: Copy safe nodes with attributes from source to destination
        Element source2 = new Element("div");
        source2.attr("id", "test");
        Element dest2 = new Element("div");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        int result2 = cleaner2.copySafeNodes(source2, dest2);
        assertEquals(0, result2);
        assertFalse(dest2.attributes().isEmpty());
    }
    
    @Test
    public void testCreateSafeElement() {
        // Test case 1: Create safe element with allowed attribute
        Element sourceEl1 = new Element("div");
        sourceEl1.attr("id", "test");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        Cleaner.ElementMeta result1 = cleaner1.createSafeElement(sourceEl1);
        assertNotNull(result1);
        assertFalse(result1.el.attributes().isEmpty());
        assertEquals(0, result1.numAttribsDiscarded);

        // Test case 2: Create safe element with disallowed attribute
        Element sourceEl2 = new Element("div");
        sourceEl2.attr("onclick", "alert('Hello')");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        Cleaner.ElementMeta result2 = cleaner2.createSafeElement(sourceEl2);
        assertNotNull(result2);
        assertFalse(result2.el.attributes().isEmpty());
        assertEquals(1, result2.numAttribsDiscarded);
    }

}