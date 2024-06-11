
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
        dirtyDocument = new Document("");
    }
    
    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals(cleanDocument.baseUri(), dirtyDocument.baseUri());
        assertEquals(cleanDocument.body().childNodes().size(), dirtyDocument.body().childNodes().size());
    }
    
    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertEquals(isValid, true);
    }
    
    @Test
    public void testCopySafeNodes() {
        org.w3c.dom.Document w3cDocument = new W3CDom().fromJsoup(dirtyDocument);
        org.w3c.dom.Document expectedW3cDocument = new W3CDom().fromJsoup(dirtyDocument);
        
        cleaner.copySafeNodes(dirtyDocument.body(), dirtyDocument.body());
        
        assertEquals(new W3CDom().fromJsoup(dirtyDocument).toString(), expectedW3cDocument.toString());
    }
    
    @Test
    public void testCreateSafeElement() {
        Element element = dirtyDocument.appendElement("div");
        element.attr("id", "testId");
        
        ElementMeta meta = cleaner.createSafeElement(element);
        
        assertEquals(meta.el, element);
        assertEquals(meta.numAttribsDiscarded, 0);
    }
}
