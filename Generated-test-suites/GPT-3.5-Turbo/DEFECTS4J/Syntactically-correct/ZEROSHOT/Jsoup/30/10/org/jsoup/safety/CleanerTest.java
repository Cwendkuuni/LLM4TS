import org.jsoup.helper.Validate;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {
    
    private Cleaner cleaner;
    
    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }
    
    @Test
    public void testClean() {
        // test case 1: dirty document with body
        String dirtyHtml = "<html><head></head><body><p>This is a paragraph.</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertTrue(cleanDocument.body().hasText());
        assertEquals("<p>This is a paragraph.</p>", cleanDocument.body().html());
        
        // test case 2: dirty document without body
        dirtyHtml = "<html><head></head></html>";
        dirtyDocument = Jsoup.parse(dirtyHtml);
        
        cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertTrue(cleanDocument.body().hasText());
        assertEquals("", cleanDocument.body().html());
    }
    
    @Test
    public void testIsValid() {
        // test case 1: valid document
        String dirtyHtml = "<html><head></head><body><p>This is a paragraph.</p></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
        
        // test case 2: invalid document
        dirtyHtml = "<html><head></head><body onload='alert(1)'><p>This is a paragraph.</p></body></html>";
        dirtyDocument = Jsoup.parse(dirtyHtml);
        
        isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("html"), "");
        
        Element div = new Element(Tag.valueOf("div"), "");
        div.attr("id", "mydiv");
        div.text("This is a div element");
        
        Element p = new Element(Tag.valueOf("p"), "");
        p.text("This is a p element");
        
        Element script = new Element(Tag.valueOf("script"), "");
        script.attr("src", "https://malicioussite.com/malwarescript.js");
        
        root.appendChild(div);
        root.appendChild(p);
        root.appendChild(script);
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertEquals(1, numDiscarded);
        assertEquals(2, destination.children().size());
        assertEquals("<div id=\"mydiv\">This is a div element</div><p>This is a p element</p>", destination.html());
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("id", "mydiv");
        sourceEl.attr("class", "myclass");
        
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        
        assertNotNull(meta.el);
        assertEquals("<div id=\"mydiv\" class=\"myclass\"></div>", meta.el.outerHtml());
        assertEquals(0, meta.numAttribsDiscarded);
        
        sourceEl = new Element(Tag.valueOf("script"), "");
        sourceEl.attr("src", "https://malicioussite.com/malwarescript.js");
        
        meta = cleaner.createSafeElement(sourceEl);
        
        assertNotNull(meta.el);
        assertEquals("<script></script>", meta.el.outerHtml());
        assertEquals(1, meta.numAttribsDiscarded);
    }
}