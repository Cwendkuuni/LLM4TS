import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_validInputWithDecimalBase_returnsTranslatedCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("A", result.toString());
    }

    @Test
    public void testTranslate_validInputWithHexadecimalBase_returnsTranslatedCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        try {
            unescaper.translate("&#x3C;", 0, result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("<", result.toString());
    }

    @Test
    public void testTranslate_validInputWithMissingSemicolon_returnsTranslatedCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        try {
            unescaper.translate("&#65", 0, result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("A", result.toString());
    }

    @Test
    public void testTranslate_invalidInputWithNonNumericCharacter_throwsNumberFormatException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        try {
            unescaper.translate("&#invalid;", 0, result);
            fail("NumberFormatException should be thrown");
        } catch (NumberFormatException e) {
            assertEquals("FAIL: invalid[2][9]", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception type thrown");
        }
    }
    
    @Test
    public void testTranslate_invalidInputWithMissingBaseCharacter_throwsNumberFormatException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        try {
            unescaper.translate("&#;", 0, result);
            fail("NumberFormatException should be thrown");
        } catch (NumberFormatException e) {
            assertEquals("FAIL: [2][2]", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception type thrown");
        }
    }

    @Test
    public void testTranslate_invalidInputWithMissingNumericCharacter_throwsNumberFormatException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        try {
            unescaper.translate("&#;", 0, result);
            fail("NumberFormatException should be thrown");
        } catch (NumberFormatException e) {
            assertEquals("FAIL: [2][2]", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception type thrown");
        }
    }

    @Test
    public void testTranslate_emptyInput_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        try {
            int translatedLength = unescaper.translate("", 0, result);
            assertEquals(0, translatedLength);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testTranslate_invalidInputWithIncorrectIndex_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        try {
            int translatedLength = unescaper.translate("&#65;", -1, result);
            assertEquals(0, translatedLength);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

}