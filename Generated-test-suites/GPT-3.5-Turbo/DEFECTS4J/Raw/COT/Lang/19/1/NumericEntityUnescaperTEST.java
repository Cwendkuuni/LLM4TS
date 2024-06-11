import org.junit.Test;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        // Basic test case where input string contains a numeric entity
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("The numeric entity &#65; translates to 'A'.");
        StringBuilder expected = new StringBuilder("The numeric entity A translates to 'A'.");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(expected.toString(), output.toString());
        assertEquals(8, result);

        // Test case for input string without a numeric entity
        input = new StringBuilder("This string does not contain a numeric entity.");
        expected = new StringBuilder("This string does not contain a numeric entity.");
        output = new StringBuilder();
        result = unescaper.translate(input, 0, output);
        assertEquals(expected.toString(), output.toString());
        assertEquals(0, result);

        // Test case for input string with multiple numeric entities
        input = new StringBuilder("The numeric entities &#66; and &#67; translate to 'B' and 'C' respectively.");
        expected = new StringBuilder("The numeric entities B and C translate to 'B' and 'C' respectively.");
        output = new StringBuilder();
        result = unescaper.translate(input, 0, output);
        assertEquals(expected.toString(), output.toString());
        assertEquals(8, result);
    }
}