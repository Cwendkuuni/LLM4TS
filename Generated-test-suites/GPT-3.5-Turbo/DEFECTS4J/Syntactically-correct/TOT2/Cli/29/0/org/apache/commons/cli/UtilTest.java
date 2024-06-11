import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("test1", Util.stripLeadingHyphens("--test1"));
        assertEquals("test2", Util.stripLeadingHyphens("-test2"));
        assertEquals("test3", Util.stripLeadingHyphens("test3"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("text", Util.stripLeadingAndTrailingQuotes("\"text"));
        assertEquals("text", Util.stripLeadingAndTrailingQuotes("text\""));
        assertEquals("text", Util.stripLeadingAndTrailingQuotes("text"));
    }
}