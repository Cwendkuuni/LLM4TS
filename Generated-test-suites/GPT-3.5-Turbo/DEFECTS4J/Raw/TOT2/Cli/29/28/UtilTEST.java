import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--test";
        String input2 = "-test";
        String input3 = "test";
        
        assertEquals("test", Util.stripLeadingHyphens(input1));
        assertEquals("test", Util.stripLeadingHyphens(input2));
        assertEquals("test", Util.stripLeadingHyphens(input3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"test\"";
        String input2 = "test";
        
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(input1));
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(input2));
    }
}