package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        assertEquals("", translator.translate(new String(""), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithSingleKeyValue() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { new CharSequence[] {"foo", "bar"} });
        assertEquals("bar", translator.translate(new String("foo"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithMultipleKeysValues() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            new CharSequence[] {"foo1", "bar1"},
            new CharSequence[] {"foo2", "bar2"},
            new CharSequence[] {"foo3", "bar3"}
        });
        assertEquals("bar1", translator.translate(new String("foo1"), 0, new StringBuffer()));
        assertEquals("bar2", translator.translate(new String("foo2"), 0, new StringBuffer()));
        assertEquals("bar3", translator.translate(new String("foo3"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithPartialMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            new CharSequence[] {"foo1", "bar1"},
            new CharSequence[] {"foo2", "bar2"},
            new CharSequence[] {"foo3", "bar3"}
        });
        assertEquals("bar1", translator.translate(new String("foo1bar"), 0, new StringBuffer()));
        assertEquals("", translator.translate(new String("foobar"), 0, new StringBuffer()));
        assertEquals("", translator.translate(new String("barfoo"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithOutOfBoundsIndex() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            new CharSequence[] {"foo1", "bar1"},
            new CharSequence[] {"foo2", "bar2"},
            new CharSequence[] {"foo3", "bar3"}
        });
        String input = "foobarfoo";
        StringBuffer output = new StringBuffer();

        // Index is out of bounds, no translation should occur
        assertEquals("", translator.translate(new String(input), input.length(), output));
        assertEquals(input, new String(input));
        assertEquals("", output.toString());

        // Translate the first occurrence of "foo"
        int index = input.indexOf("foo");
        assertEquals("bar1", translator.translate(new String(input), index, output));
        assertEquals("bar1foo", new String(input.substring(index)));
        assertEquals("bar1", output.toString());

        // Translate the second occurrence of "foo"
        index += "foo".length();
        assertEquals("bar2", translator.translate(new String(input), index, output));
        assertEquals("bar1bar2", new String(input.substring(index)));
        assertEquals("bar1bar2", output.toString());
    }

    @Test
    public void testLookupTranslatorWithEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            new CharSequence[] {"foo1", "bar1"},
            new CharSequence[] {"foo2", "bar2"},
            new CharSequence[] {"foo3", "bar3"}
        });
        assertEquals("", translator.translate(new String(""), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            new CharSequence[] {"foo1", "bar1"},
            new CharSequence[] {"foo2", "bar2"},
            new CharSequence[] {"foo3", "bar3"}
        });
        assertNull(translator.translate(null, 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithNullOutput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            new CharSequence[] {"foo1", "bar1"},
            new CharSequence[] {"foo2", "bar2"},
            new CharSequence[] {"foo3", "bar3"}
        });
        assertEquals("", translator.translate(new String("foo"), 0, null));
    }
}