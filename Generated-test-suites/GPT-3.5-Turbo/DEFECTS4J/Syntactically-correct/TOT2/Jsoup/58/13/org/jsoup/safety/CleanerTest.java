import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        Element body = new Element("body", "");
        body.append("<div>Hello, world!</div>");
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        // Verify that only allowed elements are present
        assertEquals(1, cleanDocument.body().children().size());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals("Hello, world!", cleanDocument.body().child(0).text());
    }

    @Test
    public void testIsValid() {
        Document validDocument = new Document("");
        Element validBody = new Element("body", "");
        validBody.append("<p>This is a valid paragraph.</p>");
        validDocument.appendChild(validBody);

        Document invalidDocument = new Document("");
        Element invalidBody = new Element("body", "");
        invalidBody.append("<script>alert('XSS attack!');</script>");
        invalidDocument.appendChild(invalidBody);

        assertTrue(cleaner.isValid(validDocument));
        assertTrue(!cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCleanWithEmptyDocument() {
        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        // Verify that the output document is empty
        assertEquals(0, cleanDocument.body().children().size());
    }

    // Add more test cases for edge cases and exception handling scenarios as needed

}