import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        // Test if isSet returns correct value for an option that is set
        NumericEntityUnescaper.OPTION option = NumericEntityUnescaper.OPTION.semiColonRequired;
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(option);
        assertTrue(unescaper.isSet(option));

        // Test if isSet returns correct value for an option that is not set
        NumericEntityUnescaper.OPTION option2 = NumericEntityUnescaper.OPTION.semiColonOptional;
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper();
        assertFalse(unescaper2.isSet(option2));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test basic translation with decimal encoding
        String input1 = "&#65;";
        StringWriter writer1 = new StringWriter();
        int result1 = unescaper.translate(input1, 0, writer1);
        assertEquals(4, result1);
        assertEquals("A", writer1.toString());

        // Test basic translation with hexadecimal encoding
        String input2 = "&#x41;";
        StringWriter writer2 = new StringWriter();
        int result2 = unescaper.translate(input2, 0, writer2);
        assertEquals(5, result2);
        assertEquals("A", writer2.toString());

        // Test translation when semi-colon is not required
        String input3 = "&#65";
        StringWriter writer3 = new StringWriter();
        int result3 = unescaper.translate(input3, 0, writer3);
        assertEquals(3, result3);
        assertEquals("A", writer3.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateErrorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        // Test translation when semi-colon is missing
        String input = "&#65";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
    }

    // Add more test cases for edge cases and exception handling scenarios here

}