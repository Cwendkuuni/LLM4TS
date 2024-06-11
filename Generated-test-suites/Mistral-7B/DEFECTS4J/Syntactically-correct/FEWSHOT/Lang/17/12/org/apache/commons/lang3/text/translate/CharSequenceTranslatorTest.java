package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.HexTranslator;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        HexTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator translator = hexTranslator;

        String input = "foo";
        String expected = "foo";
        String result = translator.translate(input);
        assertEquals(expected, result);

        input = "фoo";
        expected = "56f36f";
        result = translator.translate(input);
        assertEquals(expected, result);

        Writer writer = mock(Writer.class);
        int index = 0;
        CharSequence inputSequence = "фoo";

        int consumed = translator.translate(inputSequence, index, writer);
        verify(writer).write(new String(Character.toChars(0x56f3)).getBytes());
        verify(writer).write(new String(Character.toChars(0x6f)).getBytes());
        verify(writer).write(new String(Character.toChars(0x6f)).getBytes());
        assertEquals(2, consumed);

        index += 2;
        consumed = translator.translate(inputSequence, index, writer);
        assertEquals(0, consumed);
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        HexTranslator hexTranslator = new HexTranslator();
        HexTranslator anotherHexTranslator = new HexTranslator();
        CharSequenceTranslator translator = hexTranslator.with(anotherHexTranslator);

        String input = "foo";
        String expected = "foo";
        String result = translator.translate(input);
        assertEquals(expected, result);

        input = "фoo";
        expected = "56f36f";
        result = translator.translate(input);
        assertEquals(expected, result);

        Writer writer = mock(Writer.class);
        int index = 0;
        CharSequence inputSequence = "фoo";

        int consumed = translator.translate(inputSequence, index, writer);
        verify(writer).write(new String(Character.toChars(0x56f3)).getBytes());
        verify(writer).write(new String(Character.toChars(0x6f)).getBytes());
        verify(writer).write(new String(Character.toChars(0x6f)).getBytes());
        assertEquals(2, consumed);

        index += 2;
        consumed = translator.translate(inputSequence, index, writer);
        assertEquals(0, consumed);

        verify(hexTranslator).translate(inputSequence, index, writer);
        verify(anotherHexTranslator).translate(inputSequence, index, writer).times(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new HexTranslator();
        CharSequence inputSequence = "фoo";
        Writer writer = null;

        translator.translate(inputSequence, 0, writer);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new HexTranslator();
        Writer writer = mock(Writer.class);

        translator.translate(null, 0, writer);
        assertNull(writer.toString());
    }

    @Test
    public void testTranslateToString() {
        HexTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator translator = hexTranslator;

        String input = "foo";
        String expected = "foo";
        String result = translator.translateToString(input);
        assertEquals(expected, result);

        input = "фoo";
        expected = "56f36f";
        result = translator.translateToString(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateToStringWithAggregateTranslator() {
        HexTranslator hexTranslator = new HexTranslator();
        HexTranslator anotherHexTranslator = new HexTranslator();
        CharSequenceTranslator translator = hexTranslator.with(anotherHexTranslator);

        String input = "foo";
        String expected = "foo";
        String result = translator.translateToString(input);
        assertEquals(expected, result);

        input = "фoo";
        expected = "56f36f";
        result = translator.translateToString(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateToStringWithNullInput() {
        CharSequenceTranslator translator = new HexTranslator();

        translator.translateToString(null);
        assertNull(translator.translateToString(null));
    }
}