
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private static final String DIRTY_HTML = "<html><head></head><body><div>This is a test</div></body></html>";
    private static final String BASE_URI = "http://www.example.com";

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse(DIRTY_HTML, BASE_URI);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse(DIRTY_HTML, BASE_URI);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }
}
