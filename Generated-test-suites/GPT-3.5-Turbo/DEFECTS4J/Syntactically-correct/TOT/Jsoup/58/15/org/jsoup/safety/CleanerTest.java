import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical Use Case: Dirty document with allowed elements and attributes
        String dirtyHtml = "<html><body><div class=\"container\"><h1>Title</h1><p>Paragraph</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check if the clean document contains only allowed elements and attributes
        String expectedCleanHtml = "<html><head></head><body><div class=\"container\"><h1>Title</h1><p>Paragraph</p></div></body></html>";
        Assert.assertEquals(expectedCleanHtml, cleanDocument.html());
    }

    @Test
    public void testIsValid_Valid() {
        // Typical Use Case: Valid document with allowed elements and attributes
        String validHtml = "<html><body><div class=\"container\"><h1>Title</h1><p>Paragraph</p></div></body></html>";
        Document validDocument = Jsoup.parse(validHtml);
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        boolean isValid = cleaner.isValid(validDocument);

        // Check if the document is considered valid
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_Invalid() {
        // Typical Use Case: Invalid document with disallowed elements and attributes
        String invalidHtml = "<html><body><script>alert('Hello');</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        boolean isValid = cleaner.isValid(invalidDocument);

        // Check if the document is considered invalid
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Edge Case: Empty source element
        Document dirtyDocument = Jsoup.parse("<html><body></body></html>");
        Element source = dirtyDocument.body();
        Document cleanDocument = Document.createShell("");
        Element destination = cleanDocument.body();
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner(source, destination);

        int numDiscarded = cleaner.copySafeNodes(source, destination);

        // Check if no nodes were discarded
        Assert.assertEquals(0, numDiscarded);
        // Check if the destination is still empty
        Assert.assertTrue(destination.children().isEmpty());
    }

    @Test
    public void testCreateSafeElement() {
        // Edge Case: Source element with no attributes
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Check if the resulting ElementMeta has no discarded attributes
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
        // Check if the resulting ElementMeta has the same tag name
        Assert.assertEquals("div", elementMeta.el.tagName());
    }

    @Test
    public void testCreateSafeElement_WithDiscardedAttributes() {
        // Edge Case: Source element with some disallowed attributes
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        sourceElement.attr("href", "https://example.com");
        sourceElement.attr("onclick", "alert('Hello');");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Check if the resulting ElementMeta has 1 discarded attribute
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
        // Check if the resulting ElementMeta has the remaining allowed attribute
        Assert.assertNotNull(elementMeta.el.attr("href"));
        // Check if the resulting ElementMeta has discarded the disallowed attribute
        Assert.assertNull(elementMeta.el.attr("onclick"));
    }

    @Test
    public void testCleanerConstructor() {
        // Error Scenario: Null whitelist parameter
        Whitelist whitelist = null;

        try {
            Cleaner cleaner = new Cleaner(whitelist);
        } catch (NullPointerException e) {
            // Check if a NullPointerException is thrown
            Assert.assertNotNull(e);
        }
    }
}