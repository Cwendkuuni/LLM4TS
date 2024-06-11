package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        String result = translator.translate("Hello");
        assert(result.equals("Hello"));
    }

    @Test
    public void testTranslate_NullInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        String result = translator.translate(null);
        assert(result == null);
    }

    @Test
    public void testTranslateToWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.toString());
                return input.length();
            }
        };
        
        StringWriter writer = new StringWriter();
        translator.translate("Hello", writer);
        String result = writer.toString();
        assert(result.equals("Hello"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateToWriter_NullWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        translator.translate("Hello", null);
    }

    @Test
    public void testWith() throws IOException {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assert(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        String hex = CharSequenceTranslator.hex(65);
        assert(hex.equals("41"));
    }
}

