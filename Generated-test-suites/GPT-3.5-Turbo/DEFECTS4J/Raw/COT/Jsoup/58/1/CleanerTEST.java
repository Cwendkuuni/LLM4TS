
import org.jsoup.safety.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.jsoup.select.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Hello, World!</p></div></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><div><p>Hello, World!</p></div></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Hello, World!</p></div></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }
}
