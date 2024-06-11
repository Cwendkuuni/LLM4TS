import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "This is a test";
        String expected = "This_is_a_test";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithWriter() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "This is a test";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        String expected = "This_is_a_test";
        String result = writer.toString();
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "This is a test";
        translator.translate(input, null);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "";
        String expected = "";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithSurrogatePair() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "😀 is a smiley";
        String expected = "😀_is_a_smiley";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithException() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "This is a test";
        StringWriter writer = new StringWriter() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test Exception");
            }
        };
        try {
            translator.translate(input, writer);
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new ConcreteTranslator();
        CharSequenceTranslator translator2 = new AdditionalTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        String input = "This is a test";
        String expected = "This**is**a**test";
        String result = mergedTranslator.translate(input);
        assertEquals(expected, result);
    }

    private class ConcreteTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                return 0;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = index; i < input.length(); i++) {
                char c = input.charAt(i);
                if (Character.isLetter(c)) {
                    sb.append(c);
                } else {
                    sb.append('_');
                }
            }
            out.write(sb.toString());
            return input.length() - index;
        }
    }

    private class AdditionalTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                return 0;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = index; i < input.length(); i++) {
                char c = input.charAt(i);
                if (Character.isLetter(c)) {
                    sb.append(c);
                } else {
                    sb.append("**");
                }
            }
            out.write(sb.toString());
            return input.length() - index;
        }
    }
}