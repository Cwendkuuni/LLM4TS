package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Category(org.apache.commons.lang3.text.translate.TranslatorTest.class)
public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;
    private CharSequenceTranslator escaper;
    private CharSequenceTranslator unescaper;

    @Before
    public void setUp() {
        escaper = new TranslateEscaper();
        unescaper = new TranslateUnescaper();
        translator = escaper.with(unescaper);
    }

    @Test
    public void testTranslateSimple() {
        CharSequence input = "Hello World!";
        String expected = "Hello%20World!";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateEmpty() {
        String actual = translator.translate(CharSequenceUtils.EMPTY);
        assertNull(actual);
    }

    @Test
    public void testTranslateNull() {
        assertNull(translator.translate(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        translator.translate(CharSequenceUtils.EMPTY, null);
    }

    @Test
    public void testTranslateSurrogatePair() {
        String input = "\uD800\uDC00";
        String expected = "\uD800\uDC0D";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithAggregate() {
        CharSequenceTranslator translatorWithEscaper = new TranslateEscaper().with(new TranslateUnescaper());
        String input = "Hello World! & < >";
        String expected = "Hello%20World%21%20%26%20%3C%20%3E";
        String actual = translatorWithEscaper.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithAggregateSurrogate() {
        CharSequenceTranslator translatorWithEscaper = new TranslateEscaper().with(new TranslateUnescaper());
        String input = "\uD800\uDC00 & < >";
        String expected = "\uD800\uDC01\uD800\uDC0D % %3C%3E";
        String actual = translatorWithEscaper.translate(input);
        assertEquals(expected, actual);
    }
}

class TranslateEscaper extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        switch (c) {
            case ' ':
                out.write("+");
                break;
            case '&':
                out.write("%26");
                break;
            case '<':
                out.write("%3C");
                break;
            case '>':
                out.write("%3E");
                break;
            default:
                out.write(c);
                break;
        }
        return Character.charCount(c);
    }
}

class TranslateUnescaper extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        switch (c) {
            case '+':
                out.write(' ');
                break;
            case '%':
                out.write('%');
                index++;
                char hex1 = Character.codePointAt(input, index);
                switch (hex1) {
                    case '2':
                        out.write('&');
                        index++;
                        char hex2 = Character.codePointAt(input, index);
                        switch (hex2) {
                            case '6':
                                out.write('A');
                                break;
                            case '2':
                                out.write('B');
                                break;
                            case '3':
                                out.write('C');
                                break;
                            case '4':
                                out.write('D');
                                break;
                            case '5':
                                out.write('E');
                                break;
                            case '6':
                                out.write('F');
                                break;
                            default:
                                out.write(hex2);
                                break;
                        }
                        break;
                    default:
                        out.write(hex1);
                        break;
                }
                break;
            case '%':
                index++;
                char hex1 = Character.codePointAt(input, index);
                switch (hex1) {
                    case '3':
                        out.write('<');
                        break;
                    case '2':
                        out.write('>');
                        break;
                    default:
                        out.write(hex1);
                        break;
                }
                break;
            default:
                out.write(c);
                break;
        }
        return 1;
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;
        for (CharSequenceTranslator translator : translators) {
            consumed += translator.translate(input, index, out);
            if (consumed >= input.length()) {
                break;
            }
            index += consumed;
        }
        return consumed;
    }
}