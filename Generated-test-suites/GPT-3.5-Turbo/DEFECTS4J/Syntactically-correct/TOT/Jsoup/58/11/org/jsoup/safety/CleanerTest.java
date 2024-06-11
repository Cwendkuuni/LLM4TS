import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        // Typical use case: cleaning a dirty document
        Document dirtyDocument = new Document("<html><body><h1>Hello World!</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Expect the clean document to only contain the cleaned elements
        Element body = cleanDocument.body();
        Assert.assertEquals(body.children().size(), 1);
        Assert.assertEquals(body.child(0).tagName(), "h1");
        Assert.assertEquals(body.child(0).text(), "Hello World!");
    }

    @Test
    public void testIsValid() {
        // Typical use case: validating a clean document
        Document cleanDocument = new Document("<html><body><p>Hello World!</p></body></html>");
        boolean isValid = cleaner.isValid(cleanDocument);

        // Expect the clean document to be valid
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidWithUnsafeTag() {
        // Edge case: validating a clean document with an unsafe tag
        Document cleanDocument = new Document("<html><body><script>alert('Hello World!');</script></body></html>");
        boolean isValid = cleaner.isValid(cleanDocument);

        // Expect the clean document to be invalid
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidWithUnsafeAttribute() {
        // Edge case: validating a clean document with an unsafe attribute
        Document cleanDocument = new Document("<html><body><a href='javascript:alert('Hello World!');'>Click me</a></body></html>");
        boolean isValid = cleaner.isValid(cleanDocument);

        // Expect the clean document to be invalid
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidWithHeadContent() {
        // Edge case: validating a clean document with content in the head tag
        Document cleanDocument = new Document("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
        boolean isValid = cleaner.isValid(cleanDocument);

        // Expect the clean document to be invalid
        Assert.assertFalse(isValid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanWithNullDocument() {
        // Error scenario: cleaning a null document
        cleaner.clean(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidWithNullDocument() {
        // Error scenario: validating a null document
        cleaner.isValid(null);
    }
}