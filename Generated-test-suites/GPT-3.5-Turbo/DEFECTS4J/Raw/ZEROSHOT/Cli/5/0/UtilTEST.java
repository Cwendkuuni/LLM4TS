import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilTest {

    private Util util;

    @Before
    public void setUp() {
        util = new Util();
    }

    @Test
    public void testStripLeadingHyphens() {
        String result = util.stripLeadingHyphens("--test");
        assertEquals("test", result);

        result = util.stripLeadingHyphens("-test");
        assertEquals("test", result);

        result = util.stripLeadingHyphens("test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String result = util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", result);

        result = util.stripLeadingAndTrailingQuotes("\"test");
        assertEquals("\"test", result);

        result = util.stripLeadingAndTrailingQuotes("test\"");
        assertEquals("test\"", result);

        result = util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", result);
    }
}