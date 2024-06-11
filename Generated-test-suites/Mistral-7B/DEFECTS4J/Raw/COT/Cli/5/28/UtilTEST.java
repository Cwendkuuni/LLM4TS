import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);

        str = "-test";
        expected = "test";
        actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);

        str = "test";
        expected = "test";
        actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNull() {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test\"";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = "test";
        expected = "test";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = "\"test\"";
        expected = "test";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesNull() {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }
}