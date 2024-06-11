
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        String str1 = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str1));

        // Edge case
        String str2 = "-";
        assertEquals("", Util.stripLeadingHyphens(str2));

        // Error scenario
        String str3 = "test";
        assertEquals("test", Util.stripLeadingHyphens(str3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String str1 = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str1));

        // Edge case
        String str2 = "\"";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str2));

        // Error scenario
        String str3 = "\"test";
        assertEquals("\"test", Util.stripLeadingAndTrailingQuotes(str3));
    }
}
