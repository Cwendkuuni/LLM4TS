import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Mock
    private Writer mockWriter;

    private CharSequenceTranslator translator;

    public CharSequenceTranslatorTest() {
        MockitoAnnotations.initMocks(this);
        translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
    }

    @Test
    public void testTranslate_WhenInputIsNull_ShouldReturnNull() {
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_WhenInputIsEmpty_ShouldReturnEmptyString() {
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslate_WhenInputHasCharacters_ShouldReturnTranslatedString() throws IOException {
        StringWriter writer = new StringWriter();
        translator.translate("hello", writer);
        assertEquals("hello", writer.toString());
    }

    @Test
    public void testTranslate_WhenInputHasSpecialCharacters_ShouldReturnTranslatedString() throws IOException {
        StringWriter writer = new StringWriter();
        translator.translate("<html>", writer);
        assertEquals("<html>", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_WhenWriterIsNull_ShouldThrowIllegalArgumentException() throws IOException {
        translator.translate("hello", null);
    }

    @Test(expected = RuntimeException.class)
    public void testTranslate_WhenWriterThrowsIOException_ShouldThrowRuntimeException() throws IOException {
        Writer throwingWriter = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {
                throw new IOException();
            }

            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        translator.translate("hello", throwingWriter);
    }

    @Test
    public void testWith_WhenTranslatorsAreProvided_ShouldCreateAggregateTranslator() {
        CharSequenceTranslator otherTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        CharSequenceTranslator mergedTranslator = translator.with(otherTranslator);
        assertTrue(mergedTranslator instanceof AggregateTranslator);
        assertEquals(2, ((AggregateTranslator) mergedTranslator).getTranslators().length);
        assertSame(translator, ((AggregateTranslator) mergedTranslator).getTranslators()[0]);
        assertSame(otherTranslator, ((AggregateTranslator) mergedTranslator).getTranslators()[1]);
    }

    @Test
    public void testHex_WhenCodepointIsValid_ShouldReturnUpperCaseHexString() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("7A", CharSequenceTranslator.hex(122));
        assertEquals("C5", CharSequenceTranslator.hex(197));
    }

    @Test
    public void testHex_WhenCodepointIsInvalid_ShouldReturnUpperCaseHexString() {
        assertEquals("0", CharSequenceTranslator.hex(0));
        assertEquals("1B", CharSequenceTranslator.hex(27));
        assertEquals("FF", CharSequenceTranslator.hex(255));
    }
}