import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&amp;", 0, new StringWriter(output));
        assertEquals(2, result);
        assertEquals("&", output.toString());
    }

    @Test
    public void testTranslateWithHexValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#x41;", 0, new StringWriter(output));
        assertEquals(5, result);
        assertEquals("A", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        unescaper.translate("&#65", 0, new StringWriter());
    }

    @Test
    public void testTranslateInvalidHexValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#xG;", 0, new StringWriter(output));
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

}