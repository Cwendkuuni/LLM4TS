import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--test";
        String input2 = "-test";
        String input3 = "test";
        
        String expected = "test";
        
        assertEquals(expected, Util.stripLeadingHyphens(input1));
        assertEquals(expected, Util.stripLeadingHyphens(input2));
        assertEquals(input3, Util.stripLeadingHyphens(input3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"test\"";
        String input2 = "test";
        
        String expected = "test";
        
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input1));
        assertEquals(input2, Util.stripLeadingAndTrailingQuotes(input2));
    }
}