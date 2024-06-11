
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Basic test case with valid numeric entity
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("A", output.toString());

        // Edge case: Empty input
        input = new StringBuilder("");
        output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("", output.toString());

        // Edge case: Input without numeric entity
        input = new StringBuilder("abc");
        output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("", output.toString());

        // Exception handling: Input with invalid hexadecimal entity
        input = new StringBuilder("&#xyz;");
        output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("", output.toString());

        // Exception handling: Input with invalid decimal entity
        input = new StringBuilder("&#10a;");
        output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("", output.toString());

        // Exception handling: Input with missing ending semicolon
        input = new StringBuilder("&#65");
        output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("", output.toString());
    }
}