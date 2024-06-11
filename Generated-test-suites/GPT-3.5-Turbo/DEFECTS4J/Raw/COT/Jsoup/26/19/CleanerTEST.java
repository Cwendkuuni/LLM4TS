import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        Assert.assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.valueOf("div"), "");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        Assert.assertNotNull(elementMeta.el);
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}