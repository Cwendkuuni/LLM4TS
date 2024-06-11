import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case: input starts with hyphens
        String input1 = "--example";
        String expectedOutput1 = "example";
        assertEquals(expectedOutput1, Util.stripLeadingHyphens(input1));

        // Edge case: input starts with a single hyphen
        String input2 = "-example";
        String expectedOutput2 = "example";
        assertEquals(expectedOutput2, Util.stripLeadingHyphens(input2));

        // Error scenario: null input
        assertNull(Util.stripLeadingHyphens(null));

        // Error scenario: input with no hyphens
        String input3 = "example";
        assertEquals(input3, Util.stripLeadingHyphens(input3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case: input with leading and trailing quotes
        String input1 = "\"example\"";
        String expectedOutput1 = "example";
        assertEquals(expectedOutput1, Util.stripLeadingAndTrailingQuotes(input1));

        // Edge case: input with only leading quote
        String input2 = "\"example";
        String expectedOutput2 = "\"example";
        assertEquals(expectedOutput2, Util.stripLeadingAndTrailingQuotes(input2));

        // Edge case: input with only trailing quote
        String input3 = "example\"";
        String expectedOutput3 = "example\"";
        assertEquals(expectedOutput3, Util.stripLeadingAndTrailingQuotes(input3));

        // Error scenario: null input
        assertNull(Util.stripLeadingAndTrailingQuotes(null));

        // Error scenario: input with no quotes
        String input4 = "example";
        assertEquals(input4, Util.stripLeadingAndTrailingQuotes(input4));
    }
}