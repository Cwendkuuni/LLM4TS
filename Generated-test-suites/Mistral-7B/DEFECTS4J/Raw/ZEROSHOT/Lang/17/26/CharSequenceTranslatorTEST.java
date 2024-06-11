package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.CharSequenceTranslator.hex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;

import org.junit.Test;

/**
 * Test class for {@link CharSequenceTranslator}
 */
public class CharSequenceTranslatorTest {

    private static final CharSequenceTranslator IDENTITY = new IdentityTranslator();
    private static final CharSequenceTranslator HEX_TRANSLATOR = new HexTranslator();

    /**
     * Test translate method with {@link IdentityTranslator}
     */
    @Test
    public void testTranslateIdentity() {
        final String input = "test input";
        String output = IDENTITY.translate(input);
        assertEquals(input, output);
    }

    /**
     * Test translate method with {@link HexTranslator}
     */
    @Test
    public void testTranslateHex() {
        final String input = "test input";
        String output = HEX_TRANSLATOR.translate(input);
        String expected = "";
        for (int i = 0; i < input.length(); i++) {
            expected += hex(Character.codePointAt(input, i));
        }
        assertEquals(expected, output);
    }

    /**
     * Test translate method with {@link IdentityTranslator} and {@link HexTranslator}
     */
    @Test
    public void testTranslateAggregate() {
        CharSequenceTranslator translator = new IdentityTranslator().with(new HexTranslator());
        final String input = "test input";
        String output = translator.translate(input);
        String expected = "";
        for (int i = 0; i < input.length(); i++) {
            expected += hex(Character.codePointAt(input, i));
        }
        assertEquals(expected, output);
    }

    /**
     * Test translate method with StringWriter
     */
    @Test
    public void testTranslateStringWriter() throws IOException {
        final String input = "test input";
        String output = CharSequenceUtils.toString(new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence cs, int start, Writer writer) throws IOException {
                for (int index = start; Character.isDefined(cs.charAt(index)); index++) {
                    writer.write(Character.toChars(Character.codePointAt(cs, index)));
                }
                return Character.isDefined(cs.charAt(start)) ? Character.charCount(Character.codePointAt(cs, start)) : 0;
            }
        }.translate(input));
        String expected = "";
        for (int i = 0; i < input.length(); i++) {
            expected += hex(Character.codePointAt(input, i));
        }
        assertEquals(expected, output);
    }

    /**
     * Test translate method with OutputStreamWriter
     */
    @Test
    public void testTranslateOutputStreamWriter() throws IOException {
        final String input = "test input";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteArrayOutputStream);
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence cs, int start, Writer writer) throws IOException {
                for (int index = start; Character.isDefined(cs.charAt(index)); index++) {
                    writer.write(Character.toChars(Character.codePointAt(cs, index)));
                }
                return Character.isDefined(cs.charAt(start)) ? Character.charCount(Character.codePointAt(cs, start)) : 0;
            }
        };
        translator.translate(input, 0, writer);
        String output = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        String expected = "";
        for (int i = 0; i < input.length(); i++) {
            expected += hex(Character.codePointAt(input, i));
        }
        assertEquals(expected, output);
    }
}

class HexTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (Character.isDefined(input.charAt(index))) {
            out.write(Character.toChars(Character.codePointAt(input, index)));
            out.write(Character.toChars('%'));
            out.write(Character.toChars('x'));
            out.write(Character.toChars(hex(Character.codePointAt(input, index++))
                    .charAt(0)));
            out.write(Character.toChars(hex(Character.codePointAt(input, index++))
                    .charAt(0)));
            return 1;
        }
        return 0;
    }
}