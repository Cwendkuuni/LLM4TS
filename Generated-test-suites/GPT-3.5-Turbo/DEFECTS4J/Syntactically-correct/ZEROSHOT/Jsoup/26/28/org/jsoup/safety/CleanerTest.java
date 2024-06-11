import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CleanerTest {
    private static Cleaner cleaner;

    @BeforeClass
    public static void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<p>This is <b>dirty</b> HTML.</p>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expectedHtml = "<html>\n" +
                              " <head></head>\n" +
                              " <body>\n" +
                              "  <p>This is <b>dirty</b> HTML.</p>\n" +
                              " </body>\n" +
                              "</html>";
        assertEquals(expectedHtml, cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        String safeHtml = "<p>This is <b>safe</b> HTML.</p>";
        Document safeDocument = Jsoup.parse(safeHtml);
        boolean isValid = cleaner.isValid(safeDocument);
        assertEquals(true, isValid);

        String unsafeHtml = "<p>This is <script>unsafe</script> HTML.</p>";
        Document unsafeDocument = Jsoup.parse(unsafeHtml);
        boolean isUnsafe = cleaner.isValid(unsafeDocument);
        assertEquals(false, isUnsafe);
    }

    @Test
    public void testCopySafeNodes() {
        String dirtyHtml = "<p>This is <b>dirty</b> HTML.</p>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Element source = dirtyDocument.body();
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element dest = cleanDocument.body();

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        String expectedHtml = "<p>This is <b>dirty</b> HTML.</p>";
        assertEquals(expectedHtml, dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        String sourceTag = "a";
        Element sourceEl = new Element(sourceTag);
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals(sourceEl.tagName(), meta.el.tagName());
        assertEquals(0, meta.numAttribsDiscarded);
    }
}