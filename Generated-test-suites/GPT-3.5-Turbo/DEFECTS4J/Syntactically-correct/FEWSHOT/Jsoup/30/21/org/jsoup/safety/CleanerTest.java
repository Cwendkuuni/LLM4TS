
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        String html = "<div><p>This is a test</p></div>";
        Document dirtyDocument = Parser.parseBodyFragment(html);
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals(html, cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        String html = "<div><p>This is a test</p></div>";
        Document dirtyDocument = Parser.parseBodyFragment(html);
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        assertTrue(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testIsValid_InvalidDocument() {
        String html = "<script>alert('XSS');</script>";
        Document dirtyDocument = Parser.parseBodyFragment(html);
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        assertFalse(cleaner.isValid(dirtyDocument));
    }
}
