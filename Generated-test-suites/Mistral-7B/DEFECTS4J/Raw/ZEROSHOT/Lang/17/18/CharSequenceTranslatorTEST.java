package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.CharSequenceTranslator.hex;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorFactory;
import org.junit.Test;

/**
 * Test class for {@link CharSequenceTranslator}
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = createHtml4Escaper();
        String input = "example <> & \t";
        StringWriter output = new StringWriter();
        int index = 0;
        int len = input.length();

        int consumedChars;
        while (index < len) {
            consumedChars = translator.translate(CharSequenceUtils.subsequence(input, index), index, output);
            index += consumedChars;
        }

        String actual = output.toString();
        String expected = "example &lt; &gt; &amp; &t;";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateString() throws Exception {
        CharSequenceTranslator translator = createHtml4Escaper();
        String input = "example <> & \t";
        String actual = translator.translate(input);
        String expected = "example &lt; &gt; &amp; &t;";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with an empty input.
     */
    @Test
    public void testTranslateEmptyInput() throws Exception {
        CharSequenceTranslator translator = createIdentityTranslator();
        StringWriter output = new StringWriter();
        CharSequence input = "";
        translator.translate(input, output);
        assertEquals("", output.toString());
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with a null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() throws Exception {
        CharSequenceTranslator translator = createIdentityTranslator();
        StringWriter output = new StringWriter();
        translator.translate(null, output);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with a null writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws Exception {
        CharSequenceTranslator translator = createHtml4Escaper();
        CharSequence input = "example <> & \t";
        translator.translate(input, null);
    }

    /**
     * Test for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method.
     */
    @Test
    public void testWithAggregateTranslator() throws Exception {
        CharSequenceTranslator htmlEscaper = createHtml4Escaper();
        CharSequenceTranslator urlDecoder = createUrlDecoder();

        CharSequenceTranslator aggregatedTranslator = htmlEscaper.with(urlDecoder);
        CharSequence input = "%3Cexample%3E%3Cscript%3Ealert(%27Hello%27)%3C/script%3E";
        String actual = aggregatedTranslator.translate(input);
        String expected = "&lt;example&gt;&lt;script&gt;alert(&#39;Hello&#39;)&lt;/script&gt;";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#hex(int)} method.
     */
    @Test
    public void testHex() {
        assertEquals("41", hex('A'));
        assertEquals("26", hex('&'));
        assertEquals("09", hex('\t'));
    }

    /**
     * Factory method to create a new instance of {@link IdentityTranslator}.
     *
     * @return a new instance of {@link IdentityTranslator}
     */
    private static CharSequenceTranslator createIdentityTranslator() {
        return CharSequenceTranslatorFactory.IDENTITY;
    }

    /**
     * Factory method to create a new instance of {@link Html4Escaper}.
     *
     * @return a new instance of {@link Html4Escaper}
     */
    private static CharSequenceTranslator createHtml4Escaper() {
        return CharSequenceTranslatorFactory.HTML4;
    }

    /**
     * Factory method to create a new instance of {@link UrlDecoder}.
     *
     * @return a new instance of {@link UrlDecoder}
     */
    private static CharSequenceTranslator createUrlDecoder() {
        return CharSequenceTranslatorFactory.URL_DECODE;
    }

    /**
     * Identity translator. This class does not change the input text.
     */
    private static class IdentityTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isHighSurrogate(c) && index < input.length() - 1) {
                char d = Character.codePointAt(input, index + 1);
                if (Character.isLowSurrogate(d)) {
                    out.write((char) (0xD800 | ((0x03FF & (c - 0xD800)) << 10) | (0xDC00 & d)));
                    index += 2;
                    return 2;
                }
            }
            out.write(c);
            return 1;
        }
    }

    /**
     * Aggregate translator that merges multiple translators.
     */
    private static class AggregateTranslator extends CharSequenceTranslator {

        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            for (CharSequenceTranslator translator : translators) {
                int consumedChars = translator.translate(input, index, out);
                if (consumedChars > 0) {
                    index += consumedChars;
                } else if (consumedChars == 0) {
                    char c = Character.codePointAt(input, index);
                    out.write(c);
                    index++;
                }
                if (index >= input.length()) {
                    break;
                }
            }
            return index - Character.codePointCount(input, 0, index);
        }
    }
}