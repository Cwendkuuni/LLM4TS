import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    private class MockTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }

    @Test
    public void testTranslate_InputIsNull_ReturnsNull() {
        CharSequenceTranslator translator = new MockTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_InputIsEmpty_ReturnsEmptyString() {
        CharSequenceTranslator translator = new MockTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslate_InputHasNoTranslation_ReturnsInputString() {
        CharSequenceTranslator translator = new MockTranslator();
        assertEquals("abc123", translator.translate("abc123"));
    }

    @Test
    public void testTranslate_InputHasTranslation_ReturnsTranslatedString() {
        CharSequenceTranslator translator = new MockTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("*");
                return 1;
            }
        };
        assertEquals("***", translator.translate("abc123"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_OutputWriterIsNull_ThrowsIllegalArgumentException() throws IOException {
        CharSequenceTranslator translator = new MockTranslator();
        translator.translate("abc123", null);
    }

    @Test
    public void testTranslate_OutputWriterIsNotNull_TranslatesInputToOutputWriter() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequenceTranslator translator = new MockTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("*");
                return 1;
            }
        };
        translator.translate("abc123", writer);
        assertEquals("***", writer.toString());
    }

    @Test
    public void testWith_NoTranslators_ReturnsSelf() {
        CharSequenceTranslator translator = new MockTranslator();
        assertSame(translator, translator.with());
    }

    @Test
    public void testWith_SingleTranslator_ReturnsMergedTranslator() {
        CharSequenceTranslator[] translators = {new MockTranslator()};
        CharSequenceTranslator translator = new MockTranslator().with(translators);
        assertTrue(translator instanceof AggregateTranslator);
    }

    @Test
    public void testWith_MultipleTranslators_ReturnsMergedTranslator() {
        CharSequenceTranslator[] translators = {new MockTranslator(), new MockTranslator()};
        CharSequenceTranslator translator = new MockTranslator().with(translators);
        assertTrue(translator instanceof AggregateTranslator);
    }

    @Test
    public void testHex_LowercaseChar_ReturnsUppercaseHexString() {
        assertEquals("41", CharSequenceTranslator.hex(65));
    }

    @Test
    public void testHex_UppercaseChar_ReturnsUppercaseHexString() {
        assertEquals("42", CharSequenceTranslator.hex(66));
    }

    @Test
    public void testHex_NumberChar_ReturnsUppercaseHexString() {
        assertEquals("39", CharSequenceTranslator.hex(57));
    }

    @Test
    public void testHex_MaxInt_ReturnsUppercaseHexString() {
        assertEquals("7FFFFFFF", CharSequenceTranslator.hex(Integer.MAX_VALUE));
    }

}