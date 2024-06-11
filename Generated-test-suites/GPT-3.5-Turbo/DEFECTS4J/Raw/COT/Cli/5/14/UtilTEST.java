import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input1 = "--testString";
        String input2 = "-testString";
        String input3 = "testString";

        String result1 = Util.stripLeadingHyphens(input1);
        String result2 = Util.stripLeadingHyphens(input2);
        String result3 = Util.stripLeadingHyphens(input3);

        assertEquals("testString", result1);
        assertEquals("testString", result2);
        assertEquals("testString", result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input1 = "\"testString\"";
        String input2 = "\"testString";
        String input3 = "testString\"";
        String input4 = "testString";

        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);
        String result3 = Util.stripLeadingAndTrailingQuotes(input3);
        String result4 = Util.stripLeadingAndTrailingQuotes(input4);

        assertEquals("testString", result1);
        assertEquals("\"testString", result2);
        assertEquals("testString\"", result3);
        assertEquals("testString", result4);
    }

}