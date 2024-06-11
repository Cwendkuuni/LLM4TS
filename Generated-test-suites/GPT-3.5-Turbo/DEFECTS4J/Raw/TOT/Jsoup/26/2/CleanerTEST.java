import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class CleanerTest {
    
    private Cleaner cleaner;
    
    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }
    
    @Test
    public void testClean() {
        String dirtyHTML = "<html><head><title>Dirty HTML</title></head><body><h1>Dirty</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Check if only body elements are present in the clean document
        assertEquals(2, cleanDocument.body().children().size());
        
        // Check if the dirty document is not modified
        assertEquals(2, dirtyDocument.body().children().size());
    }
    
    @Test
    public void testCleanWithEmptyDocument() {
        Document emptyDocument = Document.createShell("");
        
        Document cleanDocument = cleaner.clean(emptyDocument);
        
        // Check if the clean document is still empty
        assertTrue(cleanDocument.body().children().isEmpty());
    }
    
    @Test
    public void testIsValid() {
        String validHTML = "<html><body><h1>Valid HTML</h1></body></html>";
        Document validDocument = Jsoup.parse(validHTML);
        
        boolean isValid = cleaner.isValid(validDocument);
        
        // Check if the valid document is considered valid
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValidWithUnsafeTags() {
        String unsafeHTML = "<html><body><script>alert('Unsafe')</script></body></html>";
        Document unsafeDocument = Jsoup.parse(unsafeHTML);
        
        boolean isValid = cleaner.isValid(unsafeDocument);
        
        // Check if the unsafe document is considered invalid
        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        String html = "<html><body><div><span>Safe</span></div></body></html>";
        Document document = Jsoup.parse(html);
        
        Element source = document.body();
        Element dest = new Element(Tag.valueOf("div"), "");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        // Check if the safe nodes are successfully copied
        assertEquals(0, numDiscarded);
        assertEquals(1, dest.children().size());
    }
    
    @Test
    public void testCopySafeNodesWithUnsafeTags() {
        String html = "<html><body><div><script>alert('Unsafe')</script></div></body></html>";
        Document document = Jsoup.parse(html);
        
        Element source = document.body();
        Element dest = new Element(Tag.valueOf("div"), "");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        // Check if the unsafe tags are discarded
        assertEquals(1, numDiscarded);
        assertTrue(dest.children().isEmpty());
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("id", "safe");
        sourceEl.attr("style", "color:blue;");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        Element dest = elementMeta.el;
        int numDiscarded = elementMeta.numAttribsDiscarded;
        
        // Check if the safe element is created with correct attributes
        assertEquals("div", dest.tagName());
        assertEquals(1, dest.attributes().size());
        assertEquals("safe", dest.attr("id"));
        assertEquals(1, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElementWithUnsafeAttribute() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("onclick", "alert('Unsafe')");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        Element dest = elementMeta.el;
        int numDiscarded = elementMeta.numAttribsDiscarded;
        
        // Check if the unsafe attribute is discarded
        assertEquals(0, dest.attributes().size());
        assertEquals(1, numDiscarded);
    }
}