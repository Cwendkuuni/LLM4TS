import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphensWithDoubleHyphen() {
        String result = Util.stripLeadingHyphens("--example");
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingHyphensWithSingleHyphen() {
        String result = Util.stripLeadingHyphens("-example");
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingHyphensWithoutHyphen() {
        String result = Util.stripLeadingHyphens("example");
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"example\"");
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithoutQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("example");
        assertEquals("example", result);
    }
}