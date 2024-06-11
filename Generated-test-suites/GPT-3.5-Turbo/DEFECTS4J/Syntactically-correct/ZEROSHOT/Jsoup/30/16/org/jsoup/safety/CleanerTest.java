import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
   
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }
  
    @Test
    public void testClean() {
        Document dirtyDocument = Document.createShell("http://example.com");
  
        // Add some unsafe elements and attributes to the dirty document
        Element div = dirtyDocument.createElement("div");
        div.appendChild(dirtyDocument.createElement("script"));
        div.append("<p unsafeAttr='true'>Some text</p>");
        dirtyDocument.body().appendChild(div);
  
        Document cleanDocument = cleaner.clean(dirtyDocument);
  
        // Test if the clean document only contains the allowed elements and attributes
        assertFalse(cleanDocument.select("script").isEmpty());
        assertFalse(cleanDocument.select("div p[unsafeAttr]").isEmpty());
        assertTrue(cleanDocument.select("html").isEmpty());
    }
  
    @Test
    public void testIsValid() {
        Document dirtyDocument = Document.createShell("http://example.com");
  
        // Add some unsafe elements and attributes to the dirty document
        Element div = dirtyDocument.createElement("div");
        div.appendChild(dirtyDocument.createElement("script"));
        div.append("<p unsafeAttr='true'>Some text</p>");
        dirtyDocument.body().appendChild(div);
  
        assertFalse(cleaner.isValid(dirtyDocument));
  
        // Remove the unsafe elements and attributes from the dirty document
        cleanDocument = cleaner.clean(dirtyDocument);
  
        assertTrue(cleaner.isValid(cleanDocument));
    }
  
    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Document.createShell("http://example.com");
  
        // Add some unsafe elements and attributes to the dirty document
        Element div = dirtyDocument.createElement("div");
        div.appendChild(dirtyDocument.createElement("script"));
        div.append("<p unsafeAttr='true'>Some text</p>");
        dirtyDocument.body().appendChild(div);
  
        Document cleanDocument = Document.createShell("http://example.com");
        Element cleanBody = cleanDocument.body();
  
        cleaner.copySafeNodes(dirtyDocument.body(), cleanBody);
  
        // Test if the clean body only contains the allowed elements and attributes
        assertFalse(cleanBody.select("script").isEmpty());
        assertFalse(cleanBody.select("div p[unsafeAttr]").isEmpty());
        assertTrue(cleanBody.select("html").isEmpty());
    }
  
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "http://example.com", new Attributes());
        sourceEl.attr("unsafeAttr", "true");
  
        Cleaner.Whitelist whitelist = Cleaner.Whitelist.relaxed();
        Element destEl = cleaner.createSafeElement(sourceEl);
  
        // Test if the destEl only contains safe elements and attributes
        assertFalse(destEl.hasAttr("unsafeAttr"));
    }
}