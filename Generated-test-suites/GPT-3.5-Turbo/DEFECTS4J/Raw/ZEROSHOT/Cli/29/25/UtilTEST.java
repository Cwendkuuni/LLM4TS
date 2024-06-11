import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullString() {
        String str = null;
        String result = Util.stripLeadingHyphens(str);
        assertNull(result);
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphens() {
        String str = "--example";
        String expected = "example";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        String str = "-example";
        String expected = "example";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphens_NoHyphen() {
        String str = "example";
        String expected = "example";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingAndTrailingQuotes() {
        String str = "\"example\"";
        String expected = "example";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingQuotes() {
        String str = "\"example";
        String expected = "example";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingQuotes() {
        String str = "example\"";
        String expected = "example";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String str = "example";
        String expected = "example";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }
}