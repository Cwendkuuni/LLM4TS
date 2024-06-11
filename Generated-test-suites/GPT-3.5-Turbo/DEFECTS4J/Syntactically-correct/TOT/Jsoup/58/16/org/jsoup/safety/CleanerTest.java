import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        String html = "<html><head><title>Test</title></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals("Title", cleanDocument.head().select("title").text());
        Assert.assertEquals("Title", cleanDocument.body().select("h1").text());
        Assert.assertEquals("Paragraph", cleanDocument.body().select("p").text());
    }

    @Test
    public void testClean_WithEmptyBody() {
        String html = "<html><head><title>Test</title></head></html>";
        Document dirtyDocument = Jsoup.parse(html);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals("Test", cleanDocument.head().select("title").text());
        Assert.assertNotNull(cleanDocument.body());
        Assert.assertEquals(0, cleanDocument.body().children().size());
    }

    @Test
    public void testIsValid_ValidDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        String html = "<html><head><title>Test</title></head><body><script>alert('Vulnerable')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValid_EmptyHead() {
        String html = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid);
    }

}