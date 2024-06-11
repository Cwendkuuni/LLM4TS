
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    
    private Cleaner cleaner;
    
    @Before
    public void setup() {
        Whitelist whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }
    
    @Test
    public void testClean() {
        // Test a basic input document with only allowed elements and attributes
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello, world!</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<body><p>Hello, world!</p></body>";
        assertEquals(expected, cleanDocument.toString());
        
        // Test an input document with disallowed elements and attributes
        dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS');</script>");
        cleanDocument = cleaner.clean(dirtyDocument);
        expected = "<body></body>";
        assertEquals(expected, cleanDocument.toString());
    }
    
    @Test
    public void testIsValid() {
        // Test a valid document
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello, world!</p>");
        assertTrue(cleaner.isValid(dirtyDocument));
        
        // Test an invalid document
        dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS');</script>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        // Test copying safe elements and attributes
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        Element source = new Element(Tag.valueOf("p"), "");
        Attributes attributes = new Attributes();
        attributes.put("style", "color: red;");
        source.attributes().addAll(attributes);
        source.text("Hello, world!");
        root.appendChild(source);
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        String expected = "<div><p style=\"color: red;\">Hello, world!</p></div>";
        assertEquals(expected, destination.toString());
        assertEquals(0, numDiscarded);
        
        // Test copying disallowed elements and attributes
        root = new Element(Tag.valueOf("div"), "");
        destination = new Element(Tag.valueOf("div"), "");
        source = new Element(Tag.valueOf("script"), "");
        source.text("alert('XSS');");
        root.appendChild(source);
        
        numDiscarded = cleaner.copySafeNodes(root, destination);
        expected = "<div></div>";
        assertEquals(expected, destination.toString());
        assertEquals(1, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        Attributes attributes = new Attributes();
        attributes.put("style", "color: red;");
        sourceEl.attributes().addAll(attributes);
        
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;
        String expected = "<p style=\"color: red;\"></p>";
        assertEquals(expected, dest.toString());
        assertEquals(0, meta.numAttribsDiscarded);
    }
}

