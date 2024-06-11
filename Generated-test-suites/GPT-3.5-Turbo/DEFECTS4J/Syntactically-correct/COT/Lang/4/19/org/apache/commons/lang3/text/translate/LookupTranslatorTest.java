
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder input = new StringBuilder("apple");
        StringBuilder expectedOutput = new StringBuilder("apple");
        try {
            translator.translate(input, 0, System.out);
            assertEquals(expectedOutput, input.toString());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateWithLookup() {
        CharSequence[] lookup = new CharSequence[2];
        lookup[0] = "apple";
        lookup[1] = "fruit";
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder input = new StringBuilder("apple");
        StringBuilder expectedOutput = new StringBuilder("fruit");
        try {
            translator.translate(input, 0, System.out);
            assertEquals(expectedOutput, input.toString());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

}
