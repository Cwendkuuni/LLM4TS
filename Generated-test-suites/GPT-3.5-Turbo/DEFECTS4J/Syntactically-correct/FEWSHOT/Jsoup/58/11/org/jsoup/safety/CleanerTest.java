
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        // Create a cleaner object with the whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document
        Document dirtyDocument = new Document("<html><head><title>Dirty Document</title></head><body><h1>Hello World!</h1><div><p>Dirty content</p></div></body></html>");

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Ensure the clean document is not null
        assertNotNull(cleanDocument);
        
        // Ensure the clean document does not contain the <head> tag
        assertNull(cleanDocument.head());
        
        // Ensure the clean document contains the <body> tag
        assertNotNull(cleanDocument.body());
        
        // Ensure the clean document contains the <h1> tag
        assertNotNull(cleanDocument.body().getElementsByTag("h1").first());
        
        // Ensure the clean document contains the <p> tag
        assertNotNull(cleanDocument.body().getElementsByTag("p").first());
        
        // Ensure the clean document does not contain the <div> tag
        assertNull(cleanDocument.body().getElementsByTag("div").first());
    }
    
    @Test
    public void testIsValid() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        // Create a cleaner object with the whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document
        Document dirtyDocument = new Document("<html><head><title>Dirty Document</title></head><body><h1>Hello World!</h1><div><p>Dirty content</p></div></body></html>");

        // Check if the dirty document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Ensure the dirty document is not valid
        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        // Create a cleaner object with the whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a source element
        Element source = new Element("div");
        source.html("<p>Safe content</p>");

        // Create a destination element
        Element dest = new Element("div");

        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Ensure the number of discarded nodes is 0
        assertEquals(0, numDiscarded);
        
        // Ensure the destination element contains the <p> tag
        assertNotNull(dest.getElementsByTag("p").first());
    }
    
    @Test
    public void testCreateSafeElement() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        // Create a cleaner object with the whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a source element
        Element source = new Element("div");

        // Create an attribute for the source element
        Attribute attribute = new Attribute("data-src", "example.jpg");
        source.attributes().put(attribute);

        // Create a safe element from the source element
        ElementMeta elementMeta = cleaner.createSafeElement(source);

        // Ensure the element meta is not null
        assertNotNull(elementMeta);
        
        // Ensure the element meta contains the destination element
        assertNotNull(elementMeta.el);
        
        // Ensure the element meta contains the number of discarded attributes
        assertEquals(0, elementMeta.numAttribsDiscarded);
        
        // Ensure the destination element contains the attribute
        assertEquals("example.jpg", elementMeta.el.attr("data-src"));
    }
    
    @Test
    public void testCleaningVisitor() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        // Create a cleaner object with the whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a root element
        Element root = new Element("html");

        // Create a destination element
        Element dest = new Element("body");

        // Create a cleaning visitor
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, dest);

        // Create a source element
        Element source = new Element("div");
        source.html("<p>Safe content</p>");

        // Clean the source element using the cleaning visitor
        cleaningVisitor.head(source, 0);

        // Ensure the destination element contains the <div> tag
        assertNotNull(dest.getElementsByTag("div").first());
        
        // Ensure the destination element contains the <p> tag
        assertNotNull(dest.getElementsByTag("p").first());
        
        // Ensure the destination element does not contain the <head> tag
        assertNull(dest.getElementsByTag("head").first());
    }

}

