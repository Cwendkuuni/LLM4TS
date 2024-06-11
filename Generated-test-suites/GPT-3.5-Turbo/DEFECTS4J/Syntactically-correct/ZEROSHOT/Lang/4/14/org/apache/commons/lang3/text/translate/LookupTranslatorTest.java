import static org.junit.Assert.*;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{ "a", "1" }, { "b", "2" }, { "c", "3" }});
        
        // Test case 1: input string with multiple occurrences of the same character
        String input1 = "abcabc";
        StringBuilder output1 = new StringBuilder();
        int result1 = translator.translate(input1, 0, output1);
        assertEquals(output1.toString(), "123123");
        assertEquals(result1, 6);
        
        // Test case 2: input string with different characters
        String input2 = "cab";
        StringBuilder output2 = new StringBuilder();
        int result2 = translator.translate(input2, 0, output2);
        assertEquals(output2.toString(), "321");
        assertEquals(result2, 3);
        
        // Test case 3: input string with characters not in the lookup table
        String input3 = "def";
        StringBuilder output3 = new StringBuilder();
        int result3 = translator.translate(input3, 0, output3);
        assertEquals(output3.toString(), "");
        assertEquals(result3, 0);
    }
}