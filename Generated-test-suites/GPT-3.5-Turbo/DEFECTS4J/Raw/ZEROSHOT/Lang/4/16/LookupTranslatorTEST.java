package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequence[] lookup = {{"a", "1"}, {"b", "2"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "abc";
        StringBuilder output = new StringBuilder();
        assertEquals(0, translator.translate(input, 0, output));
        assertEquals("abc", output.toString());
    }

    @Test
    public void testSingleLookup() throws Exception {
        CharSequence[] lookup = {{"a", "1"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "abc";
        StringBuilder output = new StringBuilder();
        assertEquals(1, translator.translate(input, 0, output));
        assertEquals("1", output.toString());
    }

    @Test
    public void testMultipleLookup() throws Exception {
        CharSequence[] lookup = {{"a", "1"}, {"b", "2"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "abc";
        StringBuilder output = new StringBuilder();
        assertEquals(1, translator.translate(input, 0, output));
        assertEquals("1bc", output.toString());
    }

    @Test
    public void testEmptyLookup() throws Exception {
        CharSequence[][] lookup = {};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "abc";
        StringBuilder output = new StringBuilder();
        assertEquals(0, translator.translate(input, 0, output));
        assertEquals("abc", output.toString());
    }

    @Test
    public void testTranslateWithIndex() throws Exception {
        CharSequence[] lookup = {{"a", "1"}, {"b", "2"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "abc";
        StringBuilder output = new StringBuilder();
        assertEquals(2, translator.translate(input, 1, output));
        assertEquals("2c", output.toString());
    }

    @Test
    public void testTranslateWithWriter() throws Exception {
        CharSequence[] lookup = {{"a", "1"}, {"b", "2"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "abc";
        StringWriter writer = new StringWriter();
        assertEquals(0, translator.translate(input, 0, writer));
        assertEquals("abc", writer.toString());
    }

}