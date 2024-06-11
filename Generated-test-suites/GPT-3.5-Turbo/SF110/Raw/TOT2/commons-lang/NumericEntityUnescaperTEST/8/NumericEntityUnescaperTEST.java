import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test
    public void testTranslateWithValidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#65;", 0, new java.io.StringWriter(output));
            assertEquals(5, output.length());
            assertEquals(5, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testTranslateWithInvalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&abc;", 0, new java.io.StringWriter(output));
            assertEquals(0, output.length());
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithMissingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        unescaper.translate("&#65", 0, new java.io.StringWriter(output));
    }
    
    @Test
    public void testTranslateWithInvalidHexInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#0x;", 0, new java.io.StringWriter(output));
            assertEquals(0, output.length());
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
}