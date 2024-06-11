import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test 1: Test clean method with a null input
        assertNull(new Cleaner(Whitelist.relaxed()).clean(null));

        // Test 2: Test clean method with an empty document
        Document emptyDoc = Document.createShell("");
        assertEquals(emptyDoc, new Cleaner(Whitelist.relaxed()).clean(emptyDoc));

        // Test 3: Test clean method with a document containing only safe elements
        Document safeDoc = Document.createShell("");
        safeDoc.body().append("<p>Paragraph</p><a href=\"https://example.com\">Link</a>");
        Document cleanSafeDoc = new Cleaner(Whitelist.relaxed()).clean(safeDoc);
        assertEquals(safeDoc.body().html(), cleanSafeDoc.body().html());

        // Test 4: Test clean method with a document containing unsafe elements
        Document unsafeDoc = Document.createShell("");
        unsafeDoc.body().append("<script>alert('XSS');</script>");
        Document cleanUnsafeDoc = new Cleaner(Whitelist.relaxed()).clean(unsafeDoc);
        assertTrue(cleanUnsafeDoc.body().html().isEmpty());
    }

    @Test
    public void testIsValid() {
        // Test 1: Test isValid method with a null input
        assertFalse(new Cleaner(Whitelist.relaxed()).isValid(null));

        // Test 2: Test isValid method with an empty document
        Document emptyDoc = Document.createShell("");
        assertTrue(new Cleaner(Whitelist.relaxed()).isValid(emptyDoc));

        // Test 3: Test isValid method with a document containing only safe elements
        Document safeDoc = Document.createShell("");
        safeDoc.head().append("head content");
        assertTrue(new Cleaner(Whitelist.relaxed()).isValid(safeDoc));

        // Test 4: Test isValid method with a document containing unsafe elements
        Document unsafeDoc = Document.createShell("");
        unsafeDoc.body().append("<p>Paragraph</p><script>alert('XSS');</script>");
        assertFalse(new Cleaner(Whitelist.relaxed()).isValid(unsafeDoc));
    }

}