import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Typical case
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        assertEquals(expectedOutput, translator.translate(input));
        
        // Edge case: input is null
        assertEquals(null, translator.translate(null));
        
        // Edge case: empty input
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslateWithIndex() {
        // Typical case
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        String input = "Hello World!";
        int index = 0;
        int expectedCount = 12;
        try {
            int consumed = translator.translate(input, index, writer);
            assertEquals(expectedCount, consumed);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Exception handling scenario: IOException from Writer
        StringWriter errorWriter = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        try {
            translator.translate(input, index, errorWriter);
            fail("IOException should have been thrown");
        } catch (IOException e) {
            // Expected behavior
        }
    }

    @Test
    public void testTranslateWithWriter() {
        // Typical case
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        String input = "Hello World!";
        try {
            translator.translate(input, writer);
            assertEquals("Hello World!", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Edge case: Writer is null
        try {
            translator.translate(input, null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected behavior
        }
    }

}