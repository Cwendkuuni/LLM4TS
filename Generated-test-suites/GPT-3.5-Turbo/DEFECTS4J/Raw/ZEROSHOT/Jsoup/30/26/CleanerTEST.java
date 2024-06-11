import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
      // Create a dirty document
      String html = "<html><body><script>alert('Hello, world!');</script></body></html>";
      Document dirtyDocument = Jsoup.parse(html);
      
      // Clean the dirty document
      Document cleanDocument = cleaner.clean(dirtyDocument);
      
      // The clean document should not contain the script tag
      assertFalse(cleanDocument.toString().contains("<script>"));
    }

    @Test
    public void testIsValid() {
      // Create a dirty document
      String html = "<html><body><p>Hello, world!</p></body></html>";
      Document dirtyDocument = Jsoup.parse(html);
      
      // Check if the dirty document is valid
      boolean valid = cleaner.isValid(dirtyDocument);
      
      // The dirty document should be valid
      assertTrue(valid);
    }

    @Test
    public void testCopySafeNodes() {
      // Create a root element
      Element root = new Element("html");
      
      // Create a destination element
      Element destination = new Element("body");
      
      // Create a source element
      Element source = new Element("p");
      source.text("Hello, world!");
      
      // Add the source element to the root element
      root.appendChild(source);
      
      // Copy the safe nodes from the root element to the destination element
      int numDiscarded = cleaner.copySafeNodes(root, destination);
      
      // The destination element should contain the source element
      assertTrue(destination.toString().contains("<p>Hello, world!</p>"));
      
      // The number of discarded nodes should be 0
      assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
      // Create a source element
      Element sourceEl = new Element("p");
      sourceEl.attr("class", "test");
      
      // Create a safe element
      Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
      Element dest = meta.el;
      
      // The safe element should have the same tag name as the source element
      assertEquals("p", dest.tagName());
      
      // The safe element should have the same base URI as the source element
      assertEquals(sourceEl.baseUri(), dest.baseUri());
      
      // The safe element should have the same class attribute as the source element
      assertEquals(sourceEl.attr("class"), dest.attr("class"));
      
      // The number of discarded attributes should be 0
      assertEquals(0, meta.numAttribsDiscarded);
    }
}