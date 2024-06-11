import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><div><h1>Title</h1><p>Paragraph</p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(1, cleanDocument.body().children().size());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals(2, cleanDocument.body().child(0).children().size());
        assertEquals("h1", cleanDocument.body().child(0).child(0).tagName());
        assertEquals("p", cleanDocument.body().child(0).child(1).tagName());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><body><h1>Title</h1><p>Paragraph</p><script>alert('XSS Attack')</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }
}