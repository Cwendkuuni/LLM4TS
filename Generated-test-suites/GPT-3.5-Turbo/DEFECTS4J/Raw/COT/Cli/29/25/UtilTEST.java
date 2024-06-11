import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str));

        str = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str));

        str = "test";
        assertEquals("test", Util.stripLeadingHyphens(str));

        str = null;
        assertNull(Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));

        str = "\"test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));

        str = "test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));

        str = "test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
}