import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("string", Util.stripLeadingHyphens("--string"));
        assertEquals("string", Util.stripLeadingHyphens("-string"));
        assertEquals("string", Util.stripLeadingHyphens("string"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
    }
}