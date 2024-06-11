import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String strWithLeadingHyphens = "--option";
        String expected = "option";
        String actual = Util.stripLeadingHyphens(strWithLeadingHyphens);
        assertEquals(expected, actual);

        String strWithoutLeadingHyphens = "option";
        String actualWithoutLeadingHyphens = Util.stripLeadingHyphens(strWithoutLeadingHyphens);
        assertEquals(strWithoutLeadingHyphens, actualWithoutLeadingHyphens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullString() {
        String nullString = null;
        Util.stripLeadingHyphens(nullString);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String strWithLeadingAndTrailingQuotes = "\"one two\"";
        String expected = "one two";
        String actual = Util.stripLeadingAndTrailingQuotes(strWithLeadingAndTrailingQuotes);
        assertEquals(expected, actual);

        String strWithoutLeadingAndTrailingQuotes = "one two";
        String actualWithoutLeadingAndTrailingQuotes = Util.stripLeadingAndTrailingQuotes(strWithoutLeadingAndTrailingQuotes);
        assertEquals(strWithoutLeadingAndTrailingQuotes, actualWithoutLeadingAndTrailingQuotes);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String emptyString = "";
        Util.stripLeadingAndTrailingQuotes(emptyString);
    }
}