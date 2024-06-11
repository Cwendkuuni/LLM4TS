
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    // Test the translate method
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        
        // Test numeric entity with decimal value
        int result1 = unescaper.translate("&#65;", 0, new StringWriter(output));
        assertEquals(5, result1); // Check that writer outputs correct codepoint
        assertEquals("A", output.toString()); // Check that the output is correct
        
        // Test numeric entity with hex value
        int result2 = unescaper.translate("&#x41;", 0, new StringWriter(output));
        assertEquals(7, result2); // Check that writer outputs correct codepoint
        assertEquals("AA", output.toString()); // Check that the output is correct
        
        // Test invalid numeric entity
        int result3 = unescaper.translate("&#invalid;", 0, new StringWriter(output));
        assertEquals(0, result3); // Check that invalid entity returns 0
    }
}