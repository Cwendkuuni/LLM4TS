import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild(new Element("div"));
        dirtyDocument.body().getElementsByTag("div").get(0).append("Hello, World!");

        Document cleanedDocument = cleaner.clean(dirtyDocument);

        assertEquals("", cleanedDocument.baseUri());
        assertEquals(1, cleanedDocument.body().children().size());
        assertEquals("div", cleanedDocument.body().children().get(0).tagName());
        assertEquals("Hello, World!", cleanedDocument.body().getElementsByTag("div").get(0).text());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild(new Element("p").attr("id", "test"));

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testEdgeCases() {
        // Test when dirtyDocument is null
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        assertThrows(IllegalArgumentException.class, () -> cleaner.clean(null));
        assertThrows(IllegalArgumentException.class, () -> cleaner.isValid(null));
    }

    @Test
    public void testExceptionHandling() {
        // Test when safe tag is not found
        Cleaner cleaner = new Cleaner(Whitelist.none());
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild(new Element("div"));
        dirtyDocument.body().getElementsByTag("div").get(0).text("Hello, World!");

        Document cleanedDocument = cleaner.clean(dirtyDocument);

        assertEquals("", cleanedDocument.baseUri());
        assertEquals(0, cleanedDocument.body().children().size());
    }
}