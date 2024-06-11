import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidInput_shouldReturnExpectedOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);
        assertEquals(4, result); // Expected output: A
    }

    @Test
    public void testTranslate_withInvalidInput_shouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#invalid;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withSemiColonRequiredOptionAndMissingSemiColon_shouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        int index = 0;

        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withErrorIfNoSemiColonOptionAndMissingSemiColon_shouldThrowException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        int index = 0;

        unescaper.translate(input, index, writer);
    }

    @Test
    public void testTranslate_withHexadecimalInput_shouldReturnExpectedOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#x41;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);
        assertEquals(6, result); // Expected output: A
    }

    @Test
    public void testTranslate_withDecimalInput_shouldReturnExpectedOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);
        assertEquals(4, result); // Expected output: A
    }

    @Test
    public void testIsSet_withOptionSet_shouldReturnTrue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        boolean result = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(result);
    }

    @Test
    public void testIsSet_withOptionNotSet_shouldReturnFalse() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        boolean result = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertFalse(result);
    }
}