import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean_withValidDocument_returnCleanedDocument() {
        String dirtyHtml = "<html><body><div><p>Hello, World!</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document expectedCleanDocument = Jsoup.parse("<html><head></head><body><div><p>Hello, World!</p></div></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(expectedCleanDocument.outerHtml(), cleanDocument.outerHtml());
    }

    @Test
    public void testClean_withInvalidDocument_returnEmptyDocument() {
        String dirtyHtml = "<html><head></head><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document expectedCleanDocument = Jsoup.parse("<html><head></head><body></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(expectedCleanDocument.outerHtml(), cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid_withValidDocument_returnTrue() {
        String dirtyHtml = "<html><head></head><body><div><p>Hello, World!</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_withInvalidDocument_returnFalse() {
        String dirtyHtml = "<html><head><script>alert('XSS')</script></head><body><div><p>Hello, World!</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

}