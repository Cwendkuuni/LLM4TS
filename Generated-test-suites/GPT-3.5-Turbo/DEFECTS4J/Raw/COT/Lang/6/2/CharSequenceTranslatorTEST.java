import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Test basic functionality
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "abc";
        String expectedOutput = "ABC";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);

        // Test translating a null input
        input = null;
        assertNull(translator.translate(input));
    }

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        // Test basic functionality
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "abc";
        int index = 0;
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(input, index, writer);
        assertEquals(3, consumed);
        assertEquals("ABC", writer.toString());

        // Test translating an empty input
        input = "";
        index = 0;
        writer = new StringWriter();
        consumed = translator.translate(input, index, writer);
        assertEquals(0, consumed);
        assertEquals("", writer.toString());

        // Test translating an input that ends before consuming all codepoints
        input = "abcd";
        index = 0;
        writer = new StringWriter();
        consumed = translator.translate(input, index, writer);
        assertEquals(3, consumed);
        assertEquals("ABCd", writer.toString());
    }

    @Test
    public void testWith() {
        // Test merging translators
        CharSequenceTranslator translator1 = new ConcreteTranslator();
        CharSequenceTranslator translator2 = new ConcreteTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertNotNull(mergedTranslator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws IOException {
        // Test translating with a null writer
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "abc";
        int index = 0;
        Writer writer = null;
        translator.translate(input, index, writer);
    }

    @Test
    public void testHex() {
        // Test converting codepoint to hex
        int codepoint = 65;
        String expectedOutput = "41";
        String actualOutput = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, actualOutput);
    }
}

class ConcreteTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input.charAt(index) >= 'a' && input.charAt(index) <= 'z') {
            out.write(input.charAt(index) - 32);
            return 1;
        }
        return 0;
    }
}