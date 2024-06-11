
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_NullInput_ReturnsNull() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        String result = translator.translate(null);

        assertNull(result);
    }

    @Test
    public void testTranslate_EmptyInput_ReturnsEmptyString() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        String result = translator.translate("");

        assertEquals("", result);
    }

    @Test
    public void testTranslate_OutputWriterNull_ThrowsIllegalArgumentException() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        try {
            translator.translate("test", null);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testTranslate_ExceptionInTranslateMethod_ThrowsRuntimeException() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        try {
            translator.translate("test");
            fail("Expected RuntimeException to be thrown.");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testTranslate_TranslateMethodCalledOnce() throws IOException {
        final StringBuilder outBuilder = new StringBuilder();

        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("abc");
                return 1;
            }
        };

        translator.translate("test", new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                outBuilder.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        });

        assertEquals("abc", outBuilder.toString());
    }

    @Test
    public void testWith_AddsTranslatorToArray() {
        CharSequenceTranslator initialTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        CharSequenceTranslator additionalTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        CharSequenceTranslator mergedTranslator = initialTranslator.with(additionalTranslator);

        CharSequenceTranslator[] expectedArray = new CharSequenceTranslator[]{initialTranslator, additionalTranslator};
        assertArrayEquals(expectedArray, mergedTranslator);
    }

    @Test
    public void testHex_ReturnsUpperCaseHexString() {
        int codepoint = 123;

        String result = CharSequenceTranslator.hex(codepoint);

        assertEquals("7B", result);
    }
}

