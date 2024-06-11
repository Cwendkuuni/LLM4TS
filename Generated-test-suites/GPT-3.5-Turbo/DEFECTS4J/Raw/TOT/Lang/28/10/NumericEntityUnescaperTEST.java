import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidInput_ValidOutput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Typical use case
        String input1 = "&#65;";
        StringBuilder output1 = new StringBuilder();
        try {
            unescaper.translate(input1, 0, output1);
        } catch(IOException e) {
            fail("IOException is not expected");
        }
        assertEquals("A", output1.toString());
        
        // Edge case - Lower bound
        String input2 = "&#0;";
        StringBuilder output2 = new StringBuilder();
        try {
            unescaper.translate(input2, 0, output2);
        } catch(IOException e) {
            fail("IOException is not expected");
        }
        assertEquals("\u0000", output2.toString());
        
        // Edge case - Upper bound
        String input3 = "&#65535;";
        StringBuilder output3 = new StringBuilder();
        try {
            unescaper.translate(input3, 0, output3);
        } catch(IOException e) {
            fail("IOException is not expected");
        }
        assertEquals("\uffff", output3.toString());
    }
    
    @Test
    public void testTranslate_InvalidInput_Return0() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Error scenario - No input string
        String input1 = "";
        StringBuilder output1 = new StringBuilder();
        try {
            int result = unescaper.translate(input1, 0, output1);
            assertEquals(0, result);
            assertEquals("", output1.toString());
        } catch(IOException e) {
            fail("IOException is not expected");
        }
        
        // Error scenario - Invalid entity value
        String input2 = "&#invalid;";
        StringBuilder output2 = new StringBuilder();
        try {
            int result = unescaper.translate(input2, 0, output2);
            assertEquals(0, result);
            assertEquals("", output2.toString());
        } catch(IOException e) {
            fail("IOException is not expected");
        }
        
        // Error scenario - Invalid character sequence
        String input3 = "&#x65;";
        StringBuilder output3 = new StringBuilder();
        try {
            int result = unescaper.translate(input3, 0, output3);
            assertEquals(0, result);
            assertEquals("", output3.toString());
        } catch(IOException e) {
            fail("IOException is not expected");
        }
    }

}