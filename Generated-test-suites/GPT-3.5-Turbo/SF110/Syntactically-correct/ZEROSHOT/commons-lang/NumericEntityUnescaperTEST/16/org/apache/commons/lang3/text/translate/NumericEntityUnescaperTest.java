import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslateWithHexValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        StringBuilder expectedOutput = new StringBuilder();
        int expectedOffset = 4;

        // Test with hexadecimal value
        input.append("&#x41;");
        expectedOutput.append("A");

        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);

        // Test when semi-colon is missing
        input.setLength(0);
        expectedOutput.setLength(0);
        expectedOffset = 0;

        input.append("&#x42");
        expectedOutput.append("&#x42");

        output.setLength(0);
        result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);

        // Test when semi-colon is optional
        input.setLength(0);
        expectedOutput.setLength(0);
        expectedOffset = 5;

        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        input.append("&#x43");
        expectedOutput.append("C");

        output.setLength(0);
        result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);

        // Test when semi-colon is optional but not present
        input.setLength(0);
        expectedOutput.setLength(0);
        expectedOffset = 0;

        input.append("&#x44");
        expectedOutput.append("&#x44");

        output.setLength(0);
        result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);
    }

    @Test
    public void testTranslateWithDecimalValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        StringBuilder expectedOutput = new StringBuilder();
        int expectedOffset = 5;

        // Test with decimal value
        input.append("&#65;");
        expectedOutput.append("A");

        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);

        // Test when semi-colon is missing
        input.setLength(0);
        expectedOutput.setLength(0);
        expectedOffset = 0;

        input.append("&#66");
        expectedOutput.append("&#66");

        output.setLength(0);
        result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);

        // Test when semi-colon is optional but not present
        input.setLength(0);
        expectedOutput.setLength(0);
        expectedOffset = 0;

        input.append("&#67");
        expectedOutput.append("&#67");

        output.setLength(0);
        result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);
    }

    @Test
    public void testTranslateWithInvalidValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        StringBuilder expectedOutput = new StringBuilder();
        int expectedOffset = 0;

        // Test with invalid value
        input.append("&#X;");
        expectedOutput.append("&#X;");

        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);

        // Test with invalid hexadecimal value
        input.setLength(0);
        expectedOutput.setLength(0);
        expectedOffset = 0;

        input.append("&#xG;");
        expectedOutput.append("&#xG;");

        output.setLength(0);
        result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);
    }

    @Test
    public void testTranslateWithHighValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        StringBuilder expectedOutput = new StringBuilder();
        int expectedOffset = 8;

        // Test with value greater than 65535
        input.append("&#65536;");
        expectedOutput.append("\uD800\uDC00");

        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithSemiColonRequired() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder();

        // Test with missing semi-colon
        input.append("&#65");

        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, output);
    }

    @Test
    public void testTranslateWithSemiColonRequiredButNotPresent() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder();
        StringBuilder expectedOutput = new StringBuilder();
        int expectedOffset = 0;

        // Test with semi-colon required but not present
        input.append("&#65");

        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);
    }

    @Test
    public void testTranslateWithSemiColonOptionalButNotPresent() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder input = new StringBuilder();
        StringBuilder expectedOutput = new StringBuilder();
        int expectedOffset = 5;

        // Test with semi-colon optional and not present
        input.append("&#65");

        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);
    }

    @Test
    public void testTranslateWithRandomInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        StringBuilder expectedOutput = new StringBuilder();
        int expectedOffset = 0;

        // Test with random input not matching numeric entity format
        input.append("random");
        expectedOutput.append("random");

        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(expectedOffset, result);
    }
}