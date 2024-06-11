import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class CleanerTest {

    private Document dirtyDocument;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        dirtyDocument = Jsoup.parse("<html><head><title>Dirty Document</title></head><body><p>Test paragraph</p></body></html>");
        cleaner = new Cleaner(Whitelist.basic()); // using basic whitelist for testing purposes
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = dirtyDocument.body();
        Element dest = new Element(Tag.valueOf("div"), source.baseUri());
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = dirtyDocument.body().child(0);
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}