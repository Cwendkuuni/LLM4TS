import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test when input is empty
        StringBuilder input = new StringBuilder();
        StringBuilder output = new StringBuilder();
        int index = 0;
        int result = unescaper.translate(input, index, new Writer(output));
        assertEquals(0, result);
        
        // Test when input is not empty
        input = new StringBuilder("&#65;");
        output = new StringBuilder();
        index = 0;
        result = unescaper.translate(input, index, new Writer(output));
        assertEquals(5, result);
        assertEquals("A", output.toString());
        
        // Test when input is not valid number
        input = new StringBuilder("&#ABC;");
        output = new StringBuilder();
        index = 0;
        result = unescaper.translate(input, index, new Writer(output));
        assertEquals(0, result);
        assertEquals("", output.toString());
        
        // Test when input starts with &#x
        input = new StringBuilder("&#x41;");
        output = new StringBuilder();
        index = 0;
        result = unescaper.translate(input, index, new Writer(output));
        assertEquals(5, result);
        assertEquals("A", output.toString());
        
        // Test when input does not start with &#x
        input = new StringBuilder("&#41;");
        output = new StringBuilder();
        index = 0;
        result = unescaper.translate(input, index, new Writer(output));
        assertEquals(4, result);
        assertEquals(")", output.toString());
        
        // Test when input ends with ;
        input = new StringBuilder("&#65;");
        output = new StringBuilder();
        index = 0;
        result = unescaper.translate(input, index, new Writer(output));
        assertEquals(5, result);
        assertEquals("A", output.toString());
        
        // Test when input does not end with ;
        input = new StringBuilder("&#65");
        output = new StringBuilder();
        index = 0;
        result = unescaper.translate(input, index, new Writer(output));
        assertEquals(4, result);
        assertEquals("", output.toString());
    }
}