import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanTypicalCase() {
        Whitelist whitelist = Whitelist.basic(); // Creating a basic whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Creating a dirty document with some unsafe tags and attributes
        String dirtyHTML = "<html><head></head><body><div><script>alert('XSS');</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);

        // Cleaning the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // The cleaned document should not contain the script tag
        assertFalse(cleanDocument.html().contains("script"));
    }
    
    @Test
    public void testCleanEdgeCases() {
        Whitelist whitelist = Whitelist.basic(); // Creating a basic whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Creating a dirty document with different scenarios
        String dirtyHtml = "<html><head></head><body><p><script>alert('XSS');</script></p>" +
                          "<img src='image.jpg' onerror='javascript:alert('Error');' />" +
                          "<a href='javascript:alert('XSS');'>Click Me</a></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Cleaning the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // The cleaned document should not contain any unsafe tags or attributes
        assertFalse(cleanDocument.html().contains("script"));
        assertFalse(cleanDocument.html().contains("onerror"));
        assertFalse(cleanDocument.html().contains("href"));
    }

    @Test
    public void testIsValidTypicalCase() {
        Whitelist whitelist = Whitelist.basic(); // Creating a basic whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Creating a dirty document with no unsafe tags or attributes
        String dirtyHtml = "<html><head></head><body><p>Hello, World!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Checking if the dirty document is valid
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidErrorScenarios() {
        Whitelist whitelist = Whitelist.basic(); // Creating a basic whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Creating a dirty document with unsafe tags or attributes
        String dirtyHtml = "<html><head></head><body><p><script>alert('XSS');</script></p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Checking if the dirty document is valid
        assertFalse(cleaner.isValid(dirtyDocument));
    }

}