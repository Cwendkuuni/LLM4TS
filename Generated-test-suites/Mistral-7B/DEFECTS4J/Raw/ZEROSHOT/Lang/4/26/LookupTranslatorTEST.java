package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        assertEquals("", translator.translate(CharSequence.class.cast(StringUtils.EMPTY), 0, new Writer() {
            @Override
            public void write(int c, Writer writer) throws IOException {
                writer.write(c);
            }

            @Override
            public void write(char[] c, int offset, int length) throws IOException {
                writer.write(new String(c, offset, length));
            }

            @Override
            public void flush() throws IOException {
                // Not used in this test
            }

            @Override
            public void close() throws IOException {
                // Not used in this test
            }
        }));
    }

    @Test
    public void testSingleValueLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"A", "Z"}});
        assertEquals("Z", translator.translate(CharSequence.class.cast("A"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertNull(translator.translate(CharSequence.class.cast("B"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testMultipleValuesLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"A", "Z"}, {"B", "Y"}, {"C", "X"}});
        assertEquals("X", translator.translate(CharSequence.class.cast("C"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("Z", translator.translate(CharSequence.class.cast("A"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("Y", translator.translate(CharSequence.class.cast("B"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertNull(translator.translate(CharSequence.class.cast("D"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testLookupTableWithVariantLengthEntries() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}, {"AB", "YY"}, {"BA", "XX"}});
        assertEquals("XX", translator.translate(CharSequence.class.cast("BA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("ZZ", translator.translate(CharSequence.class.cast("AA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("YY", translator.translate(CharSequence.class.cast("AB"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertNull(translator.translate(CharSequence.class.cast("CC"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"A", "Z"}});
        translator.translate(null, 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        });
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"A", "Z"}});
        translator.translate(CharSequence.class.cast("AB"), 1, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        });
    }

    @Test
    public void testEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"A", "Z"}});
        assertEquals("", translator.translate(CharSequence.class.cast(StringUtils.EMPTY), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testInputLongerThanLookupValue() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}});
        assertEquals("ZZ", translator.translate(CharSequence.class.cast("AAA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testInputShorterThanLookupValue() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}});
        assertEquals("", translator.translate(CharSequence.class.cast("A"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testCompositeInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}, {"AB", "YY"}});
        assertEquals("YY", translator.translate(CharSequence.class.cast("ABAA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testSubsequencesOverlap() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}, {"A", "Y"}});
        assertEquals("Y", translator.translate(CharSequence.class.cast("A"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("ZZ", translator.translate(CharSequence.class.cast("AA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("Y", translator.translate(CharSequence.class.cast("A"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("ZZ", translator.translate(CharSequence.class.cast("AA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("", translator.translate(CharSequence.class.cast("A"), 1, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testSubsequencesDoNotOverlap() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}, {"AB", "YY"}});
        assertEquals("YY", translator.translate(CharSequence.class.cast("AB"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("", translator.translate(CharSequence.class.cast("AA"), 1, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testInputContainsMultipleOccurrencesOfSameSubsequence() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}, {"AB", "YY"}});
        assertEquals("YY", translator.translate(CharSequence.class.cast("ABAB"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testInputContainsMultipleSubsequences() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}, {"AB", "YY"}});
        assertEquals("YY", translator.translate(CharSequence.class.cast("ABABAA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("ZZ", translator.translate(CharSequence.class.cast("AAA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testInputContainsMultipleSubsequencesWithOverlap() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}, {"AB", "YY"}});
        assertEquals("YY", translator.translate(CharSequence.class.cast("ABABAA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("ZZ", translator.translate(CharSequence.class.cast("AAA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("", translator.translate(CharSequence.class.cast("A"), 1, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }

    @Test
    public void testInputContainsMultipleSubsequencesWithNoOverlap() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"AA", "ZZ"}, {"AB", "YY"}});
        assertEquals("YY", translator.translate(CharSequence.class.cast("ABABAA"), 0, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
        assertEquals("", translator.translate(CharSequence.class.cast("AAA"), 1, new Writer() {
            // Writer implementation is the same as in testEmptyLookupTable()
        }));
    }
}