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
    
    private CharSequenceTranslator translator = new CharSequenceTranslator() {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    };
    
    @Test
    public void testTranslate_CharSequenceIntWriter() throws IOException {
        StringWriter writer = new StringWriter();
        assertEquals(0, translator.translate("test", 0, writer));
        assertEquals("", writer.toString());
        
        StringWriter mockWriter = mock(StringWriter.class);
        when(mockWriter.toString()).thenReturn("123");
        assertEquals(0, translator.translate("test", 0, mockWriter));
        verify(mockWriter).toString();
    }
    
    @Test
    public void testTranslate_CharSequence() {
        assertEquals(null, translator.translate(null));
        
        StringWriter writer = new StringWriter();
        when(writer.toString()).thenReturn("test");
        assertEquals("test", translator.translate("test"));
        verify(writer).toString();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullWriter() throws IOException {
        writer = null;
        translator.translate("test", 0, writer);
    }
    
    @Test
    public void testTranslate_NullInput() throws IOException {
        StringWriter writer = new StringWriter();
        assertEquals(null, translator.translate(null, writer));
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_PosIntWriter() throws IOException {
        writer = null;
        StringWriter writer = new StringWriter();
        assertEquals(0, translator.translate("test", 2, writer));
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = mock(CharSequenceTranslator.class);
        CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);
        
        CharSequenceTranslator result = translator.with(translator1, translator2);
        
        assertEquals(AggregateTranslator.class, result.getClass());
        AggregateTranslator aggregateTranslator = (AggregateTranslator) result;
        assertEquals(translator, aggregateTranslator.getTranslators()[0]);
        assertEquals(translator1, aggregateTranslator.getTranslators()[1]);
        assertEquals(translator2, aggregateTranslator.getTranslators()[2]);
    }
    
    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("12AB", CharSequenceTranslator.hex(4779));
    }
}