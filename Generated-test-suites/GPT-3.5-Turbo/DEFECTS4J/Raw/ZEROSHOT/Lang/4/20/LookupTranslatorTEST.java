import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[][]{{"a", "1"}, {"b", "2"}, {"c", "3"}}
        );

        String input = "abc";
        StringBuilder output = new StringBuilder();
        int translated = 0;

        try {
            translated = translator.translate(input, 0, output);
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }

        assertEquals("123", output.toString());
        assertEquals(3, translated);

        input = "def";
        output = new StringBuilder();
        translated = 0;

        try {
            translated = translator.translate(input, 0, output);
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }

        assertEquals("", output.toString());
        assertEquals(0, translated);
    }

}