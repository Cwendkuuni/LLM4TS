package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * Test class for LookupTranslator
 */
public class LookupTranslatorTest {

    /**
     * Test constructor with empty lookup table
     */
    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        assertEquals("", translator.translate(CharSequenceUtils.EMPTY, 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
    }

    /**
     * Test constructor with non-empty lookup table
     */
    @Test
    public void testNonEmptyLookupTable() {
        CharSequence[][] lookup = {{"one", "uno"}, {"two", "dos"}, {"three", "tres"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("uno", translator.translate(CharSequenceUtils.wrap("one"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertEquals("dos", translator.translate(CharSequenceUtils.wrap("two"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertEquals("tres", translator.translate(CharSequenceUtils.wrap("three"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertNull(translator.translate(CharSequenceUtils.wrap("four"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
    }

    /**
     * Test translate method with input length smaller than shortest key
     */
    @Test
    public void testTranslateInputTooShort() {
        CharSequence[][] lookup = {{"one", "uno"}, {"two", "dos"}, {"three", "tres"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("", translator.translate(CharSequenceUtils.wrap(""), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertEquals("", translator.translate(CharSequenceUtils.wrap("short"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
    }

    /**
     * Test translate method with input length equal to shortest key
     */
    @Test
    public void testTranslateInputShortestKey() {
        CharSequence[][] lookup = {{"one", "uno"}, {"two", "dos"}, {"three", "tres"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("uno", translator.translate(CharSequenceUtils.wrap("one"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertEquals("dos", translator.translate(CharSequenceUtils.wrap("two"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertEquals("tres", translator.translate(CharSequenceUtils.wrap("three"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
    }

    /**
     * Test translate method with input length equal to longest key
     */
    @Test
    public void testTranslateInputLongestKey() {
        CharSequence[][] lookup = {{"one", "uno"}, {"two", "dos"}, {"three", "tres"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("uno", translator.translate(CharSequenceUtils.wrap("one"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertEquals("dos", translator.translate(CharSequenceUtils.wrap("too"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertEquals("tres", translator.translate(CharSequenceUtils.wrap("three"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertEquals("", translator.translate(CharSequenceUtils.wrap("four"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
    }

    /**
     * Test translate method with input length greater than longest key
     */
    @Test
    public void testTranslateInputLongerKey() {
        CharSequence[][] lookup = {{"one", "uno"}, {"two", "dos"}, {"three", "tres"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("", translator.translate(CharSequenceUtils.wrap("onetwo"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
        assertEquals("", translator.translate(CharSequenceUtils.wrap("threetwoone"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
    }

    /**
     * Test translate method with input containing multiple keys
     */
    @Test
    public void testTranslateInputMultipleKeys() {
        CharSequence[][] lookup = {{"one", "uno"}, {"two", "dos"}, {"three", "tres"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("dosuno", translator.translate(CharSequenceUtils.wrap("twone"), 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
    }

    /**
     * Test constructor with null lookup table
     */
    @Test(expected = NullPointerException.class)
    public void testNullLookupTable() {
        new LookupTranslator(null);
    }

    /**
     * Test constructor with empty CharSequence[]
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharSequenceArray() {
        new LookupTranslator(new CharSequence[][] {new CharSequence[0]});
    }

    /**
     * Test constructor with non-2D CharSequence[]
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNon2DCharSequenceArray() {
        new LookupTranslator(new CharSequence[][] {{"one", "uno"}});
    }
}