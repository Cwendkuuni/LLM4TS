import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertNotNull(unescaper);
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
    }

    @Test
    public void testTranslateEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        try {
            unescaper.translate("", 0, new StringWriter(result));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("", result.toString());
    }

    @Test
    public void testTranslateValidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder result = new StringBuilder();
        try {
            unescaper.translate("&#x41;", 0, new StringWriter(result));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("A", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        unescaper.translate("&#xgg;", 0, new StringWriter());
    }
}