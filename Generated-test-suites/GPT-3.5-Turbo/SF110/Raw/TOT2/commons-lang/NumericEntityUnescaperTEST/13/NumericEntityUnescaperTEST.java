import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslateBasicCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, new StringWriter(sb));
        } catch (Exception e) {
            fail("Unexpected exception");
        }

        assertEquals("A", sb.toString());
    }

    @Test
    public void testTranslateHexadecimalCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        try {
            unescaper.translate("&#x41;", 0, new StringWriter(sb));
        } catch (Exception e) {
            fail("Unexpected exception");
        }

        assertEquals("A", sb.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMissingSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder sb = new StringBuilder();
        unescaper.translate("&#65", 0, new StringWriter(sb));
    }

    @Test
    public void testTranslateErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder sb = new StringBuilder();
        try {
            unescaper.translate("&#65", 0, new StringWriter(sb));
        } catch (Exception e) {
            fail("Unexpected exception");
        }

        assertEquals("", sb.toString());
    }

}