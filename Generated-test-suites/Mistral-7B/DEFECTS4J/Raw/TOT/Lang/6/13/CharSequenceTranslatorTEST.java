package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CharSequenceTranslator}.
 */
@RunWith(Parameterized.class)
public class CharSequenceTranslatorTest {

    private final CharSequenceTranslator translator;
    private final String input;
    private final String expectedOutput;

    public CharSequenceTranslatorTest(CharSequenceTranslator translator, String input, String expectedOutput) {
        this.translator = translator;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{index}: input={0} -> output={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new IdentityTranslator(), "Hello, World!", "Hello, World!"},
                {new ASCIIToHTMLTranslator(), "&amp;<special>&quot;", "&amp;amp; &lt;special&gt; &amp;quot;"},
                {new HTMLToASCIITranslator(), "&lt;script src=&quot;foo.js&quot;&gt;", "<script src=\"foo.js\">"},
                {new IdentityTranslator().with(new HTMLToASCIITranslator()), "<script src=&quot;foo.js&quot;>", "<script src=\"foo.js\">"},
                // Add more test cases here
        });
    }

    @Before
    public void setUp() {
        // Set up any state needed for tests here
    }

    @Test
    public void testTranslate() throws Exception {
        Writer out = new StringWriter();
        int consumed = translator.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);
        assertEquals(expectedOutput.length(), consumed);
        assertEquals(expectedOutput, out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        translator.translate(input, null);
    }

    @Test
    public void testIdentityTranslate() {
        assertEquals(expectedOutput, translator.translate(input));
    }

    static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                return 0;
            }
            char[] chars = Character.toChars(Character.codePointAt(input, index));
            out.write(chars);
            return chars.length;
        }
    }

    static class ASCIIToHTMLTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            switch (c) {
                case '&':
                    out.write("&amp;");
                    break;
                case '<':
                    out.write("&lt;");
                    break;
                case '>':
                    out.write("&gt;");
                    break;
                case '&quot;':
                    out.write("&quot;");
                    break;
                case '&amp;':
                    out.write("&amp;amp;");
                    break;
                default:
                    char[] chars = Character.toChars(c);
                    out.write(chars);
                    break;
            }
            return Character.charCount(c);
        }
    }

    static class HTMLToASCIITranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            switch (c) {
                case '&amp;':
                    out.write('&');
                    break;
                case '&lt;':
                    out.write('<');
                    break;
                case '&gt;':
                    out.write('>');
                    break;
                case '&quot;':
                    out.write('"');
                    break;
                default:
                    char[] chars = Character.toChars(c);
                    out.write(chars);
                    break;
            }
            return Character.charCount(c);
        }
    }

    static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            for (CharSequenceTranslator translator : translators) {
                int consumed = translator.translate(input, index, out);
                if (consumed > 0) {
                    return consumed;
                }
                index += consumed;
            }
            char c = Character.codePointAt(input, index);
            char[] chars = Character.toChars(c);
            out.write(chars);
            return chars.length;
        }
    }
}