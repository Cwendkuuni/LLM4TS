import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        // Test input without numeric entity
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(0, result);
        assertEquals("This is a test", output.toString());

        // Test input with decimal numeric entity
        input = new StringBuilder("This is &#65; test");
        output = new StringBuilder();
        result = unescaper.translate(input, 0, output);
        assertEquals(10, result);
        assertEquals("This is A test", output.toString());

        // Test input with hexadecimal numeric entity
        input = new StringBuilder("This is &#x41; test");
        output = new StringBuilder();
        result = unescaper.translate(input, 0, output);
        assertEquals(12, result);
        assertEquals("This is A test", output.toString());

        // Test input with invalid numeric entity
        input = new StringBuilder("This is &#invalid; test");
        output = new StringBuilder();
        result = unescaper.translate(input, 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());

    }
}