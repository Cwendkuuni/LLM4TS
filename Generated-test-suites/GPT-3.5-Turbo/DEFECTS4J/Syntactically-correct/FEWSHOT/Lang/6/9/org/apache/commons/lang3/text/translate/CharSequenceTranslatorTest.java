package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        String result = translator.translate(null);
        assertNull(result);

        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = mock(CharSequenceTranslator.class);    
        when(translator1.translate(any(CharSequence.class), anyInt(), any(Writer.class))).thenReturn(0);

        CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);    
        when(translator2.translate(any(CharSequence.class), anyInt(), any(Writer.class))).thenReturn(0);

        CharSequenceTranslator[] translators = {translator1, translator2};

        CharSequenceTranslator mergedTranslator = new AggregateTranslator(translators).with(translator1, translator2);

        StringWriter writer = new StringWriter();
        try {
            mergedTranslator.translate(null, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        verify(translator1, times(2)).translate(any(CharSequence.class), anyInt(), any(Writer.class));
        verify(translator2, times(2)).translate(any(CharSequence.class), anyInt(), any(Writer.class));
    }

    @Test
    public void testHex() {
        assertEquals("A", CharSequenceTranslator.hex(10));
        assertEquals("1D", CharSequenceTranslator.hex(29));
        assertEquals("FF", CharSequenceTranslator.hex(255));
    }
}