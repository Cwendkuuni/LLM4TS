import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_EmptyString() {
        String result = Util.stripLeadingHyphens("");
        assertEquals("", result);
    }

    @Test
    public void testStripLeadingHyphens_HyphenPrefix() {
        String result = Util.stripLeadingHyphens("-test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphenPrefix() {
        String result = Util.stripLeadingHyphens("--test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EmptyString() {
        String result = Util.stripLeadingAndTrailingQuotes("");
        assertEquals("", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesOnly() {
        String result = Util.stripLeadingAndTrailingQuotes("\"\"");
        assertEquals("", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("test\"");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_BothQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", result);
    }
}