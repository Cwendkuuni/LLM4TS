import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("hello", Util.stripLeadingAndTrailingQuotes("\"hello\""));
        assertEquals("hello", Util.stripLeadingAndTrailingQuotes("\"hello"));
        assertEquals("hello", Util.stripLeadingAndTrailingQuotes("hello\""));
        assertEquals("hello", Util.stripLeadingAndTrailingQuotes("hello"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

}