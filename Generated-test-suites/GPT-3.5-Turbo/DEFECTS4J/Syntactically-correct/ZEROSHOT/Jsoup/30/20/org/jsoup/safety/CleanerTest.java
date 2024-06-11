import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Hello</p><script>alert('XSS');</script></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertFalse(cleanDocument.getElementsByTag("script").size() > 0);
    }

    @Test
    public void testIsValid_true() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Hello</p></div></body></html>");

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_false() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><p onclick='alert('XSS')'>Hello</p></div></body></html>");

        assertFalse(cleaner.isValid(dirtyDocument));
    }
}