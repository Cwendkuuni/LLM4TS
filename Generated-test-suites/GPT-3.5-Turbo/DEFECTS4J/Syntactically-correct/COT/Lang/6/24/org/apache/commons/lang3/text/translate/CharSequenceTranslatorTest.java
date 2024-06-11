import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Test basic functionality
        assertEquals("ABC", translator.translate("ABC"));

        // Test null input
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_withIndexAndWriter() throws Exception {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Test basic functionality
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("ABC", 0, writer);
        assertEquals(3, consumed);
        assertEquals("ABC", writer.toString());

        // Test null input
        writer = new StringWriter();
        consumed = translator.translate(null, 0, writer);
        assertEquals(0, consumed);
        assertEquals("", writer.toString());
    }

    @Test(expected=IOException.class)
    public void testTranslate_withIOException() throws Exception {
        CharSequenceTranslator translator = new FailingCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("ABC", 0, writer);
    }

    @Test
    public void testTranslate_withSurrogatePair() throws Exception {
        CharSequenceTranslator translator = new SurrogatePairTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("A\uD83D\uDE00BC", writer);
        assertEquals("A\\U0001F600BC", writer.toString());
    }

    @Test
    public void testTranslate_withAggregateTranslator() throws Exception {
        CharSequenceTranslator translator1 = new MockCharSequenceTranslator();
        CharSequenceTranslator translator2 = new FailingCharSequenceTranslator();
        CharSequenceTranslator aggregator = translator1.with(translator2);

        // Test basic functionality
        assertEquals("ABC", aggregator.translate("ABC"));

        // Test null input
        assertNull(aggregator.translate(null));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testTranslate_withEmptyString() throws Exception {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testTranslate_withNullWriter() throws Exception {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        translator.translate("ABC", 0, null);
    }

    @Test
    public void testTranslate_withEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("ABC", 0, writer);
        assertEquals(3, consumed);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withEmptyInput() throws Exception {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        assertNull(translator.translate(""));
    }

    @Test
    public void testHex() throws Exception {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("1F600", CharSequenceTranslator.hex(128512));
    }
}

class MockCharSequenceTranslator extends CharSequenceTranslator {
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }
        out.write(input.subSequence(index, input.length()).toString());
        return input.length() - index;
    }
}

class FailingCharSequenceTranslator extends CharSequenceTranslator {
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new IOException();
    }
}

class SurrogatePairTranslator extends CharSequenceTranslator {
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }
        String str = input.subSequence(index, input.length()).toString();
        if (str.length() > 1) {
            int codePoint = str.codePointAt(0);
            if (Character.isSupplementaryCodePoint(codePoint)) {
                out.write("\\" + CharSequenceTranslator.hex(codePoint));
                return 2;
            }
        }
        out.write(str);
        return input.length() - index;
    }
}