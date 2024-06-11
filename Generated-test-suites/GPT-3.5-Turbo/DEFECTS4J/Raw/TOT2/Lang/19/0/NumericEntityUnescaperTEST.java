import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidInput_IntegerDecode() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65;", 0, output);
        assertEquals(3, result);
        assertEquals('A', output.charAt(0));
    }

    @Test
    public void testTranslate_ValidInput_HexadecimalDecode() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#x41;", 0, output);
        assertEquals(4, result);
        assertEquals('A', output.charAt(0));
    }

    @Test
    public void testTranslate_InvalidInput_NonNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&amp;", 0, output);
        assertEquals(0, result);
        assertEquals(0, output.length());
    }

    @Test
    public void testTranslate_InvalidInput_InvalidNumberFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#invalid;", 0, output);
        assertEquals(0, result);
        assertEquals(0, output.length());
    }

    @Test
    public void testTranslate_InvalidInput_MissingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65", 0, output);
        assertEquals(3, result);
        assertEquals('A', output.charAt(0));
    }

    @Test
    public void testTranslate_InvalidInput_ValueExceedsFFFF() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65536;", 0, output);
        assertEquals(7, result);
        assertEquals(0, output.length());
    }

}