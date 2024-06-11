import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = null; // Test for null whitelist
        try {
            new Cleaner(whitelist);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Whitelist emptyWhitelist = Whitelist.empty(); // Test for empty whitelist
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Jsoup.parse("<html></html>"); // Test for empty document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());

        // Test for invalid document
        Document invalidDocument = Jsoup.parse("<html><script>alert('XSS')</script></html>");
        cleaner = new Cleaner(whitelist);
        cleanDocument = cleaner.clean(invalidDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());

        // Test for invalid tag name in whitelist
        Whitelist invalidWhitelist = new Whitelist() {{
            addTag("invalidTag", null, null); // Invalid tag name
        }};
        cleaner = new Cleaner(invalidWhitelist);
        Document invalidTagDocument = Jsoup.parse("<html><invalidTag></invalidTag></html>");
        cleanDocument = cleaner.clean(invalidTagDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());

        // Test for multiple occurrences of the same tag in the document
        Document multipleTagDocument = Jsoup.parse("<html><body><p><span>Tag: <b>Bold</b></span></p><p><span>Tag: <b>Bold</b></span></p></body></html>");
        Element dest = new Element(Tag.BODY, null);
        cleaner = new Cleaner(whitelist);
        int numDiscarded = cleaner.copySafeNodes(multipleTagDocument.body(), dest);
        assertEquals(0, numDiscarded);

        // Test for multiple occurrences of the same attribute in the document
        Document multipleAttributeDocument = Jsoup.parse("<html><body><div id='id' class='class'></div></body></html>");
        cleaner = new Cleaner(whitelist);
        cleanDocument = cleaner.clean(multipleAttributeDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());

        // Test for malformed HTML input
        Document malformedDocument = Jsoup.parse("<html><body><p><span>Tag: <b>Bold</b></p></html>");
        cleaner = new Cleaner(whitelist);
        cleanDocument = cleaner.clean(malformedDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());

        // Test for nested elements with different whitelist permissions
        Document nestedDocuments = Jsoup.parse("<html><body><div><p><span>Safe tag: <b>Bold</b></span></p></div></body></html>");
        Element destNested = new Element(Tag.BODY, null);
        cleaner = new Cleaner(whitelist);
        int numDiscardedNested = cleaner.copySafeNodes(nestedDocuments.body(), destNested);
        assertEquals(1, numDiscardedNested);

        // Test for edge cases in copySafeNodes method
        Document edgeCaseDocument = Jsoup.parse("<html><body><p><span></span></p></body></html>"); // Empty tags
        cleaner = new Cleaner(whitelist);
        cleanDocument = cleaner.clean(edgeCaseDocument);
        assertEquals(0, cleanDocument.body().childNodeSize());

        Document edgeCaseDocumentAttributes = Jsoup.parse("<html><body><p><span id='id' class='class'></span></p></body></html>"); // Empty attributes
        cleaner = new Cleaner(whitelist);
        cleanDocument = cleaner.clean(edgeCaseDocumentAttributes);
        assertEquals(0, cleanDocument.body().childNodeSize());

        // Test for edge cases in createSafeElement method
        Document edgeCaseDocumentChildren = Jsoup.parse("<html><body><p><span><i>Italic</i></i></span></p></body></html>"); // Nested tags
        ElementMeta meta = new Cleaner().createSafeElement(edgeCaseDocumentChildren.body().element(0));
        assertEquals(Tag.I, meta.el.tagName());
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = Whitelist.simpleText();
        Document dirtyDocument = Jsoup.parse("<div><p></p></div>");
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertEquals(true, isValid);

        // Test for invalid document
        Document invalidDocument = Jsoup.parse("<html><script>alert('XSS')</script></html>");
        cleaner = new Cleaner(whitelist);
        isValid = cleaner.isValid(invalidDocument);
        assertEquals(false, isValid);

        // Test for empty document
        Document emptyDocument = Jsoup.parse("<html></html>");
        cleaner = new Cleaner(whitelist);
        isValid = cleaner.isValid(emptyDocument);
        assertEquals(true, isValid);
    }

    @Test
    public void testCleanMethodWithNullDocument() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = null; // Test for null document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(null, cleanDocument);
    }

    // Other edge cases and exception handling test cases can be added here

    private static class ElementMeta {
        Element el;
        int numAttribsDiscarded;

        ElementMeta(Element el, int numAttribsDiscarded) {
            this.el = el;
            this.numAttribsDiscarded = numAttribsDiscarded;
        }
    }
}