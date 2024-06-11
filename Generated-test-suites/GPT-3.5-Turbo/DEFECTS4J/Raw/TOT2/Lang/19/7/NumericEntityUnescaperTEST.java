import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Typical use case
        StringBuilder input1 = new StringBuilder("&#65;");
        StringBuilder output1 = new StringBuilder();
        unescaper.translate(input1, 0, new StringBuilderWriter(output1));
        assertEquals('A', output1.charAt(0));

        // Edge case : hexadecimal input
        StringBuilder input2 = new StringBuilder("&#x41;");
        StringBuilder output2 = new StringBuilder();
        unescaper.translate(input2, 0, new StringBuilderWriter(output2));
        assertEquals('A', output2.charAt(0));

        // Error scenario: invalid input
        StringBuilder input3 = new StringBuilder("&#invalid;");
        StringBuilder output3 = new StringBuilder();
        unescaper.translate(input3, 0, new StringBuilderWriter(output3));
        assertEquals(0, output3.length());
    }

}