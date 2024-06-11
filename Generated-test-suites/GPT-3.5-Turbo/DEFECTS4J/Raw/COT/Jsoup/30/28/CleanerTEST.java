
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.safety.Whitelist;

import java.util.List;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a Whitelist
        Whitelist whitelist = new Whitelist();

        // Create a Cleaner with the Whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty Document
        Document dirtyDocument = Document.createShell("");

        // Call the clean method and get the cleaned Document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check if the cleanDocument is not null
        Assert.assertNotNull(cleanDocument);

        // Check if the cleanDocument has the same baseUri as the dirtyDocument
        Assert.assertEquals(dirtyDocument.baseUri(), cleanDocument.baseUri());

        // Check if the cleanDocument has an empty body
        Assert.assertTrue(cleanDocument.body().childNodes().isEmpty());
    }

    @Test
    public void testIsValid() {
        // Create a Whitelist
        Whitelist whitelist = new Whitelist();

        // Create a Cleaner with the Whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty Document
        Document dirtyDocument = Document.createShell("");

        // Call the isValid method and get the result
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Check if the isValid is true
        Assert.assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a Whitelist
        Whitelist whitelist = new Whitelist();

        // Create a Cleaner with the Whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a source Element
        Element source = new Element(Tag.valueOf("div"), "");

        // Create a destination Element
        Element destination = new Element(Tag.valueOf("div"), "");

        // Call the copySafeNodes method and get the number of discarded nodes
        int numDiscarded = cleaner.copySafeNodes(source, destination);

        // Check if the destination Element has the same tag as the source Element
        Assert.assertEquals(source.tagName(), destination.tagName());

        // Check if the number of discarded nodes is 0
        Assert.assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a Whitelist
        Whitelist whitelist = new Whitelist();

        // Create a Cleaner with the Whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a source Element
        Element source = new Element(Tag.valueOf("div"), "");

        // Call the createSafeElement method and get the safe Element and the number of discarded attributes
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);

        // Check if the safe Element has the same tag as the source Element
        Assert.assertEquals(source.tagName(), elementMeta.el.tagName());

        // Check if the number of discarded attributes is 0
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}
