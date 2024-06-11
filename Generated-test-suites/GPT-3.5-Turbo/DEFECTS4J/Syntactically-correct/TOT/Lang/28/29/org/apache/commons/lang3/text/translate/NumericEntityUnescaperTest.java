import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_typicalUseCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&amp;&#65;&#x41;&#1234;&#x500;");
        StringBuilder expectedOutput = new StringBuilder("&65Aሀ");
        StringBuilder actualOutput = new StringBuilder();

        try {
            unescaper.translate(input, 0, actualOutput);
            assertEquals(expectedOutput.toString(), actualOutput.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_edgeCase_singleDigitDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#6;");
        StringBuilder expectedOutput = new StringBuilder("");
        StringBuilder actualOutput = new StringBuilder();

        try {
            unescaper.translate(input, 0, actualOutput);
            assertEquals(expectedOutput.toString(), actualOutput.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_edgeCase_singleDigitHexadecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x2a;");
        StringBuilder expectedOutput = new StringBuilder("*");
        StringBuilder actualOutput = new StringBuilder();

        try {
            unescaper.translate(input, 0, actualOutput);
            assertEquals(expectedOutput.toString(), actualOutput.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_edgeCase_missingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder expectedOutput = new StringBuilder();
        StringBuilder actualOutput = new StringBuilder();

        try {
            int result = unescaper.translate(input, 0, actualOutput);
            assertEquals(expectedOutput.toString(), actualOutput.toString());
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_errorCase_invalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#ABC;");
        StringBuilder expectedOutput = new StringBuilder();
        StringBuilder actualOutput = new StringBuilder();

        try {
            int result = unescaper.translate(input, 0, actualOutput);
            assertEquals(expectedOutput.toString(), actualOutput.toString());
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}