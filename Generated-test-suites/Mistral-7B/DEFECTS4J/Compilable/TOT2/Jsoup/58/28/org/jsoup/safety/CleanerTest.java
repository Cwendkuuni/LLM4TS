package org.jsoup.safety;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {
    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = new Whitelist(); // Dummy white list for testing
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }
}