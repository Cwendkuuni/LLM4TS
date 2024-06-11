
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.List;

public class CleanerTest {
    @Test
    public void testClean() {
        // Create a new cleaner
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Create a dirty document
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><div><a href=\"https://www.example.com\">Link</a></div></body></html>");

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert that the clean document only contains whitelisted elements
        List<Node> nodes = cleanDocument.body().childNodes();
        for (Node node : nodes) {
            if (node instanceof Element) {
                Element element = (Element) node;
                assertTrue(cleaner.whitelist.isSafeTag(element.tagName()));
                Attributes attributes = element.attributes();
                for (Attribute attribute : attributes) {
                    assertTrue(cleaner.whitelist.isSafeAttribute(element.tagName(), element, attribute));
                }
            }
        }
    }

    @Test
    public void testIsValid() {
        // Create a new cleaner
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Create a dirty document
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><div><a href=\"https://www.example.com\">Link</a></div></body></html>");

        // Assert that the dirty document is valid according to the whitelist
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        // Create a new cleaner
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Create a source element
        Element source = new Element("div");

        // Add attributes to the source element
        source.attr("class", "test");
        source.attr("id", "123");

        // Create a destination element
        Element destination = new Element("div");

        // Copy the safe nodes from the source element to the destination element
        int numDiscarded = cleaner.copySafeNodes(source, destination);

        // Assert that the attributes of the destination element are correct
        Attributes attributes = destination.attributes();
        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("class"));
        assertEquals("test", attributes.get("class"));
        assertTrue(attributes.hasKey("id"));
        assertEquals("123", attributes.get("id"));

        // Assert that the number of discarded nodes is 0
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a new cleaner
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Create a source element
        Element source = new Element("a");

        // Add attributes to the source element
        source.attr("href", "https://www.example.com");
        source.attr("target", "_blank");

        // Create a safe element from the source element
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);
        Element safeElement = elementMeta.el;

        // Assert that the tag name of the safe element is correct
        assertEquals("a", safeElement.tagName());

        // Assert that the attributes of the safe element are correct
        Attributes attributes = safeElement.attributes();
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("href"));
        assertEquals("https://www.example.com", attributes.get("href"));

        // Assert that the number of discarded attributes is 1
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}
