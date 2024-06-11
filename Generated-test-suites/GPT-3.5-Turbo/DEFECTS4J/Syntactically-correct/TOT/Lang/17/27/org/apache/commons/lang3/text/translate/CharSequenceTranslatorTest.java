import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new TestTranslator();
    }

    @Test
    public void testTranslate_withNullInput_returnsNull() {
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslate_withEmptyInput_returnsEmptyString() {
        String result = translator.translate("");
        assertEquals("", result);
    }

    @Test
    public void testTranslate_withNoTranslators_returnsSameInput() {
        String input = "This is a test";
        String result = translator.translate(input);
        assertEquals(input, result);
    }

    @Test
    public void testTranslate_withTranslators_returnsTranslatedOutput() {
        String input = "This is a test";
        String expected = "Th!s!s#t#st";
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new AnotherTranslator();
        translator = translator1.with(translator2);
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslate_withIOException_throwsRuntimeException() {
        String input = "This is a test";
        CharSequenceTranslator translator1 = new TestTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        translator = translator1;
        try {
            translator.translate(input);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Test passed
        }
    }

    @Test
    public void testTranslate_withSurrogatePair() {
        String input = "This is a test \uD83D\uDE01";
        String expected = "Th!s!s#t#st ";
        translator = new TestTranslator();
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    private class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == '!') {
                out.write('#');
                return 1;
            }
            if (input.charAt(index) == '#') {
                out.write('!');
                return 1;
            }
            out.write(input.charAt(index));
            return 0;
        }
    }

    private class AnotherTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 's') {
                out.write('!');
                return 1;
            }
            if (input.charAt(index) == 't') {
                out.write('#');
                return 1;
            }
            out.write(input.charAt(index));
            return 0;
        }
    }
}