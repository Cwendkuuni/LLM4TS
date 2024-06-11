
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        // Test basic functionality
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#65;", 0, output);
            assertEquals("Result should be 6", 6, result);
            assertEquals("Output should be 'A'", "A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        // Test invalid numeric entity without semi-colon
        output = new StringBuilder();
        try {
            int result = unescaper.translate("&#65", 0, output);
            assertEquals("Result should be 0", 0, result);
            assertEquals("Output should be empty", "", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test invalid numeric entity with semi-colon
        output = new StringBuilder();
        try {
            NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
            
            int result = unescaper2.translate("&#65;", 0, output);
            fail("Exception should be thrown");
            
        } catch (IllegalArgumentException e) {
            assertEquals("Exception message should match", "Semi-colon required at end of numeric entity", e.getMessage());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIsSet() {
        // Test isSet method
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        assertTrue("OPTION.semiColonOptional should be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse("OPTION.semiColonRequired should not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse("OPTION.errorIfNoSemiColon should not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
}
