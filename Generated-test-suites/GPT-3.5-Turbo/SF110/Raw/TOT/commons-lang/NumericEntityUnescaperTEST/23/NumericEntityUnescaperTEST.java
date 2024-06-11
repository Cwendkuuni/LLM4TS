
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidNumericEntity_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&amp;&#65;&#66;&#67;");
        StringBuilder expectedOutput = new StringBuilder("ABC");

        StringBuilder actualOutput = new StringBuilder();
        try {
            unescaper.translate(input, 0, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslate_ValidHexadecimalNumericEntity_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x4A;");
        StringBuilder expectedOutput = new StringBuilder("J");

        StringBuilder actualOutput = new StringBuilder();
        try {
            unescaper.translate(input, 0, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslate_SemiColonNotOptional_ThrowsException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder expectedOutput = new StringBuilder("");

        StringBuilder actualOutput = new StringBuilder();
        try {
            unescaper.translate(input, 0, actualOutput);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

}
