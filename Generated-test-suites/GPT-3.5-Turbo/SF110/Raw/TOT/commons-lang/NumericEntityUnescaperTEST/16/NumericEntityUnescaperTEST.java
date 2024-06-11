import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_RegularDecimalNumericEntity_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();

        try {
            int numCharsTranslated = unescaper.translate(input, 0, new StringBuilderWriter(output));
            assertEquals(1, numCharsTranslated);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_RegularHexadecimalNumericEntity_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x41;");
        StringBuilder output = new StringBuilder();

        try {
            int numCharsTranslated = unescaper.translate(input, 0, new StringBuilderWriter(output));
            assertEquals(1, numCharsTranslated);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_RegularDecimalNumericEntityWithSemiColon_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65;;");
        StringBuilder output = new StringBuilder();

        try {
            int numCharsTranslated = unescaper.translate(input, 0, new StringBuilderWriter(output));
            assertEquals(3, numCharsTranslated);
            assertEquals("A;", output.toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_RegularHexadecimalNumericEntityWithSemiColon_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x41;;");
        StringBuilder output = new StringBuilder();

        try {
            int numCharsTranslated = unescaper.translate(input, 0, new StringBuilderWriter(output));
            assertEquals(3, numCharsTranslated);
            assertEquals("A;", output.toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_InvalidNumericEntity_ReturnsZeroCharsTranslated() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#ZZZ;");
        StringBuilder output = new StringBuilder();

        try {
            int numCharsTranslated = unescaper.translate(input, 0, new StringBuilderWriter(output));
            assertEquals(0, numCharsTranslated);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}