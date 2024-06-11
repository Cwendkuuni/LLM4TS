package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        // Add more assertions here if needed
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
        // Add more assertions here if needed
    }

    // Add more test cases for each method and edge cases if needed

}