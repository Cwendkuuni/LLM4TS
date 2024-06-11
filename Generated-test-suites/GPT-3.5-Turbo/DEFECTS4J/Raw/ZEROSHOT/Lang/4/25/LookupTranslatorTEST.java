import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[] { "cat", "dog" },
                new CharSequence[] { "apple", "banana" }
        );
        
        String input = "I have a cat and a dog";
        StringBuilder output = new StringBuilder();
        int index = 0;
        
        try {
            while (index < input.length()) {
                int translatedLen = translator.translate(input, index, output);
                if (translatedLen == 0) {
                    output.append(input.charAt(index));
                    index++;
                } else {
                    index += translatedLen;
                }
            }
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        
        assertEquals("I have a apple and a banana", output.toString());
    }
}