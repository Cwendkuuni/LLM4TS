import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test input without numeric entities
        CharSequence input1 = "This is a test string";
        StringWriter out1 = new StringWriter();
        unescaper.translate(input1, 0, out1);
        assert(out1.toString().equals(""));
        
        // Test input with numeric entities
        CharSequence input2 = "This is a test string &#65; with numeric entities";
        StringWriter out2 = new StringWriter();
        unescaper.translate(input2, 0, out2);
        assert(out2.toString().equals("This is a test string A with numeric entities"));
        
        // Test input with hexadecimal numeric entities
        CharSequence input3 = "This is a test string &#x41; with hexadecimal numeric entities";
        StringWriter out3 = new StringWriter();
        unescaper.translate(input3, 0, out3);
        assert(out2.toString().equals("This is a test string A with hexadecimal numeric entities"));    
    }
    
}