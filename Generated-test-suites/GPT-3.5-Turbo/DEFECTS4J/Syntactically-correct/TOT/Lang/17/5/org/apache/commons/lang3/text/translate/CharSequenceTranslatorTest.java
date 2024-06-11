import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockTranslator();

        // Test typical use case
        String input1 = "Hello, world!";
        String expectedOutput1 = "Hello, world!";
        String actualOutput1 = translator.translate(input1);
        assertEquals(expectedOutput1, actualOutput1);

        // Test edge case - empty input
        String input2 = "";
        String expectedOutput2 = "";
        String actualOutput2 = translator.translate(input2);
        assertEquals(expectedOutput2, actualOutput2);

        // Test error scenario - null input
        String input3 = null;
        String expectedOutput3 = null;
        String actualOutput3 = translator.translate(input3);
        assertEquals(expectedOutput3, actualOutput3);
    }

    @Test
    public void testTranslateWithWriter() {
        CharSequenceTranslator translator = new MockTranslator();

        // Test typical use case
        String input1 = "Hello, world!";
        String expectedOutput1 = "Hello, world!";
        StringWriter writer1 = new StringWriter(input1.length() * 2);
        try {
            translator.translate(input1, writer1);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        String actualOutput1 = writer1.toString();
        assertEquals(expectedOutput1, actualOutput1);

        // Test edge case - empty input
        String input2 = "";
        String expectedOutput2 = "";
        StringWriter writer2 = new StringWriter(input2.length() * 2);
        try {
            translator.translate(input2, writer2);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        String actualOutput2 = writer2.toString();
        assertEquals(expectedOutput2, actualOutput2);

        // Test error scenario - null input
        String input3 = null;
        StringWriter writer3 = new StringWriter();
        try {
            translator.translate(input3, writer3);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testHex() {
        // Test typical use case
        int codepoint1 = 65;
        String expectedOutput1 = "41";
        String actualOutput1 = CharSequenceTranslator.hex(codepoint1);
        assertEquals(expectedOutput1, actualOutput1);

        // Test edge case - lowest possible codepoint
        int codepoint2 = 0;
        String expectedOutput2 = "0";
        String actualOutput2 = CharSequenceTranslator.hex(codepoint2);
        assertEquals(expectedOutput2, actualOutput2);

        // Test edge case - highest possible codepoint
        int codepoint3 = 1114111;
        String expectedOutput3 = "10FFFF";
        String actualOutput3 = CharSequenceTranslator.hex(codepoint3);
        assertEquals(expectedOutput3, actualOutput3);
    }

    private static class MockTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) {
            try {
                out.write(input.toString().substring(index));
                return input.length() - index;
            } catch (Exception e) {
                return 0;
            }
        }
    }

}