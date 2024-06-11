import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

import java.util.List;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setup() {
        cleaner = new Cleaner(new Whitelist());
        dirtyDocument = Document.createShell("");
        dirtyDocument.body().append("Test content");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("", cleanDocument.head().html());
        Assert.assertEquals("Test content", cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        Assert.assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = Document.createShell("").body();
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<div></div>", dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        Assert.assertEquals("div", meta.el.tagName());
        Assert.assertEquals(0, meta.numAttribsDiscarded);
    }
}