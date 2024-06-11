import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator(); // replace with actual translator
        String input = "Hello World!";
        String expectedOutput = "48656C6C6F20576F726C6421"; // "Hello World!" in hex
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }
}