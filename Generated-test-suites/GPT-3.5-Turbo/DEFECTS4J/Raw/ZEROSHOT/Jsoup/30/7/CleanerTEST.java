import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;

public class CleanerTest {
    
    @Test
    public void testClean() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = body.appendElement("div");

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert that the clean document only contains the allowed elements
        assertTrue(cleanDocument.getElementsByTag("html").isEmpty());
        assertTrue(cleanDocument.getElementsByTag("head").isEmpty());
        assertTrue(cleanDocument.getElementsByTag("body").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("div").size() == 1);
    }
    
    @Test
    public void testIsValid() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = body.appendElement("div");

        // Check if the dirty document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert that the dirty document is not valid
        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = body.appendElement("div");
        TextNode textNode = new TextNode("Hello, World!", "http://example.com");
        div.appendChild(textNode);

        // Create a clean document
        Document cleanDocument = Document.createShell("http://example.com");
        Element cleanBody = cleanDocument.body();

        // Copy the safe nodes from the dirty document to the clean document
        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

        // Assert that the safe nodes were copied correctly
        assertTrue(cleanBody.getElementsByTag("div").size() == 1);
        assertTrue(cleanBody.getElementsByTag("div").text().equals("Hello, World!"));
        assertTrue(numDiscarded == 0);
    }
    
    @Test
    public void testCreateSafeElement() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty element
        Element dirtyElement = new Element(Tag.valueOf("div"), "http://example.com");
        dirtyElement.attr("class", "red");
        dirtyElement.attr("style", "font-weight: bold");

        // Create a safe element using the createSafeElement method
        Cleaner.ElementMeta safeElementMeta = cleaner.createSafeElement(dirtyElement);
        Element safeElement = safeElementMeta.el;

        // Assert that the safe element contains only the allowed attributes
        assertTrue(safeElement.tagName().equals("div"));
        assertTrue(safeElement.attributes().size() == 1);
        assertTrue(safeElement.hasAttr("class"));
        assertTrue(safeElement.attr("class").equals("red"));
    }
}