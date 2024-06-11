package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method with no translator.
     */
    @Test
    public void testTranslateNoTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = "Hello, World!";
        String expected = "Hello, World!";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test for translate method with StringWriter.
     */
    @Test
    public void testTranslateStringWriter() {
        CharSequenceTranslator translator = new TranslateHtml();
        String input = "&lt;html&gt;&lt;body&gt;&lt;p&gt;Hello, World!&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
        String expected = "<html><body><p>Hello, World!</p></body></html>";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for translate method with custom translator.
     */
    @Test
    public void testTranslateCustomTranslator() {
        CharSequenceTranslator translator = new TranslateHex();
        String input = "Hello, World!";
        String expected = "48656C6C6F2C20576F726C6421";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for translate method with error scenario.
     */
    @Test(expected = RuntimeException.class)
    public void testTranslateErrorScenario() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Writer error");
            }
        };
        String input = "Hello, World!";
        try {
            translator.translate(input);
        } catch (CharSequenceTranslator.IOException e) {
            fail("Expected RuntimeException");
        }
    }

    /**
     * Test for with method.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new TranslateHtml();
        CharSequenceTranslator translator2 = new TranslateHex();
        CharSequenceTranslator translator = translator1.with(translator2);
        String input = "&lt;html&gt;&lt;body&gt;&lt;p&gt;Hello, World!&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expectedHtml = "<html><body><p>Hello, World!</p></body></html>";
        String expectedHex = "48656C6C6F2C20576F726C6421";
        String actual = writer.toString();
        String actualHtml = CharSequenceUtils.subsequence(actual, 0, expectedHtml.length());
        String actualHex = CharSequenceUtils.subsequence(actual, expectedHtml.length(), actual.length());
        assertEquals(expectedHtml, actualHtml);
        assertEquals(expectedHex, actualHex);
    }
}

class TranslateHtml extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        if (c == '&') {
            switch (Character.codePointAt(input, index + 1)) {
                case 'l':
                    out.write("<");
                    index++;
                    break;
                case 't':
                    out.write(">");
                    index++;
                    break;
                default:
                    out.write(c);
            }
            return 2;
        } else {
            out.write(c);
            return Character.charCount(c);
        }
    }
}

class TranslateHex extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int index2 = index + 1;
        int digit1 = Character.codePointAt(input, index);
        int digit2 = Character.codePointAt(input, index2);
        int value = Integer.parseInt(Character.toString(new char[]{digit1, digit2}), 16);
        out.write((char) value);
        index += 2;
        return Character.charCount(Character.codePointAt(input, index));
    }
}