import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int translation = unescaper.translate("&#65;", 0, new StringWriter(output));
            assertEquals(6, translation);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testTranslateWithWrongSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int translation = unescaper.translate("&#65", 0, new StringWriter(output));
            assertEquals(0, translation);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testTranslateWithInvalidNumberFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int translation = unescaper.translate("&#invalid;", 0, new StringWriter(output));
            assertEquals(0, translation);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColonOption() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        unescaper.translate("&#65", 0, new StringWriter(output));
    }
    
}