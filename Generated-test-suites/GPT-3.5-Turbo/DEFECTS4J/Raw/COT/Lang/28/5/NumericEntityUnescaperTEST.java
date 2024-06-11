
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;
import java.io.StringWriter;
import java.io.Writer;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws Exception {
        // Test with numeric entity
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequenceTranslator translator = new AggregateTranslator(unescaper);
        char[] input = "Testing &#65;".toCharArray();
        Writer output = new StringWriter();
        translator.translate(input, 0, output);
        assert(output.toString().equals("Testing A"));
        
        // Test with hex numeric entity
        unescaper = new NumericEntityUnescaper();
        translator = new AggregateTranslator(unescaper);
        input = "Testing &#x41;".toCharArray();
        output = new StringWriter();
        translator.translate(input, 0, output);
        assert(output.toString().equals("Testing A"));
        
        // Test without numeric entity
        unescaper = new NumericEntityUnescaper();
        translator = new AggregateTranslator(unescaper);
        input = "Testing ABC".toCharArray();
        output = new StringWriter();
        translator.translate(input, 0, output);
        assert(output.toString().equals("Testing ABC"));
    }
}
