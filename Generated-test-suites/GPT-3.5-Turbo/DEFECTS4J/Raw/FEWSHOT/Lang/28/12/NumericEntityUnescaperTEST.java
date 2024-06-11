
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test with input containing numeric entities
        StringBuilder input1 = new StringBuilder();
        input1.append("This is a test string with numeric entities: ");
        input1.append("&amp;#65; &amp;#66; &amp;#67;");
        StringBuilder output1 = new StringBuilder();
        unescaper.translate(input1, 0, new StringWriter(output1));
        assertEquals("This is a test string with numeric entities: ABC", output1.toString());
        
        // Test with input containing hexadecimal entities
        StringBuilder input2 = new StringBuilder();
        input2.append("This input contains hexadecimal entities: ");
        input2.append("&amp;#x41; &amp;#x42; &amp;#x43;");
        StringBuilder output2 = new StringBuilder();
        unescaper.translate(input2, 0, new StringWriter(output2));
        assertEquals("This input contains hexadecimal entities: ABC", output2.toString());
        
        // Test with input containing invalid entities
        StringBuilder input3 = new StringBuilder();
        input3.append("This input contains invalid entities: ");
        input3.append("&amp;#invalid; &amp;#1234; &amp;#xg;");
        StringBuilder output3 = new StringBuilder();
        unescaper.translate(input3, 0, new StringWriter(output3));
        assertEquals("This input contains invalid entities: &amp;#invalid; &amp;#1234; &amp;#xg;", output3.toString());
        
        // Test with input without any entities
        StringBuilder input4 = new StringBuilder();
        input4.append("This input doesn't contain any entities.");
        StringBuilder output4 = new StringBuilder();
        unescaper.translate(input4, 0, new StringWriter(output4));
        assertEquals("This input doesn't contain any entities.", output4.toString());
    }
    
}
