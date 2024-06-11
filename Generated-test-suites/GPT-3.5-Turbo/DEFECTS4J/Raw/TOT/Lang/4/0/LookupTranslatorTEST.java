import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_typicalUseCase() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] { "aaa", "111" },
            new CharSequence[] { "bbb", "222" }
        );
        
        StringBuilder input = new StringBuilder("test aaa test bbb");
        StringBuilder expectedResult = new StringBuilder("test 111 test 222");

        StringBuilder result = new StringBuilder();
        int translatedLength = 0;
        try {
            translatedLength = translator.translate(input, 0, result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }

        assertEquals("Translated length does not match", expectedResult.length(), translatedLength);
        assertEquals("Translation result does not match", expectedResult.toString(), result.toString());
    }

    @Test
    public void testTranslate_edgeCase_shortestInput() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] { "", "empty" },
            new CharSequence[] { "bbb", "222" }
        );
        
        StringBuilder input = new StringBuilder("");
        StringBuilder expectedResult = new StringBuilder("empty");

        StringBuilder result = new StringBuilder();
        int translatedLength = 0;
        try {
            translatedLength = translator.translate(input, 0, result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }

        assertEquals("Translated length does not match", expectedResult.length(), translatedLength);
        assertEquals("Translation result does not match", expectedResult.toString(), result.toString());
    }

    @Test
    public void testTranslate_edgeCase_longestInput() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] { "aaa", "111" },
            new CharSequence[] { "bbb", "222" }
        );
        
        StringBuilder input = new StringBuilder("test aaa test bbb");
        StringBuilder expectedResult = new StringBuilder("test 111 test 222");

        StringBuilder result = new StringBuilder();
        int translatedLength = 0;
        try {
            translatedLength = translator.translate(input, 0, result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }

        assertEquals("Translated length does not match", expectedResult.length(), translatedLength);
        assertEquals("Translation result does not match", expectedResult.toString(), result.toString());
    }

    @Test
    public void testTranslate_errorScenario_nullInput() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] { "aaa", "111" },
            new CharSequence[] { "bbb", "222" }
        );
        
        StringBuilder input = null;
        StringBuilder expectedResult = new StringBuilder("");

        StringBuilder result = new StringBuilder();
        int translatedLength = 0;
        try {
            translatedLength = translator.translate(input, 0, result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }

        assertEquals("Translated length does not match", expectedResult.length(), translatedLength);
        assertEquals("Translation result does not match", expectedResult.toString(), result.toString());
    }
}