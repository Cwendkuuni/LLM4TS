import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_validNumericEntity_returnsTranslatedCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &amp;#65;");
        StringBuilder expectedOutput = new StringBuilder("This is a test A");
        StringBuilder output = new StringBuilder();

        try {
            int newIndex = unescaper.translate(input, 15, output);
            assertEquals(12, newIndex);
            assertEquals(expectedOutput.toString(), output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testTranslate_invalidNumericEntity_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is an invalid entity &amp;#notanumber;");
        StringBuilder expectedOutput = new StringBuilder("This is an invalid entity &amp;#notanumber;");
        StringBuilder output = new StringBuilder();

        try {
            int newIndex = unescaper.translate(input, 24, output);
            assertEquals(0, newIndex);
            assertEquals(expectedOutput.toString(), output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testTranslate_validHexadecimalNumericEntity_returnsTranslatedCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &amp;#x41;");
        StringBuilder expectedOutput = new StringBuilder("This is a test A");
        StringBuilder output = new StringBuilder();

        try {
            int newIndex = unescaper.translate(input, 15, output);
            assertEquals(10, newIndex);
            assertEquals(expectedOutput.toString(), output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testTranslate_emptyInput_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("");
        StringBuilder expectedOutput = new StringBuilder("");
        StringBuilder output = new StringBuilder();

        try {
            int newIndex = unescaper.translate(input, 0, output);
            assertEquals(0, newIndex);
            assertEquals(expectedOutput.toString(), output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}