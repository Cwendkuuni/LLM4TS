
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilTest {

    @Test
    void testStripLeadingHyphens() {
        // Typical use case
        String input1 = "--string";
        String expected1 = "string";
        String result1 = Util.stripLeadingHyphens(input1);
        Assertions.assertEquals(expected1, result1);

        // Edge case: no leading hyphens
        String input2 = "string";
        String expected2 = "string";
        String result2 = Util.stripLeadingHyphens(input2);
        Assertions.assertEquals(expected2, result2);

        // Edge case: single leading hyphen
        String input3 = "-string";
        String expected3 = "string";
        String result3 = Util.stripLeadingHyphens(input3);
        Assertions.assertEquals(expected3, result3);

        // Error scenario: null input
        String input4 = null;
        Assertions.assertThrows(NullPointerException.class, () -> Util.stripLeadingHyphens(input4));
    }

    @Test
    void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String input5 = "\"one two\"";
        String expected5 = "one two";
        String result5 = Util.stripLeadingAndTrailingQuotes(input5);
        Assertions.assertEquals(expected5, result5);

        // Edge case: no leading or trailing quotes
        String input6 = "one two";
        String expected6 = "one two";
        String result6 = Util.stripLeadingAndTrailingQuotes(input6);
        Assertions.assertEquals(expected6, result6);

        // Edge case: leading and trailing quotes are the same character
        String input7 = "'one two'";
        String expected7 = "one two";
        String result7 = Util.stripLeadingAndTrailingQuotes(input7);
        Assertions.assertEquals(expected7, result7);

        // Error scenario: null input
        String input8 = null;
        Assertions.assertThrows(NullPointerException.class, () -> Util.stripLeadingAndTrailingQuotes(input8));
    }
}
