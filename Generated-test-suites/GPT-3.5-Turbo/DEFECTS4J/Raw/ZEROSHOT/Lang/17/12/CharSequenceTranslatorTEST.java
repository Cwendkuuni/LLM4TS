import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_StringWriter_nullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_StringWriter_emptyInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslate_StringWriter_singleCharInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 1;
            }
        };

        assertEquals("a", translator.translate("a"));
    }

    @Test
    public void testTranslate_StringWriter_translateMultipleChars() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                } else if (input.charAt(index) == 'b') {
                    out.write('a');
                    return 1;
                }
                return 0;
            }
        };

        assertEquals("ba", translator.translate("ab"));
    }

    @Test
    public void testTranslate_StringWriter_translateInvalidChars() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) != 'a') {
                    out.write(input.charAt(index));
                    return 1;
                }
                return 0;
            }
        };

        assertEquals("b@!", translator.translate("ba@!"));
    }

    @Test
    public void testTranslate_Writer_nullInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_Writer_emptyInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        StringWriter writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_Writer_singleCharInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 1;
            }
        };

        StringWriter writer = new StringWriter();
        translator.translate("a", writer);
        assertEquals("a", writer.toString());
    }

    @Test
    public void testTranslate_Writer_translateMultipleChars() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                } else if (input.charAt(index) == 'b') {
                    out.write('a');
                    return 1;
                }
                return 0;
            }
        };

        StringWriter writer = new StringWriter();
        translator.translate("ab", writer);
        assertEquals("ba", writer.toString());
    }

    @Test
    public void testTranslate_Writer_translateInvalidChars() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) != 'a') {
                    out.write(input.charAt(index));
                    return 1;
                }
                return 0;
            }
        };

        StringWriter writer = new StringWriter();
        translator.translate("ba@!", writer);
        assertEquals("b@!", writer.toString());
    }

    @Test
    public void testWith_additionalTranslators() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator translator3 = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator mergedTranslator = translator1.with(translator2, translator3);

        assertNotNull(mergedTranslator);
        assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("1F", CharSequenceTranslator.hex(31));
        assertEquals("7E", CharSequenceTranslator.hex(126));
    }
}