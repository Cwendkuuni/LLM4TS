import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import java.util.List;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}