import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"test\"";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testNullString_stripLeadingHyphens()
    {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = NullPointerException.class)
    public void testNullString_stripLeadingAndTrailingQuotes()
    {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }

    @Test
    public void testEmptyString_stripLeadingHyphens()
    {
        String str = "";
        String expected = "";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyString_stripLeadingAndTrailingQuotes()
    {
        String str = "";
        String expected = "";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringWithMultipleLeadingHyphens_stripLeadingHyphens()
    {
        String str = "----test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringWithSingleLeadingHyphen_stripLeadingHyphens()
    {
        String str = "--test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringWithNoLeadingHyphens_stripLeadingHyphens()
    {
        String str = "test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringWithMultipleLeadingQuotes_stripLeadingAndTrailingQuotes()
    {
        String str = "\"\"test\"\"";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringWithSingleLeadingQuoteAndMultipleTrailingQuotes_stripLeadingAndTrailingQuotes()
    {
        String str = "\"test\"\"";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringWithMultipleLeadingQuotesAndSingleTrailingQuote_stripLeadingAndTrailingQuotes()
    {
        String str = "\"\"test\"";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStringWithNoLeadingNorTrailingQuotes_stripLeadingAndTrailingQuotes()
    {
        String str = "test";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }
}