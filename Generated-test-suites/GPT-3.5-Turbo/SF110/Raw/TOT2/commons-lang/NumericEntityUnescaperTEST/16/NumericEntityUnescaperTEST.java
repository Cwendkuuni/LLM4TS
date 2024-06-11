import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test
    public void testTranslate_basic() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, new StringWriter(output));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("A", output.toString());
    }
    
    @Test
    public void testTranslate_edgeCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#xFFFF;", 0, new StringWriter(output));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("\uFFFF", output.toString());
    }
    
    @Test
    public void testTranslate_errorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65", 0, new StringWriter(output));
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        } catch (Exception e) {
            fail("Incorrect exception thrown");
        }
    }
}