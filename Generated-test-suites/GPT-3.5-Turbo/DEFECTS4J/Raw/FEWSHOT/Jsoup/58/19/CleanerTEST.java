
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
        Document dirtyDocument = Jsoup.parse("<html><head><title>Dirty Document</title></head><body><h1>Welcome</h1><p>This is a dirty document.</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("<html><head></head><body><h1>Welcome</h1><p>This is a dirty document.</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Document validDocument = Jsoup.parse("<html><head></head><body><h1>Welcome</h1><p>This is a valid document.</p></body></html>");
        Document invalidDocument = Jsoup.parse("<html><head><script>alert('XSS');</script></head><body><h1>Welcome</h1><p>This is an invalid document.</p></body></html>");
        
        assertEquals(true, cleaner.isValid(validDocument));
        assertEquals(false, cleaner.isValid(invalidDocument));
    }
}
