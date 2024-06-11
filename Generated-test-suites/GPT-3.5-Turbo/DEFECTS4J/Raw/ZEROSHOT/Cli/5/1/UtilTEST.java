import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    private Util util;

    @Before
    public void setUp() {
        util = new Util();
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphens() {
        String str = "--test";
        String result = util.stripLeadingHyphens(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        String str = "-test";
        String result = util.stripLeadingHyphens(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_NoHyphen() {
        String str = "test";
        String result = util.stripLeadingHyphens(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingQuotes() {
        String str = "\"test";
        String result = util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingQuotes() {
        String str = "test\"";
        String result = util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_BothQuotes() {
        String str = "\"test\"";
        String result = util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String str = "test";
        String result = util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }
}