import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic()); // Using basic whitelist for simplicity
    }

    @Test
    public void testClean_WithValidDocument_ShouldReturnCleanedDocument() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals("<html><head></head><body><div><p>Hello World</p></div></body></html>", cleanDocument.toString());
    }

    @Test
    public void testClean_WithNullDocument_ShouldThrowException() {
        try {
            cleaner.clean(null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            Assert.assertEquals("dirtyDocument must not be null", e.getMessage());
        }
    }

    @Test
    public void testIsValid_WithValidDocument_ShouldReturnTrue() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_WithInvalidDocument_ShouldReturnFalse() {
        Document dirtyDocument = Jsoup.parse("<html><head><script>alert('Hello World');</script></head><body><div><p>Hello World</p></div></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValid_WithNullDocument_ShouldThrowException() {
        try {
            cleaner.isValid(null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            Assert.assertEquals("dirtyDocument must not be null", e.getMessage());
        }
    }
}