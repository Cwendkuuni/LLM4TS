import org.junit.*;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testIsSetWhenOptionIsNotSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test
    public void testTranslateWithValidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65;", 0, new Writer(output));
        assertEquals(5, result);
        assertEquals('A', output.charAt(0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithInvalidNumericEntityAndErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        unescaper.translate("&#65", 0, new Writer(output));
    }
    
    @Test
    public void testTranslateWithInvalidNumericEntityAndSemiColonNotRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#999999999;", 0, new Writer(output));
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslateWithHexadecimalEntityAndValueGreaterThan65535() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#x1FFFF;", 0, new Writer(output));
        assertEquals(8, result);
        assertEquals('\uD83F', output.charAt(0));
        assertEquals('\uDFFF', output.charAt(1));
    }
    
    @Test
    public void testTranslateWithNonHexadecimalEntityAndValueLessThan65535() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#169;", 0, new Writer(output));
        assertEquals(6, result);
        assertEquals('\u00A9', output.charAt(0));
    }
}