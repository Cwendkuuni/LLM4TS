import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test with valid numeric entity
        StringBuilder input1 = new StringBuilder("&amp;&#65;&#x41;");
        StringBuilder output1 = new StringBuilder();
        try {
            unescaper.translate(input1, 0, new java.io.StringWriter(output1));
        } catch (java.io.IOException e) {
            fail("IOException was thrown");
        }
        assertEquals("output1 should be equal", "&A,A", output1.toString());
        
        // Test with invalid numeric entity
        StringBuilder input2 = new StringBuilder("&amp;&#65a;&#x41;");
        StringBuilder output2 = new StringBuilder();
        try {
            unescaper.translate(input2, 0, new java.io.StringWriter(output2));
        } catch (java.io.IOException e) {
            fail("IOException was thrown");
        }
        assertEquals("output2 should be equal", "", output2.toString());
    }
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test with a set option
        boolean result1 = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(result1);
        
        // Test with an unset option
        boolean result2 = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertFalse(result2);
    }
    
    // Add more test cases to cover all public methods
    
}