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

        str = "";
        expected = "";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test1 test2\"";
        String expected = "test1 test2";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "test1 test2";
        expected = "test1 test2";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullString() {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingHyphensWithShortString() {
        String str = "--";
        Util.stripLeadingHyphens(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String str = "";
        String expected = "";
        String result = Util.stripLeadingAndTrailingQuotes(str);
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