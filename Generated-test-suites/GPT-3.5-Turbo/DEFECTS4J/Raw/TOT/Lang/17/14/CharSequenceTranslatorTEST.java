import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        String result = translator.translate(null);
        assertEquals(null, result);
    }

    @Test
    public void testTranslate_withEmptyStringInput_shouldReturnEmptyString() {
        String result = translator.translate("");
        assertEquals("", result);
    }

    @Test
    public void testTranslate_withValidInput_shouldReturnTranslatedString() {
        // Create a translator that translates 'a' to 'b'
        CharSequenceTranslator translateAToB = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                }
                return 0;
            }
        };

        translator = translateAToB;

        String result = translator.translate("abc");
        assertEquals("bbc", result);
    }

    @Test
    public void testTranslate_withIOException_shouldThrowRuntimeException() throws IOException {
        Writer mockWriter = mock(Writer.class);
        doThrow(new IOException()).when(mockWriter).write(anyChar());
        translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("test");
                return 0;
            }
        };
        try {
            translator.translate("abc", mockWriter);
        } catch (RuntimeException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testTranslate_withWriterNull_shouldThrowIllegalArgumentException() throws IOException {
        try {
            translator.translate("abc", null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        }
    }

    @Test
    public void testTranslate_withTranslationConsumingCodepoint_shouldConsumeCodepoint() {
        CharSequenceTranslator translateLowerToUpperCase = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (Character.isLowerCase(input.charAt(index))) {
                    out.write(Character.toUpperCase(input.charAt(index)));
                    return 1;
                }
                return 0;
            }
        };

        translator = translateLowerToUpperCase;

        String result = translator.translate("abc");
        assertEquals("ABC", result);
    }

    @Test
    public void testWith_withNoTranslators_shouldReturnOriginalTranslator() {
        CharSequenceTranslator result = translator.with();
        assertEquals(translator, result);
    }

    @Test
    public void testWith_withMultipleTranslators_shouldReturnMergedTranslator() {
        CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);
        CharSequenceTranslator result = translator.with(translator2);
        AggregateTranslator aggregateTranslator = new AggregateTranslator(translator, translator2);

        assertEquals(aggregateTranslator.getClass(), result.getClass());
    }

    @Test
    public void testHex_shouldReturnUpperCaseHexRepresentation() {
        String result = CharSequenceTranslator.hex(10);
        assertEquals("A", result);

        result = CharSequenceTranslator.hex(15);
        assertEquals("F", result);
    }

}