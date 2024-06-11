import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        
        // Test when input is empty
        String input = "";
        StringBuilder output = new StringBuilder();
        assertEquals(0, translator.translate(input, 0, output));
        assertEquals("", output.toString());
        
        // Test when input does not start with "&#"
        input = "abc";
        output = new StringBuilder();
        assertEquals(0, translator.translate(input, 0, output));
        assertEquals("", output.toString());
        
        // Test when input starts with "&#"
        input = "&#65;";
        output = new StringBuilder();
        assertEquals(6, translator.translate(input, 0, output));
        assertEquals("A", output.toString());
        
        // Test when input starts with "&#x"
        input = "&#x41;";
        output = new StringBuilder();
        assertEquals(6, translator.translate(input, 0, output));
        assertEquals("A", output.toString());
        
        // Test when input is not a valid numeric entity
        input = "&#1a;";
        output = new StringBuilder();
        assertEquals(0, translator.translate(input, 0, output));
        assertEquals("", output.toString());
        
        // Test when input contains multiple valid numeric entities
        input = "&#65;&#x41;";
        output = new StringBuilder();
        assertEquals(12, translator.translate(input, 0, output));
        assertEquals("AA", output.toString());
        
        // Test when input contains invalid numeric entities mixed with valid ones
        input = "&#65;&1a;&#x41;";
        output = new StringBuilder();
        assertEquals(0, translator.translate(input, 0, output));
        assertEquals("", output.toString());
        
        // Test when input contains numeric entities with values outside the valid range
        input = "&#-1;&#x110000;&#65;";
        output = new StringBuilder();
        assertEquals(11, translator.translate(input, 0, output));
        assertEquals("A", output.toString());
    }
}