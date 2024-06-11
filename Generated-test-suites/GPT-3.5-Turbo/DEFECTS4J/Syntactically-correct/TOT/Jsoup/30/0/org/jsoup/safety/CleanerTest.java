import org.junit.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CleanerTest {

    private static Cleaner cleaner;
    
    @BeforeClass
    public static void setup() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }
    
    @Test
    public void testClean() {
        String dirtyHTML = "<html><body><div><p>Safe content</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanedDocument);
        
        // Check if the cleaned document only contains allowed tags and attributes
        Assert.assertEquals("html", cleanedDocument.tagName());
        Assert.assertEquals("body", cleanedDocument.body().tagName());
        Assert.assertEquals("div", cleanedDocument.body().child(0).tagName());
        Assert.assertEquals("p", cleanedDocument.body().child(0).child(0).tagName());
        Assert.assertTrue(cleanedDocument.body().child(0).child(0).hasText());
        
        // Check if the original dirty document is not modified
        Assert.assertNotEquals("html", dirtyDocument.tagName());
        Assert.assertNotEquals("body", dirtyDocument.body().tagName());
        Assert.assertNotEquals("div", dirtyDocument.body().child(0).tagName());
        Assert.assertNotEquals("p", dirtyDocument.body().child(0).child(0).tagName());
        Assert.assertFalse(dirtyDocument.body().child(0).child(0).hasText());
    }
    
    @Test
    public void testIsValid() {
        String dirtyHTML = "<html><body><div><p>Safe content</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);
        
        // Check if the original dirty document is not modified
        Assert.assertNotEquals("html", dirtyDocument.tagName());
        Assert.assertNotEquals("body", dirtyDocument.body().tagName());
        Assert.assertNotEquals("div", dirtyDocument.body().child(0).tagName());
        Assert.assertNotEquals("p", dirtyDocument.body().child(0).child(0).tagName());
        Assert.assertFalse(dirtyDocument.body().child(0).child(0).hasText());
    }
    
    @Test
    public void testCopySafeNodes() {
        Element root = new Element("div");
        Element destination = new Element("div");
        
        Element pElement = new Element("p");
        pElement.text("Safe content");
        root.appendChild(pElement);
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        Assert.assertEquals(0, numDiscarded);
        
        Assert.assertEquals(1, destination.children().size());
        Assert.assertEquals("p", destination.children().get(0).tagName());
        Assert.assertTrue(destination.children().get(0).hasText());
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element("div");
        sourceElement.attr("class", "class1");
        sourceElement.attr("data-src", "image.jpg");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Assert.assertNotNull(elementMeta);
        Assert.assertEquals("div", elementMeta.el.tagName());
        Assert.assertEquals("class1", elementMeta.el.attr("class"));
        Assert.assertEquals("image.jpg", elementMeta.el.attr("data-src"));
        Assert.assertEquals(2, elementMeta.numAttribsDiscarded);
    }
}