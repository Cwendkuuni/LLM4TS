package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.util.Attributes;
import java.util.List;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist(); // Assume a valid Whitelist instance
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", "");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);
        Assert.assertFalse(cleanDocument.body().childNodes().hasTextualContent()); // No script tags should remain
    }

    @Test
    public void testIsValidDocument_ValidDocument() {
        Document validDocument = Document.createFromString("<html><body><h1>Hello World</h1></body></html>", "");
        Cleaner cleaner = new Cleaner(new DefaultWhitelist()); // Use DefaultWhitelist for simplicity
        Assert.assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testIsValidDocument_InvalidDocument() {
        Document invalidDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", "");
        Cleaner cleaner = new Cleaner(new DefaultWhitelist());
        Assert.assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes_SafeElement() {
        Document dirtyDocument = Document.createFromString("<html><body><div id='safeDiv' class='safeClass'>Safe Content</div></body></html>", "");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element safeDiv = dirtyDocument.selectFirst("div#safeDiv");
        Element destination = new Element(Tag.DIV, dirtyDocument.baseUri(), new Attributes());

        int numDiscarded = cleaner.copySafeNodes(safeDiv, destination);
        Assert.assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_UnsafeElement() {
        Document dirtyDocument = Document.createFromString("<html><body><script id='unsafeScript' src='malicious.js'>Malicious Script</script><div id='safeDiv'>Safe Content</div></body></html>", "");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element unsafeScript = dirtyDocument.selectFirst("script#unsafeScript");
        Element destination = new Element(Tag.DIV, dirtyDocument.baseUri(), new Attributes());

        int numDiscarded = cleaner.copySafeNodes(unsafeScript, destination);
        Assert.assertEquals(1, numDiscarded); // Unsafe script tag should be discarded
    }

    @Test
    public void testCreateSafeElement_SafeElement() {
        Document dirtyDocument = Document.createFromString("<html><body><div id='safeDiv' class='safeClass'>Safe Content</div></body></html>", "");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = dirtyDocument.selectFirst("div#safeDiv");
        ElementMeta result = cleaner.createSafeElement(sourceElement);

        Assert.assertNotNull(result.el);
        Assert.assertNotNull(sourceElement.tagName());
        Assert.assertEquals(sourceElement.tagName(), result.el.tagName());
        Assert.assertNotNull(sourceElement.attributes());
        Assert.assertEquals(sourceElement.attributes().size(), result.numAttribsDiscarded);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateSafeElement_NullElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = null;
        ElementMeta result = cleaner.createSafeElement(sourceElement);

        // NullPointerException should be thrown when sourceElement is null
    }

    @Test
    public void testCopySafeNodes_MixedElements() {
        Document dirtyDocument = Document.createFromString("<html><body><script id='unsafeScript' src='malicious.js'>Malicious Script</script><div id='safeDiv'>Safe Content</div></body></html>", "");
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element unsafeScript = dirtyDocument.selectFirst("script#unsafeScript");
        Element safeDiv = dirtyDocument.selectFirst("div#safeDiv");
        Element destination = new Element(Tag.DIV, dirtyDocument.baseUri(), new Attributes());

        int numDiscarded = cleaner.copySafeNodes(unsafeScript, destination);
        numDiscarded += cleaner.copySafeNodes(safeDiv, destination);

        Assert.assertEquals(2, numDiscarded); // Unsafe script tag and an empty div tag should be discarded
    }
}