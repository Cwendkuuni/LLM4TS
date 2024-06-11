import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Typical use case
        CharSequenceTranslator translator = new MyTranslator();
        String input = "abc";
        String expectedOutput = "123";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
        
        // Edge case
        input = "";
        expectedOutput = "";
        actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithIndex() {
        // Typical use case
        CharSequenceTranslator translator = new MyTranslator();
        String input = "abc";
        StringWriter writer = new StringWriter();
        int index = 0;
        int expectedConsumed = 3;
        int actualConsumed = translator.translate(input, index, writer);
        assertEquals(expectedConsumed, actualConsumed);
        
        // Edge case
        input = "";
        writer = new StringWriter();
        expectedConsumed = 0;
        actualConsumed = translator.translate(input, index, writer);
        assertEquals(expectedConsumed, actualConsumed);
    }

    @Test
    public void testTranslateToWriter() {
        // Typical use case
        CharSequenceTranslator translator = new MyTranslator();
        String input = "abc";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
            String expectedOutput = "123";
            String actualOutput = writer.toString();
            assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            fail("IOException should not be thrown in this case");
        }
        
        // Exception handling scenario
        String nullInput = null;
        writer = new StringWriter();
        try {
            translator.translate(nullInput, writer);
            fail("NullPointerException should be thrown for null input");
        } catch (NullPointerException e) {
            // Expected exception
        } catch (IOException e) {
            fail("Unexpected IOException thrown for null input");
        }
    }

    @Test
    public void testWith() {
        // Typical use case
        CharSequenceTranslator translator1 = new MyTranslator();
        CharSequenceTranslator translator2 = new AnotherTranslator();
        CharSequenceTranslator resultTranslator = translator1.with(translator2);
        assertNotNull(resultTranslator);
    }

    @Test
    public void testHex() {
        // Typical use case
        int codepoint = 97; // 'a' in Unicode
        String expectedHex = "61";
        String actualHex = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedHex, actualHex);
        
        // Edge case
        codepoint = 128149; // A random Unicode codepoint
        expectedHex = "1F405";
        actualHex = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedHex, actualHex);
    }
}