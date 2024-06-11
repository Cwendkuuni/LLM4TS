import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--test";
        String str2 = "-test";
        String str3 = "test";
        
        assertEquals("test", Util.stripLeadingHyphens(str1));
        assertEquals("test", Util.stripLeadingHyphens(str2));
        assertEquals("test", Util.stripLeadingHyphens(str3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"test\"";
        String str2 = "'test'";
        String str3 = "test";
        
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str1));
        assertEquals("'test'", Util.stripLeadingAndTrailingQuotes(str2)); // edge case with single quotes
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str3));
    }
}