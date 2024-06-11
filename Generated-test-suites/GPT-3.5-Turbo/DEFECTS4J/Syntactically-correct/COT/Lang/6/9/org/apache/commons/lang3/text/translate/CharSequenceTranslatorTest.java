import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    // Method: translate(CharSequence, int, Writer)
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();

        // Test case 1: Normal translation
        StringBuilder expectedOutput1 = new StringBuilder();
        expectedOutput1.append("Hello");
        StringWriter writer1 = new StringWriter();
        try {
            int consumed = translator.translate("Hello", 0, writer1);
            assertEquals("Incorrect consumed codepoints.", 5, consumed);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals("Incorrect translation output.", expectedOutput1.toString(), writer1.toString());

        // Test case 2: Translation with surrogate pairs
        StringBuilder expectedOutput2 = new StringBuilder();
        expectedOutput2.append("Hello\uD83D\uDE00");
        StringWriter writer2 = new StringWriter();
        try {
            int consumed = translator.translate("Hello😀", 0, writer2);
            assertEquals("Incorrect consumed codepoints.", 7, consumed);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals("Incorrect translation output.", expectedOutput2.toString(), writer2.toString());
    }

    // Method: translate(CharSequence)
    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();

        String output = translator.translate(null);

        assertNull("Output should be null for null input.", output);
    }

    @Test
    public void testTranslateWithNonNullInput() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();

        String input = "Hello";
        String expectedOutput = "Hello";
        String output = translator.translate(input);

        assertEquals("Incorrect translation output.", expectedOutput, output);
    }

    // Method: with(CharSequenceTranslator...)
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new ConcreteCharSequenceTranslator();
        CharSequenceTranslator translator2 = new OtherConcreteCharSequenceTranslator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        assertNotNull("Merged translator should not be null.", mergedTranslator);
        assertTrue("Merged translator should contain translator1.", mergedTranslator instanceof AggregateTranslator);
    }

    // Method: hex(int)
    @Test
    public void testHex() {
        int codepoint = 65; // 'A'
        String expectedOutput = "41";

        String output = CharSequenceTranslator.hex(codepoint);

        assertEquals("Incorrect hexadecimal string.", expectedOutput, output);
    }
}

