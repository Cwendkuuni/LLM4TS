import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import java.util.List;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("<html><body><p>Dirty document</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals("Dirty document", cleanDocument.body().text());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("<html><body><p>Dirty document</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));

        Document invalidDocument = new Document("<html><body><script>alert('XSS!');</script></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element root = new Element(Tag.valueOf("div"), "");
        
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.text("Safe element");
        sourceEl.attr("class", "safe");

        Element sourceEl2 = new Element(Tag.valueOf("script"), "");
        sourceEl2.attr("src", "https://evil.com/xss.js");

        root.appendChild(sourceEl);
        root.appendChild(sourceEl2);

        Element destination = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertNotNull(destination);
        assertEquals(1, destination.children().size());
        assertEquals("Safe element", destination.text());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.text("Safe element");
        sourceEl.attr("class", "safe");

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNotNull(meta);
        assertNotNull(meta.el);
        assertEquals("p", meta.el.tagName());
        assertEquals("Safe element", meta.el.text());
        assertEquals(0, meta.numAttribsDiscarded);
    }

}