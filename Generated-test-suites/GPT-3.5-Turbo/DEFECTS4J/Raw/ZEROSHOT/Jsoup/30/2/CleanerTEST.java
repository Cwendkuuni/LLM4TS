import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.safety.Cleaner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Elements bodyElements = cleanDocument.body().children();

        assertEquals(2, bodyElements.size());
        assertEquals("h1", bodyElements.get(0).tagName());
        assertEquals("p", bodyElements.get(1).tagName());
    }

    @Test
    public void testIsValid() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dirtyDocument.body());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        String html = "<div class=\"test\" id=\"123\"><p>Paragraph</p><a href=\"https://example.com\">Link</a></div>";
        Document dirtyDocument = Jsoup.parse(html);
        Elements divElements = dirtyDocument.getElementsByTag("div");
        ElementMeta elementMeta = cleaner.createSafeElement(divElements.get(0));

        assertEquals(1, elementMeta.numAttribsDiscarded);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals("test", elementMeta.el.className());
        assertEquals("123", elementMeta.el.id());
    }
}