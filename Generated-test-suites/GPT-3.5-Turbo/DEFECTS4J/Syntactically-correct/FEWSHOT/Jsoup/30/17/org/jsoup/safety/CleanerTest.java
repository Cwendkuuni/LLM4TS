
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }
    
    @Test
    public void testClean() {
        String dirtyHtml = "<html><head></head><body><div><p>Valid content</p><script>Malicious script</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertTrue(cleanDocument.body().html().contains("Valid content"));
        assertFalse(cleanDocument.body().html().contains("Malicious script"));
    }
    
    @Test
    public void testIsValid() {
        String html = "<html><head></head><body><div><p>Valid content</p><script>Malicious script</script></div></body></html>";
        Document document = Jsoup.parse(html);
        
        boolean isValid = cleaner.isValid(document);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        String html = "<html><head></head><body><div><p>Valid content</p><script>Malicious script</script></div></body></html>";
        Document document = Jsoup.parse(html);
        Element root = document.body();
        Element destination = new Element("div");
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertEquals(1, numDiscarded);
        assertEquals("<p>Valid content</p>", destination.html());
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element("div");
        sourceEl.attr("class", "safe");
        sourceEl.attr("onclick", "alert('malicious code')");
        
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        
        assertEquals("div", meta.el.tagName());
        assertEquals(1, meta.numAttribsDiscarded);
        assertEquals(1, meta.el.attributes().size());
        assertTrue(meta.el.attributes().hasKey("class"));
        assertFalse(meta.el.attributes().hasKey("onclick"));
    }
}
