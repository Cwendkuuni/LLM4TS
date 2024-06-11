package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * Unit tests for {@link LookupTranslator} class
 *
 * @author John Doe
 */
public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(6, translator.longest);
    }

    @Test
    public void testTranslateNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"hello", "hi"}});
        assertNull(translator.translate(null, 0, new StringWriter()));
    }

    @Test
    public void testTranslateEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"hello", "hi"}});
        assertNull(translator.translate(Character.toString(new char[0]), 0, new StringWriter()));
    }

    @Test
    public void testTranslateEmptyLookup() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        assertNull(translator.translate(Character.toString("hello"), 0, new StringWriter()));
    }

    @Test
    public void testTranslateSimple() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"hello", "hi"}, {"world", "earth"}});
        StringWriter writer = new StringWriter();
        assertEquals(5, translator.translate(Character.toString("hello"), 0, writer).intValue());
        assertEquals("hi", writer.toString());
        writer.getBuffer().setLength(0);
        assertEquals(6, translator.translate(Character.toString("world"), 0, writer).intValue());
        assertEquals("earth", writer.toString());
    }

    @Test
    public void testTranslateMiddle() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"hello", "hi"}, {"world", "earth"}});
        StringWriter writer = new StringWriter();
        assertEquals(5, translator.translate(Character.toString("helloworld"), 0, writer).intValue());
        assertEquals("hi", writer.toString());
        writer.getBuffer().setLength(0);
        assertEquals(6, translator.translate(Character.toString("helloworld"), 5, writer).intValue());
        assertEquals("earth", writer.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateOutOfBounds() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"hello", "hi"}, {"world", "earth"}});
        StringWriter writer = new StringWriter();
        translator.translate(Character.toString("helloworld"), 10, writer);
    }

    @Test
    public void testTranslateCaseInsensitive() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"Hello", "hi"}, {"World", "earth"}});
        StringWriter writer = new StringWriter();
        assertEquals(5, translator.translate(Character.toString("Hello"), 0, writer).intValue());
        assertEquals("hi", writer.toString());
        writer.getBuffer().setLength(0);
        assertEquals(6, translator.translate(Character.toString("World"), 0, writer).intValue());
        assertEquals("earth", writer.toString());
    }

    @Test
    public void testTranslateMixed() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"hello", "hi"}, {"World", "earth"}});
        StringWriter writer = new StringWriter();
        assertEquals(5, translator.translate(Character.toString("helloWorld"), 0, writer).intValue());
        assertEquals("hi", writer.toString());
        writer.getBuffer().setLength(0);
        assertEquals(6, translator.translate(Character.toString("helloWorld"), 5, writer).intValue());
        assertEquals("earth", writer.toString());
    }

    @Test
    public void testTranslateLongest() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"hello", "hi"}, {"world", "earth"}});
        StringWriter writer = new StringWriter();
        assertEquals(6, translator.translate(Character.toString("worldhelloworld"), 0, writer).intValue());
        assertEquals("earth", writer.toString());
        writer.getBuffer().setLength(0);
        assertEquals(5, translator.translate(Character.toString("worldhelloworld"), 6, writer).intValue());
        assertEquals("hi", writer.toString());
    }

    @Test
    public void testTranslateShortest() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"hello", "hi"}, {"world", "earth"}});
        StringWriter writer = new StringWriter();
        assertEquals(5, translator.translate(Character.toString("hell"), 0, writer).intValue());
        assertEquals("hi", writer.toString());
    }
}