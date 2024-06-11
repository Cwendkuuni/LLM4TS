import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_basicHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#x41;", 0, new StringWriter(output));
            assertEquals(6, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslate_decimalOutOfBounds() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#100000;", 0, new StringWriter(output));
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testIsSet_optionPresent() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
    }

}