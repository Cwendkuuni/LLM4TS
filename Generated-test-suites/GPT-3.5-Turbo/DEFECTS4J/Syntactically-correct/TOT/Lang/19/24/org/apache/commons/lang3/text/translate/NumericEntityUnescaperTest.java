import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Typical use case
        String input = "&#65;";
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        assertEquals("A", output.toString());

        // Edge case - Minimum input
        input = "&#0;";
        output.setLength(0);
        try {
            unescaper.translate(input, 0, output);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        assertEquals("\u0000", output.toString());

        // Edge case - Maximum input
        input = "&#1114111;";
        output.setLength(0);
        try {
            unescaper.translate(input, 0, output);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        assertEquals("\uD834\uDD1E", output.toString());

        // Error scenario - Invalid input format
        input = "&#notANumber;";
        output.setLength(0);
        try {
            unescaper.translate(input, 0, output);
            fail("NumberFormatException not thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Wrong exception occurred: " + e.getClass().getSimpleName());
        }
    }
}