import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setup() {
        whitelist = Whitelist.basicWithImages();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><body><div><script>alert('XSS');</script></div></body></html>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);

        Document cleanDoc = cleaner.clean(dirtyDoc);

        String expectedHtml = "<html><head></head><body><div></div></body></html>";
        Document expectedDoc = Jsoup.parse(expectedHtml);

        assertEquals(expectedDoc.html(), cleanDoc.html());
    }

    @Test
    public void testIsValid_validDocument() {
        String validHtml = "<html><body><div></div></body></html>";
        Document validDoc = Jsoup.parse(validHtml);

        boolean isValid = cleaner.isValid(validDoc);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocument() {
        String invalidHtml = "<html><body><img onerror='alert('XSS')' src='invalid.jpg'></body></html>";
        Document invalidDoc = Jsoup.parse(invalidHtml);

        boolean isValid = cleaner.isValid(invalidDoc);

        assertEquals(false, isValid);
    }

    @Test
    public void testCopySafeNodes_validElement() {
        String sourceHtml = "<div><p>Safe text</p></div>";
        Document sourceDoc = Jsoup.parse(sourceHtml);
        Document destDoc = Document.createShell("");

        int numDiscarded = cleaner.copySafeNodes(sourceDoc.body(), destDoc.body());

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_unsafeElement() {
        String sourceHtml = "<div><script>alert('XSS');</script></div>";
        Document sourceDoc = Jsoup.parse(sourceHtml);
        Document destDoc = Document.createShell("");

        int numDiscarded = cleaner.copySafeNodes(sourceDoc.body(), destDoc.body());

        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_textNode() {
        String sourceHtml = "<div>Safe text</div>";
        Document sourceDoc = Jsoup.parse(sourceHtml);
        Document destDoc = Document.createShell("");

        int numDiscarded = cleaner.copySafeNodes(sourceDoc.body(), destDoc.body());

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement_safeTag() {
        String sourceHtml = "<div><p>Safe text</p></div>";
        Document sourceDoc = Jsoup.parse(sourceHtml);
        Element sourceElement = sourceDoc.body().child(0);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals(sourceElement.tagName(), elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_unsafeTag() {
        String sourceHtml = "<div><script>alert('XSS');</script></div>";
        Document sourceDoc = Jsoup.parse(sourceHtml);
        Element sourceElement = sourceDoc.body().child(0);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals(sourceElement.tagName(), elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_unsafeAttribute() {
        String sourceHtml = "<div><img src='invalid.jpg'></div>";
        Document sourceDoc = Jsoup.parse(sourceHtml);
        Element sourceElement = sourceDoc.body().child(0);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals(sourceElement.tagName(), elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_enforcedAttributes() {
        String sourceHtml = "<div><a href='#' target='_blank'>Link</a></div>";
        Document sourceDoc = Jsoup.parse(sourceHtml);
        Element sourceElement = sourceDoc.body().child(0);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals(sourceElement.tagName(), elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertTrue(sourceElement.attributes().size() >= elementMeta.el.attributes().size());
    }

}