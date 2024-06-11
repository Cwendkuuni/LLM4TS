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
        } catch (IOException e) {
            fail("IOException should not be thrown in typical use case");
        }
        assertEquals("A", output.toString());
        
        // Edge case: Unicode character
        output.setLength(0);
        try {
            unescaper.translate("&#8364;", 0, output);
        } catch (IOException e) {
            fail("IOException should not be thrown in edge case");
        }
        assertEquals("€", output.toString());
        
        // Error scenario: Invalid numeric entity
        output.setLength(0);
        try {
            unescaper.translate("&#xyz;", 0, output);
        } catch (IOException e) {
            assertTrue(e instanceof NumberFormatException);
            System.out.println("NumberFormatException for invalid numeric entity handled correctly");
        }
    }
}