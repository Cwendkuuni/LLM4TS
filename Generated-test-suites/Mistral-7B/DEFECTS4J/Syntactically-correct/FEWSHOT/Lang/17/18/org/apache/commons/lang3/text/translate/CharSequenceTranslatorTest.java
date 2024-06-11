package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.HexTranslator;

public class CharSequenceTranslatorTest {

    @Test
    public void testHexTranslator() {
        CharSequenceTranslator translator = new HexTranslator();
        String input = "abcdef";
        String expected = "41424344454647";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator identityTranslator = (cs, out) -> {};
        CharSequenceTranslator hexTranslator = new HexTranslator();

        CharSequenceTranslator aggregateTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                int consumed = identityTranslator.translate(input, index, out);
                if (consumed == 0) {
                    consumed = hexTranslator.translate(input, index, out);
                }
                return consumed;
            }
        };

        String input = "abcdef";
        String expected = "41424344454647abcdef";
        String output = aggregateTranslator.translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testAggregateTranslatorWithNullTranslator() {
        CharSequenceTranslator identityTranslator = (cs, out) -> {};
        CharSequenceTranslator nullTranslator = null;

        CharSequenceTranslator aggregateTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                int consumed = identityTranslator.translate(input, index, out);
                if (consumed == 0) {
                    if (nullTranslator != null) {
                        consumed = nullTranslator.translate(input, index, out);
                    }
                }
                return consumed;
            }
        };

        String input = "abcdef";
        String expected = "abcdef";
        String output = aggregateTranslator.translate(input);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAggregateTranslatorWithNullWriter() {
        CharSequenceTranslator identityTranslator = (cs, out) -> {};
        CharSequenceTranslator hexTranslator = new HexTranslator();

        CharSequenceTranslator aggregateTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                int consumed = identityTranslator.translate(input, index, out);
                if (consumed == 0) {
                    consumed = hexTranslator.translate(input, index, out);
                }
                return consumed;
            }
        };

        String input = "abcdef";
        Writer out = null;
        aggregateTranslator.translate(input, 0, out);
    }

    @Test
    public void testTranslateNullInput() {
        String output = new CharSequenceTranslator().translate(null);
        assertNull(output);
    }

    @Test
    public void testTranslateEmptyInput() {
        String output = new CharSequenceTranslator().translate("");
        assertEquals("", output);
    }

    @Test
    public void testTranslateSimpleInput() {
        String input = "abc";
        String expected = "abc";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithLeadingAndTrailingHyphens() {
        String input = "--abc--";
        String expected = "abc";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithLeadingHyphen() {
        String input = "--abc";
        String expected = "abc";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithTrailingHyphen() {
        String input = "abc--";
        String expected = "abc";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithLeadingAndTrailingHyphensAndCodepoints() {
        String input = "--\u00E9abc--";
        String expected = "\u00E9abc";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithLeadingSurrogatePair() {
        String input = "⃝abc";
        String expected = "abc";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithTrailingSurrogatePair() {
        String input = "abc⃜";
        String expected = "abc";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithLeadingAndTrailingSurrogatePairs() {
        String input = "⃝⃜abc⃜⃝";
        String expected = "abc";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithCodepoints() {
        String input = "ab\u0304cd\u0308ef";
        String expected = "ab\u0304cd\u0308ef";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithSurrogatePairAfterCodepoint() {
        String input = "ab\u0304cd\uD800\uDC04ef";
        String expected = "ab\u0304cd\uD800\uDC04ef";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithSurrogatePairBeforeCodepoint() {
        String input = "\uD800\uDC04ab\u0304cdef";
        String expected = "\uD800\uDC04ab\u0304cdef";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithSurrogatePairInMiddle() {
        String input = "\uD800\uDC04ab\u0304cd\uD800\uDC04ef";
        String expected = "\uD800\uDC04ab\u0304cd\uD800\uDC04ef";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithSurrogatePairAndCodepoints() {
        String input = "\uD800\uDC04ab\u0304cd\u0308ef\uD800\uDC04";
        String expected = "\uD800\uDC04ab\u0304cd\u0308ef\uD800\uDC04";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithMultipleSurrogatePairs() {
        String input = "\uD800\uDC04\uD801\uDC06\uD802\uDC08abcd\uD803\uDC0Aef\uD804\uDC0C";
        String expected = "\uD800\uDC04\uD801\uDC06\uD802\uDC08abcd\uD803\uDC0Aef\uD804\uDC0C";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithSurrogatePairAndCodepointAtEnd() {
        String input = "\uD800\uDC04abcd\uD800\uDC04";
        String expected = "\uD800\uDC04abcd\uD800\uDC04";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithSurrogatePairAndCodepointInMiddle() {
        String input = "\uD800\uDC04ab\u0304cd\uD800\uDC04ef";
        String expected = "\uD800\uDC04ab\u0304cd\uD800\uDC04ef";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithSurrogatePairAndCodepointAtBeginning() {
        String input = "\uD800\uDC04abcd\uD800\uDC04";
        String expected = "\uD800\uDC04abcd\uD800\uDC04";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateInputWithSurrogatePairAndCodepointsAtBeginningAndEnd() {
        String input = "\uD800\uDC04ab\u0304cd\uD800\uDC04ef\uD800\uDC04";
        String expected = "\uD800\uDC04ab\u0304cd\uD800\uDC04ef\uD800\uDC04";
        String output = new CharSequenceTranslator().translate(input);
        assertEquals(expected, output);
    }
}