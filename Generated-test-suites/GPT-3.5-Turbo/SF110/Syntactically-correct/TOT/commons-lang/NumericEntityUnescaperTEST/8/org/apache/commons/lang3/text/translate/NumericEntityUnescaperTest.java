

import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    // Test cases for isSet method
    @Test
    public void testIsSet() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        
        assertTrue(translator.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(translator.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(translator.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
    // Test cases for translate method
    @Test
    public void testTranslateWithNumericEntityWithoutSemiColonRequired() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        StringBuilder input = new StringBuilder();
        input.append("&#65"); // A
        
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, new java.io.StringWriter());
        
        assertEquals("Invalid translation", 0, output.toString().length());
    }
    
    @Test
    public void testTranslateWithNumericEntityWithoutSemiColonOptional() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        
        StringBuilder input = new StringBuilder();
        input.append("&#65"); // A
        
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, new java.io.StringWriter());
        
        assertEquals("Invalid translation", 1, output.toString().length());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNumericEntityWithoutSemiColonErrorIfNoSemiColon() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        
        StringBuilder input = new StringBuilder();
        input.append("&#65"); // A
        
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, new java.io.StringWriter());
    }
    
    @Test
    public void testTranslateWithDecimalNumericEntity() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder();
        input.append("&#65;"); // A
        
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, output);
        
        assertEquals("Invalid translation", "A", output.toString());
    }
    
    @Test
    public void testTranslateWithHexadecimalNumericEntity() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder();
        input.append("&#x41;"); // A
        
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, output);
        
        assertEquals("Invalid translation", "A", output.toString());
    }

    @Test
    public void testTranslateWithDecimalEntityGreaterThan65535() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder();
        input.append("&#65536;"); // Greater than 65535
        
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, output);
        
        assertEquals("Invalid translation", "\uD800\uDC00", output.toString());
    }
    
    @Test
    public void testTranslateWithNonNumericEntity() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder();
        input.append("&#nonnumeric;"); // nonnumeric
        
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, output);
        
        assertEquals("Invalid translation", 0, output.toString().length());
    }
}

