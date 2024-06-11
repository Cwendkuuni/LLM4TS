import static org.junit.Assert.*;
import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test when input is empty
        int result = unescaper.translate("", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
        
        // Test when input does not start with '&' and '#' characters
        result = unescaper.translate("abc", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
        
        // Test when input starts with '&' and '#' characters
        result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
        
        // Test when hexadecimal number is used
        result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
        
        // Test when semicolon is not present
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        writer = new StringWriter();
        result = unescaper.translate("&#65", 0, writer);
        assertEquals(2, result);
        assertEquals("A", writer.toString());
        
        // Test when semicolon is required
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        writer = new StringWriter();
        result = unescaper.translate("&#65", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
        
        // Test when error should be thrown if semicolon is not present
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        writer = new StringWriter();
        try {
            unescaper.translate("&#65", 0, writer);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
        assertEquals("", writer.toString());
        
        // Test when input contains non-numeric characters
        result = unescaper.translate("&#ABC;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test when options is null
        boolean result = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertFalse(result);
        
        // Test when options contains the specified option
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        result = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(result);
        
        // Test when options does not contain the specified option
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        result = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertFalse(result);
    }
}