import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CleanerTest {
    private static Cleaner cleaner;

    @BeforeClass
    public static void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<p>Hello, <span>world!</span></p>");
    
        Document cleanDocument = cleaner.clean(dirtyDocument);
    
        Element body = cleanDocument.body();
        Elements paragraphs = body.getElementsByTag("p");
        Assert.assertEquals(1, paragraphs.size());
    
        Element paragraph = paragraphs.get(0);
        Assert.assertEquals("Hello, world!", paragraph.text());
    
        Elements spans = paragraph.getElementsByTag("span");
        Assert.assertEquals(1, spans.size());
    }
    
    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<p>Hello, <span>world!</span></p>");
    
        boolean isValid = cleaner.isValid(dirtyDocument);
    
        Assert.assertTrue(isValid);
    }
}