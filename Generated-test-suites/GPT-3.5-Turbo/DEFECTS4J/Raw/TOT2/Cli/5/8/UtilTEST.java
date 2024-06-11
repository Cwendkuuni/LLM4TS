import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("bcd", Util.stripLeadingHyphens("-bcd"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("three four", Util.stripLeadingAndTrailingQuotes("\"three four\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_Empty() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_MultipleQuotes() {
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("\"\"abc\"\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("abc"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_SingleQuotes() {
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("'abc'"));
    }
}