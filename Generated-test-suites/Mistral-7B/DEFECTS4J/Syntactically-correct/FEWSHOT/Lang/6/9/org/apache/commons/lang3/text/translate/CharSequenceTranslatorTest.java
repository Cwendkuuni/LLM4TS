package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    private static final CharSequenceTranslator TRANSLATOR = new ASCIIStringTranslator();

    @Test
    public void testTranslate() {
        String input = "foo bar baz";
        String expected = "foo%20bar%20baz";
        String result = TRANSLATOR.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        String input = "foo bar baz";
        Writer writer = new StringWriter();
        TRANSLATOR.translate(input, writer);
        String expected = "foo%20bar%20baz";
        String result = writer.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateNullInput() {
        assertNull(TRANSLATOR.translate(null));
    }

    @Test
    public void testTranslateWithWriterNull() throws IOException {
        String input = "foo bar baz";
        assertNull(TRANSLATOR.translate(input, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNegativeIndex() throws IOException {
        String input = "foo bar baz";
        Writer out = new StringWriter();
        TRANSLATOR.translate(input, -5, out);
    }

    @Test
    public void testTranslateAggregate() throws IOException {
        CharSequenceTranslator translator1 = new ASCIIStringTranslator();
        CharSequenceTranslator translator2 = new HTMLStringTranslator();
        CharSequenceTranslator aggregated = translator1.with(translator2);
        String input = "<html><body>Hello, World!</body></html>";
        Writer out = new StringWriter();
        aggregated.translate(input, out);
        String expected = "&lt;html&gt;&amp;lt;body&gt;Hello,%20World%21&amp;gt;";
        String result = out.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithSurrogatePair() throws IOException {
        String input = "𠮷";
        Writer out = new StringWriter();
        TRANSLATOR.translate(input, out);
        String expected = "%uD840%uDC07";
        String result = out.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithAggregateAndSurrogatePair() throws IOException {
        CharSequenceTranslator translator1 = new ASCIIStringTranslator();
        CharSequenceTranslator translator2 = new HTMLStringTranslator();
        CharSequenceTranslator aggregated = translator1.with(translator2);
        String input = "𠮷";
        Writer out = new StringWriter();
        aggregated.translate(input, out);
        String expected = "&#x%uD840%uDC07;";
        String result = out.toString();
        assertEquals(expected, result);
    }

}

class ASCIIStringTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null || index >= input.length()) {
            return 0;
        }
        char c = Character.toChars(Character.codePointAt(input, index))[0];
        out.write(c);
        return 1;
    }

}

class HTMLStringTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null || index >= input.length()) {
            return 0;
        }
        char c = Character.toChars(Character.codePointAt(input, index))[0];
        switch (c) {
            case '<':
                out.write("&lt;");
                break;
            case '>':
                out.write("&gt;");
                break;
            case '&':
                out.write("&amp;");
                break;
            default:
                out.write(c);
        }
        return 1;
    }

}

class AggregateTranslator extends CharSequenceTranslator {

    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator... translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int totalConsumed = 0;
        for (CharSequenceTranslator translator : translators) {
            int consumed = translator.translate(input, index, out);
            if (consumed > 0) {
                totalConsumed += consumed;
                index += consumed;
            }
        }
        return totalConsumed;
    }

}