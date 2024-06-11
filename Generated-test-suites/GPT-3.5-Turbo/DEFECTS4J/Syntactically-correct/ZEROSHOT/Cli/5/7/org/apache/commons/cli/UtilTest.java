import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_MultipleHyphens_StartsWithHyphens() {
        String str = "--test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen_StartsWithHyphen() {
        String str = "-test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingHyphens_NoHyphen() {
        String str = "test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StartAndEndsWithQuotes() {
        String str = "\"test\"";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StartsWithQuote() {
        String str = "\"test";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EndsWithQuote() {
        String str = "test\"";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String str = "test";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }
}