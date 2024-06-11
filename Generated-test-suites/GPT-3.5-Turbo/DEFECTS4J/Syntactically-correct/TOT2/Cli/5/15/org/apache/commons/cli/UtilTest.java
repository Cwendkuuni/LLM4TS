import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("def", Util.stripLeadingHyphens("-def"));
        assertEquals("ghi", Util.stripLeadingHyphens("ghi"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("\"abc"));
        assertEquals("def", Util.stripLeadingAndTrailingQuotes("def\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EmptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyQuote() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyQuotes() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

}