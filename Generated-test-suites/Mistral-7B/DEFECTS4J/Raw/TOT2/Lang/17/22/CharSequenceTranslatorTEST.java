import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorImpl;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslatorImpl();
        String input = "Hello, World!";
        String expectedOutput = "48656c6c6f2c20576f726c6421";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }
}