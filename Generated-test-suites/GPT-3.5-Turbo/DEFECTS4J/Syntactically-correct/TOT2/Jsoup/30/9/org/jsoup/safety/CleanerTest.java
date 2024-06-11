import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.jsoup.parser.Tag;

import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    
    @Test
    public void testClean() {
        // Test basic clean functionality
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        body.appendElement("div").text("Hello, World!");
        dirtyDocument.appendChild(body);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Elements cleanBody = cleanDocument.body().children();
        
        assertEquals(1, cleanBody.size());
        assertEquals("div", cleanBody.get(0).tagName());
        assertEquals("Hello, World!", cleanBody.get(0).text());
    }
    
    @Test
    public void testIsValid() {
        // Test isValid method
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        body.appendElement("script").text("alert('XSS Attack')");
        dirtyDocument.appendChild(body);
        
        assertFalse(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        // Test copySafeNodes method
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        
        root.appendChild(new Element(Tag.valueOf("a"), "").text("Link"));
        root.appendChild(new TextNode("Hello"));
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertEquals(0, numDiscarded);
        Elements children = destination.children();
        assertEquals(2, children.size());
        assertEquals("a", children.get(0).tagName());
        assertEquals("Link", children.get(0).text());
        assertEquals("#text", children.get(1).nodeName());
        assertEquals("Hello", ((TextNode) children.get(1)).getWholeText());
    }
}