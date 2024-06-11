package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;

public class DocumentTest {
    
    private Document doc;
    
    @Before
    public void setUp() {
        doc = Document.createShell("http://example.com");
    }
    
    @Test
    public void testHead() {
        assertNotNull(doc.head());
    }

    @Test
    public void testBody() {
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", doc.title());
        
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleNull() {
        try {
            doc.title(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testElementCreation() {
        assertNotNull(doc.createElement("a"));
    }

    @Test
    public void testNormalise() {
        doc.normalise();
        // assert normalisation logic here
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", doc.outerHtml());
        // Test outerHtml logic here
    }

    @Test
    public void testTextNode() {
        doc.text("Test text");
        //assert text node logic here
    }
    
}