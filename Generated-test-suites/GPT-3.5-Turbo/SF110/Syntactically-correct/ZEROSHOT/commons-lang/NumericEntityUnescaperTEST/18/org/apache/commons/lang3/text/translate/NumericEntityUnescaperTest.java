import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;
import java.io.StringWriter;
import java.io.IOException;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_validDecimalEntity_shouldWriteCorrectCharToWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&#65;";
        int index = 0;

        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(4, result); // 2 (entity length) + 1 (semi-colon) + 1 (offset)
            assertEquals('A', writer.toString().charAt(0));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_validHexEntity_shouldWriteCorrectCharToWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&#x41;";
        int index = 0;

        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(5, result); // 3 (entity length) + 1 (semi-colon) + 1 (offset)
            assertEquals('A', writer.toString().charAt(0));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_validDecimalEntityWithoutSemiColon_shouldWriteCorrectCharToWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringWriter writer = new StringWriter();
        String input = "&#65";
        int index = 0;

        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(3, result); // 2 (entity length) + 1 (offset)
            assertEquals('A', writer.toString().charAt(0));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_invalidEntity_shouldReturnZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&invalid;";
        int index = 0;

        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(0, result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_invalidEntityWithOptionErrorIfNoSemiColon_shouldThrowException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();
        String input = "&invalid";
        int index = 0;

        unescaper.translate(input, index, writer);
    }

    @Test
    public void testIsSet_optionsNotSet_shouldReturnFalse() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        boolean result = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired);

        assertFalse(result);
    }

    @Test
    public void testIsSet_optionSet_shouldReturnTrue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        boolean result = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        assertTrue(result);
    }
}