import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("test", Util.stripLeadingHyphens("-test"));
        assertEquals("example", Util.stripLeadingHyphens("--example"));
        assertEquals("noHyphens", Util.stripLeadingHyphens("noHyphens"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("example", Util.stripLeadingAndTrailingQuotes("\"example\""));
        assertEquals("another example", Util.stripLeadingAndTrailingQuotes("\"another example\""));
        assertEquals("noQuotes", Util.stripLeadingAndTrailingQuotes("noQuotes"));
    }
}