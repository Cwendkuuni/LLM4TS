
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        
        // Create a cleaner object
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create a dirty document
        String html = "<html><body><div><p>Test</p></div></body></html>";
        Document dirtyDocument = Parser.parse(html, "");
        
        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert that the clean document only contains allowed elements
        Assert.assertEquals("<html><head></head><body><div><p>Test</p></div></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        
        // Create a cleaner object
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create a dirty document
        String html = "<html><body><div><p>Test</p></div></body></html>";
        Document dirtyDocument = Parser.parse(html, "");
        
        // Check if the dirty document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Assert that the dirty document is valid
        Assert.assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        
        // Create a cleaner object
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create source and destination elements
        Document source = Parser.parseBodyFragment("<div><p>Test</p></div>");
        Element dest = new Element(Tag.valueOf("body"), "");
        
        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(source.body(), dest);
        
        // Assert that the number of discarded elements is 0
        Assert.assertEquals(0, numDiscarded);
        // Assert that the destination element contains the copied safe nodes
        Assert.assertEquals("<div><p>Test</p></div>", dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        
        // Create a cleaner object
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create a source element
        Element source = new Element(Tag.valueOf("p"), "");
        source.attr("src", "image.jpg");
        source.attr("width", "100px");
        
        // Create a safe element from the source element
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);
        Element dest = elementMeta.el;
        
        // Assert that the destination element contains only safe attributes
        Assert.assertEquals("<p src=\"image.jpg\"></p>", dest.html());
        // Assert that the number of discarded attributes is 1
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}

