import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CleanerTest {
    @Test
    public void testClean() {
        // Test case with empty document
        Document dirtyDocument1 = Document.createShell("");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        Document cleanDocument1 = cleaner1.clean(dirtyDocument1);
        assertEquals("<html><head></head><body></body></html>", cleanDocument1.toString());

        // Test case with nested elements
        Document dirtyDocument2 = Document.createShell("");
        Element body2 = new Element("body");
        Element div2 = new Element("div");
        Element span2 = new Element("span");
        div2.appendChild(span2);
        body2.appendChild(div2);
        dirtyDocument2.appendChild(body2);
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        Document cleanDocument2 = cleaner2.clean(dirtyDocument2);
        assertEquals("<html><head></head><body><div><span></span></div></body></html>", cleanDocument2.toString());
    }

    @Test
    public void testIsValid() {
        // Test case with empty document
        Document dirtyDocument1 = Document.createShell("");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        boolean valid1 = cleaner1.isValid(dirtyDocument1);
        assertTrue(valid1);

        // Test case with unsupported tag
        Document dirtyDocument2 = Document.createShell("");
        Element body2 = new Element("body");
        Element script2 = new Element("script");
        body2.appendChild(script2);
        dirtyDocument2.appendChild(body2);
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        boolean valid2 = cleaner2.isValid(dirtyDocument2);
        assertFalse(valid2);
    }
}