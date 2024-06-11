import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for Util class.
 */
public class UtilTest
{

    /**
     * Test for stripLeadingHyphens method with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphens()
    {
        String str = "testString";
        String expected = "testString";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingHyphens method with leading double hyphens.
     */
    @Test
    public void testStripLeadingHyphensDoubleLeadingHyphens()
    {
        String str = "--testString";
        String expected = "testString";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingHyphens method with leading single hyphen.
     */
    @Test
    public void testStripLeadingHyphensSingleLeadingHyphen()
    {
        String str = "-testString";
        String expected = "testString";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingHyphens method with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphensNull()
    {
        String str = null;
        String expected = null;
        String result = Util.stripLeadingHyphens(str);
        assertNull(result);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with no leading or trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesNoLeadingOrTrailingQuotes()
    {
        String str = "testString";
        String expected = "testString";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with leading quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes()
    {
        String str = "\"testString\"";
        String expected = "testString";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes()
    {
        String str = "testString\"";
        String expected = "testString";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with leading and trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingAndTrailingQuotes()
    {
        String str = "\"testString\"";
        String expected = "testString";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    /**
     * Test for stripLeadingAndTrailingQuotes method with empty string.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString()
    {
        String str = "";
        String expected = "";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }
}