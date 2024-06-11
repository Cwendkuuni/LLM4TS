import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        // Set up a test instance of Cleaner
        cleaner = new Cleaner(Whitelist.basic());
        // Set up a test dirty Document
        dirtyDocument = Document.createShell("");
    }

    @Test
    public void testClean() {
        // Add dirty HTML elements to the dirty Document
        dirtyDocument.body().append("<h1>Hello, world!</h1>");
        dirtyDocument.body().append("<script>alert('XSS');</script>");
        dirtyDocument.body().append("<img src=\"https://example.com/image.jpg\" onerror=\"alert('XSS')\">");
        
        // Clean the dirty Document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Validate that only allowed elements are present in the clean Document
        assertEquals(1, cleanDocument.getElementsByTag("h1").size());
        assertEquals(0, cleanDocument.getElementsByTag("script").size());
        assertEquals(1, cleanDocument.getElementsByTag("img").size());
        assertEquals("https://example.com/image.jpg", cleanDocument.getElementsByTag("img").first().attr("src"));
    }

    @Test
    public void testIsValid() {
        // Add dirty HTML elements to the dirty Document
        dirtyDocument.body().append("<h1>Hello, world!</h1>");
        dirtyDocument.body().append("<div style=\"width: 1000px;\"></div>");
        
        // Test the validity of the dirty Document
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Validate that the dirty Document is not valid
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a source Element for testing
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element(Tag.valueOf("div"), "");
        // Add unsafe child Elements to the source Element
        source.appendChild(new org.jsoup.nodes.Element(Tag.valueOf("script"), ""));
        source.appendChild(new org.jsoup.nodes.Element(Tag.valueOf("img"), "").attr("src", "https://example.com/image.jpg"));
        // Add a safe child Element to the source Element
        source.appendChild(new org.jsoup.nodes.Element(Tag.valueOf("p"), ""));
        
        // Create a destination Element for testing
        org.jsoup.nodes.Element dest = new org.jsoup.nodes.Element(Tag.valueOf("div"), "");
        
        // Test the copySafeNodes method
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        // Validate that only the safe child Element is copied to the destination
        assertEquals(2, dest.childrenSize());
        assertEquals(1, dest.getElementsByTag("p").size());
        assertEquals(0, dest.getElementsByTag("script").size());
        assertEquals("https://example.com/image.jpg", dest.getElementsByTag("img").first().attr("src"));
        // Validate that the number of discarded elements is correct
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a source Element for testing
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element(Tag.valueOf("div"), "");
        // Add unsafe attributes to the source Element
        source.attr("onclick", "alert('XSS')");
        source.attr("style", "width: 1000px;");
        
        // Test the createSafeElement method
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);
        
        // Validate that the safe Element is created correctly
        assertNotNull(elementMeta.el);
        assertEquals(1, elementMeta.el.attributes().size());
        assertEquals("width: 1000px;", elementMeta.el.attr("style"));
        // Validate that the number of discarded attributes is correct
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}