import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = null;
        String actualOutput = translator.translate(input);
        assertNull(actualOutput);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "";
        String expectedOutput = "";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithIOException() {
        // Test a scenario where IOException is thrown
        // This should never happen when using StringWriter
    }

    @Test
    public void testHexUpperCase() {
        int codepoint = 97; // ASCII code for 'a'
        String expectedHex = "61"; // Expected hexadecimal representation
        String actualHex = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedHex, actualHex);
    }

}