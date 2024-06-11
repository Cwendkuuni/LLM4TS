import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateMethod() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        CharSequence input = "test input";
        Writer out = new StringWriter();

        // Testing default translate method
        String result = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return super.translate(input, index, out);
            }
        }.translate(input);
        assertEquals("test input", result);

        // Testing translate abstract method
        verify(translator).translate(input, out);

        // Testing translate method with given CharSequenceTranslator instance
        translator = new AggregateTranslator(translator, new IdentityTranslator());
        result = translator.translate(input);
        assertEquals("test input", result);
        verify(translator).translate(input, out);
        verify(translator.getTranslators()[0]).translate(input, out);

        // Testing exception handling
        assertThrows(NullPointerException.class, () -> translator.translate(null, out));
        assertThrows(NullPointerException.class, () -> translator.translate(input, null));
    }

    @Test
    public void testTranslateHelperMethod() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        CharSequence input = "test input";
        StringWriter writer = new StringWriter();

        // Testing translate method with given StringWriter
        translator.translate(input, writer);
        String result = writer.toString();
        assertEquals("test input", result);
        verify(translator).translate(input, writer);

        // Testing edge cases
        String inputWithSurrogatePair = "\uD800\uDC00";
        translator = new IdentityTranslator();
        writer = new StringWriter();
        translator.translate(inputWithSurrogatePair, writer);
        result = writer.toString();
        assertEquals("\uD800\uDC00", result);

        // Testing exception handling
        assertThrows(NullPointerException.class, () -> translator.translate(null, writer));
    }

    @Test
    public void testWithMethod() {
        CharSequenceTranslator translator1 = mock(CharSequenceTranslator.class);
        CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);

        CharSequenceTranslator result = new CharSequenceTranslator() {
            @Override
            public CharSequenceTranslator[] getTranslators() {
                return new CharSequenceTranslator[0];
            }
        }.with(translator1, translator2);

        assertNotNull(result);
        assertEquals(3, result.getTranslators().length);
        assertEquals(translator1, result.getTranslators()[0]);
        assertEquals(translator2, result.getTranslators()[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithMethodWithNull() {
        new CharSequenceTranslator() {
            @Override
            public CharSequenceTranslator[] getTranslators() {
                return new CharSequenceTranslator[0];
            }
        }.with(null);
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index < 0 || index >= input.length()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.charCount(c);
    }
}