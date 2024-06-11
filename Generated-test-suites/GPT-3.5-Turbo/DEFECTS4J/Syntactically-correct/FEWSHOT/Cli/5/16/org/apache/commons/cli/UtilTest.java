import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test case for str starting with "--"
        String result1 = Util.stripLeadingHyphens("--foo");
        assertEquals("foo", result1);

        // Test case for str starting with "-"
        String result2 = Util.stripLeadingHyphens("-foo");
        assertEquals("foo", result2);

        // Test case for str not starting with hyphens
        String result3 = Util.stripLeadingHyphens("foo");
        assertEquals("foo", result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case for str starting and ending with quotes
        String result1 = Util.stripLeadingAndTrailingQuotes("\"foo\"");
        assertEquals("foo", result1);

        // Test case for str starting and ending with quotes with spaces inside
        String result2 = Util.stripLeadingAndTrailingQuotes("\" foo \"");
        assertEquals(" foo ", result2);

        // Test case for str starting with quote and ending without quote
        String result3 = Util.stripLeadingAndTrailingQuotes("\"foo");
        assertEquals("foo", result3);

        // Test case for str starting without quote and ending with quote
        String result4 = Util.stripLeadingAndTrailingQuotes("foo\"");
        assertEquals("foo", result4);

        // Test case for empty string
        String result5 = Util.stripLeadingAndTrailingQuotes("");
        assertEquals("", result5);
    }
}