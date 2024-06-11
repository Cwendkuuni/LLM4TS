import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.Validate;

public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Assertions.assertNotNull(cleaner);
    }

    @Test
    public void testCleanEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.create();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assertions.assertEquals("<html><body></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testCleanInvalidDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS')</script></body></html>", "test.html");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assertions.assertDoesNotThrow(() -> cleanDocument.body().outputSources());
        Assertions.assertEquals("<html><body></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testCleanMalformedDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createFromString("<html<body></html>", "test.html");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assertions.assertEquals("<html><body></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testCleanDocumentWithScriptTags() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS')</script></body></html>", "test.html");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assertions.assertEquals("<html><body></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testCleanDocumentWithNullWhitelist() {
        Document dirtyDocument = Document.createFromString("<html><body></body></html>", "test.html");
        Cleaner cleaner = new Cleaner(null);
        Assertions.assertThrows(NullPointerException.class, () -> cleaner.clean(dirtyDocument));
    }

    @Test
    public void testCleanDocumentWithNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Assertions.assertThrows(NullPointerException.class, () -> cleaner.clean(null));
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createFromString("<html><body><h1>Test</h1></body></html>", "test.html");
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsValidInvalidDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS')</script></body></html>", "test.html");
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createFromString("<html><body><h1 id='test'>Test</h1></body></html>", "test.html");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = Document.create();
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        Element h1 = cleanDocument.body().getElementsByTag("h1").first();
        Assertions.assertEquals("Test", h1.text());
        Assertions.assertEquals("test", h1.id());
        Assertions.assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createFromString("<html><body><h1 id='test' class='test' style='color: red'><img src='image.png'></h1></body></html>", "test.html");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = Document.create();
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        Element h1 = cleanDocument.body().getElementsByTag("h1").first();
        Assertions.assertEquals("Test", h1.text());
        Assertions.assertEquals("test", h1.id());
        Assertions.assertEquals(0, numDiscarded);
        Assertions.assertNull(h1.getElementsByTag("img").first());
    }
}

//