package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setup() {
        translator = new BaseCharSequenceTranslator(); // Assuming BaseCharSequenceTranslator is a concrete implementation
    }

    @Test
    public void testTranslate() {
        String input = "foo bar baz qux quux";
        String expectedOutput = "foo_bar_baz_qux_quux";

        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        String input = "foo bar baz qux quux";

        StringWriter writer = new StringWriter();
        translator.translate(input, writer);

        String output = writer.toString();
        assertEquals("foo_bar_baz_qux_quux", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        String input = "foo bar baz qux quux";

        translator.translate(input, null);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        String input = "";

        String output = translator.translate(input);
        assertNull(output);
    }

    @Test
    public void testTranslateWithNullInput() {
        translator.translate(null, new StringWriter());
    }
}