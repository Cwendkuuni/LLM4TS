import org.junit.Test;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {
    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new BasicWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }
}