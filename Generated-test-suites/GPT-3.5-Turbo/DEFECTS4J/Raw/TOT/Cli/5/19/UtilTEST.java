
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        String input1 = "--test";
        String expected1 = "test";
        String output1 = Util.stripLeadingHyphens(input1);
        assertEquals(expected1, output1);

        // Edge case
        String input2 = "-";
        String expected2 = "";
        String output2 = Util.stripLeadingHyphens(input2);
        assertEquals(expected2, output2);

        // Error scenario
        String input3 = "";
        String expected3 = "";
        String output3 = Util.stripLeadingHyphens(input3);
        assertEquals(expected3, output3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String input1 = "\"test\"";
        String expected1 = "test";
        String output1 = Util.stripLeadingAndTrailingQuotes(input1);
        assertEquals(expected1, output1);

        // Edge case
        String input2 = "\"";
        String expected2 = "";
        String output2 = Util.stripLeadingAndTrailingQuotes(input2);
        assertEquals(expected2, output2);

        // Error scenario
        String input3 = "";
        String expected3 = "";
        String output3 = Util.stripLeadingAndTrailingQuotes(input3);
        assertEquals(expected3, output3);
    }
}
