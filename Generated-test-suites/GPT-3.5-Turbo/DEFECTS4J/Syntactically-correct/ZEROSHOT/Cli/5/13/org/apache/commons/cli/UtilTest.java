import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--test";
        String expectedOutput1 = "test";
        assertEquals(expectedOutput1, Util.stripLeadingHyphens(input1));

        String input2 = "-test";
        String expectedOutput2 = "test";
        assertEquals(expectedOutput2, Util.stripLeadingHyphens(input2));

        String input3 = "test";
        String expectedOutput3 = "test";
        assertEquals(expectedOutput3, Util.stripLeadingHyphens(input3));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"test\"";
        String expectedOutput1 = "test";
        assertEquals(expectedOutput1, Util.stripLeadingAndTrailingQuotes(input1));
        
        String input2 = "test";
        String expectedOutput2 = "test";
        assertEquals(expectedOutput2, Util.stripLeadingAndTrailingQuotes(input2));
        
        String input3 = "\"test";
        String expectedOutput3 = "\"test";
        assertEquals(expectedOutput3, Util.stripLeadingAndTrailingQuotes(input3));
        
        String input4 = "test\"";
        String expectedOutput4 = "test\"";
        assertEquals(expectedOutput4, Util.stripLeadingAndTrailingQuotes(input4));
    }
}