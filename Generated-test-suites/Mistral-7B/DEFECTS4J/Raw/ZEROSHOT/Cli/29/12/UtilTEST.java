import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Util methods.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        assertEquals("", Util.stripLeadingHyphens(null));
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("--str", Util.stripLeadingHyphens("--str"));
        assertEquals("str", Util.stripLeadingHyphens("--str--"));
        assertEquals("str", Util.stripLeadingHyphens("--str-"));
        assertEquals("str", Util.stripLeadingHyphens("-str"));
        assertEquals("str", Util.stripLeadingHyphens("str"));
        assertEquals("str1 str2", Util.stripLeadingHyphens("--str1--str2--"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(null));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("\"one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two three four five", Util.stripLeadingAndTrailingQuotes("\"one two\" \"three four five\""));
    }
}