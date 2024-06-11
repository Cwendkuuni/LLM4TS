import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--some-string";
        String expected = "some-string";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);

        str = "some-string";
        expected = "some-string";
        actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);

        str = "some-string";
        expected = "some-string";
        actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNullInput() {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"some-string\"";
        String expected = "some-string";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = "some-string";
        expected = "some-string";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = "\"some-string\"";
        expected = "some-string";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String str = "";
        Util.stripLeadingAndTrailingQuotes(str);
    }
}