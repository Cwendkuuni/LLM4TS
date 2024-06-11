import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input = "--test-string";
        String expected = "test-string";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = "test-string";
        result = Util.stripLeadingHyphens(input);
        assertEquals(input, result);

        input = null;
        result = Util.stripLeadingHyphens(input);
        assertNull(result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"test-string\"";
        String expected = "test-string";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = "test-string";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(input, result);

        input = "";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(input, result);
    }
}