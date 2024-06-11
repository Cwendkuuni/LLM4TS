import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&amp;", 0, new StringBuilderWriter(output));
            assertEquals(2, result);
            assertEquals("&", output.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testTranslateWithHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#x41;", 0, new StringBuilderWriter(output));
            assertEquals(4, result);
            assertEquals("A", output.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        unescaper.translate("&#65", 0, new StringBuilderWriter(output));
    }

    @Test
    public void testTranslateWithInvalidNumber() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#invalid;", 0, new StringBuilderWriter(output));
            assertEquals(0, result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

}