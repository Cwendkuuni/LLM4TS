import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean_typical() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Typical input
        Document dirtyDocument = new Document("<html><body>" +
                "<div id=\"content\"><h1>Title</h1><p>Paragraph</p></div>" +
                "</body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();

        assertEquals(1, body.childrenSize());
        assertEquals("div", body.child(0).tagName());
        assertEquals("content", body.child(0).id());
        assertEquals("h1", body.child(0).child(0).tagName());
        assertEquals("Title", body.child(0).child(0).text());
        assertEquals("p", body.child(0).child(1).tagName());
        assertEquals("Paragraph", body.child(0).child(1).text());
    }

    @Test
    public void testClean_edgeCase() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Edge case with no body element
        Document dirtyDocument = new Document("<html><head><title>Title</title></head></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNull(cleanDocument.body());
    }

    @Test
    public void testClean_error() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Error case with null document
        Document dirtyDocument = null;

        assertThrows(NullPointerException.class, () -> {
            cleaner.clean(dirtyDocument);
        });
    }

    @Test
    public void testIsValid_typical() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Typical input
        Document dirtyDocument = new Document("<html><body>" +
                "<div id=\"content\"><h1>Title</h1><p>Paragraph</p></div>" +
                "</body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_edgeCase() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Edge case with no body element
        Document dirtyDocument = new Document("<html><head><title>Title</title></head></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testIsValid_error() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Error case with null document
        Document dirtyDocument = null;

        assertThrows(NullPointerException.class, () -> {
            cleaner.isValid(dirtyDocument);
        });
    }
}