import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Typical use case
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, output);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        assertEquals("A", output.toString());
        
        // Edge case: Empty input
        output = new StringBuilder();
        try {
            unescaper.translate("", 0, output);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        assertEquals("", output.toString());
        
        // Exception handling: Invalid entity value
        output = new StringBuilder();
        try {
            unescaper.translate("&#invalid;", 0, output);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
}