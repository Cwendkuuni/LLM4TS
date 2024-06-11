import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "test";
        expected = "test";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "--";
        expected = "";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullInput() {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "test";
        expected = "test";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"test\"";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
        String str = "test";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }
}