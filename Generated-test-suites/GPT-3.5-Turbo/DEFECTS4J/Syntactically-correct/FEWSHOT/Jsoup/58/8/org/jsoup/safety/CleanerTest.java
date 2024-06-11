package org.jsoup.safety;

import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean() {
        // Test case 1: No unsafe tags or attributes
        Document dirtyDocument1 = Jsoup.parse("<body><div>Test</div></body>");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        Document cleanDocument1 = cleaner1.clean(dirtyDocument1);
        assertTrue(cleanDocument1.body().hasSameValue("<div>Test</div>"));
        
        // Test case 2: Unsafe tag and attribute
        Document dirtyDocument2 = Jsoup.parse("<body><script>alert('XSS')</script></body>");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        Document cleanDocument2 = cleaner2.clean(dirtyDocument2);
        assertTrue(cleanDocument2.body().hasSameValue(""));
    }

    @Test
    public void testIsValid() {
        // Test case 1: No unsafe tags or attributes
        Document dirtyDocument1 = Jsoup.parse("<body><div>Test</div></body>");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        assertTrue(cleaner1.isValid(dirtyDocument1));
        
        // Test case 2: Unsafe tag and attribute
        Document dirtyDocument2 = Jsoup.parse("<body><script>alert('XSS')</script></body>");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        assertFalse(cleaner2.isValid(dirtyDocument2));
    }
}