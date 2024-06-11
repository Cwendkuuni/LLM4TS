
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65;", 0, new StringWriter(output));
        assertEquals(6, result);
        assertEquals("A", output.toString());
    }

    @Test 
    public void testTranslateWhenHex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#x41;", 0, new StringWriter(output));
        assertEquals(8, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslateWhenSemiColonRequired() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65", 0, new StringWriter(output));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
    }

    @Test
    public void testTranslateWhenSemiColonOptional() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate("&#65", 0, new StringWriter(output));
        assertEquals(4, result);
        assertEquals("A", output.toString());
    }
    
    @Test
    public void testTranslateWhenErrorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65", 0, new StringWriter(output));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    // ...
    
}
