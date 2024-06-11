import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {
    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = new Whitelist(); // Assume Whitelist is initialized correctly
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }
}