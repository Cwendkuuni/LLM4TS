package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorConstructor() {
        CharSequence[][] lookup = {new CharSequence[]{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(6, translator.longest);
    }

    @Test
    public void testTranslateBasic() throws IOException {
        CharSequence[][] lookup = {new CharSequence[]{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();

        String input = "helloworld";
        int index = 0;
        translator.translate(new StringCharSequence(input), index, out);

        assertEquals("hiworld", out.toString());
    }

    @Test
    public void testTranslateEmptyString() throws IOException {
        CharSequence[][] lookup = {new CharSequence[]{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        String input = "";
        int index = 0;
        translator.translate(new StringCharSequence(input), index, out);

        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateOutsideLookupTable() throws IOException {
        CharSequence[][] lookup = {new CharSequence[]{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        String input = "bye";
        int index = 0;
        translator.translate(new StringCharSequence(input), index, out);

        assertEquals("", out.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequence[][] lookup = {new CharSequence[]{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "helloworld";
        int index = 0;

        translator.translate(new StringCharSequence(input), index, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithNegativeIndex() throws IOException {
        CharSequence[][] lookup = {new CharSequence[]{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        String input = "helloworld";
        int index = -1;
        translator.translate(new StringCharSequence(input), index, out);
    }
}