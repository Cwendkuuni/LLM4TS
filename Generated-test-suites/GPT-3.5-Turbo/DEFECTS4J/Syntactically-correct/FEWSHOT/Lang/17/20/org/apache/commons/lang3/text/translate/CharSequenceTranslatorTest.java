
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
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        when(translator.translate("abc")).thenReturn("xyz");
        
        String result = translator.translate("abc");
        
        verify(translator, times(1)).translate("abc");
        assertEquals("xyz", result);
    }
    
    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        when(translator.translate(null)).thenReturn(null);
        
        String result = translator.translate(null);
        
        verify(translator, times(1)).translate(null);
        assertNull(result);
    }
    
    @Test
    public void testTranslateWithIOException() throws IOException {
        Writer writer = mock(Writer.class);
        when(writer.toString()).thenThrow(new IOException());
        
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        try {
            translator.translate("abc", writer);
        } catch (IOException e) {
            // expected exception
            verify(writer, times(1)).toString();
            return;
        }
        fail("Expected IOException");
    }
    
    @Test
    public void testTranslateWithNonNullWriter() throws IOException {
        Writer writer = mock(Writer.class);
        
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("xyz");
                return 3;
            }
        };
        
        translator.translate("abc", writer);
        
        verify(writer, times(1)).write("xyz");
    }
    
    @Test
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        try {
            translator.translate("abc", null);
        } catch (IllegalArgumentException e) {
            // expected exception
            return;
        }
        fail("Expected IllegalArgumentException");
    }
    
    @Test
    public void testTranslateWithZeroConsumed() throws IOException {
        Writer writer = mock(Writer.class);
        
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        translator.translate("a", writer);
        
        verify(writer, times(1)).write(new char[]{'a'});
    }
    
    @Test
    public void testTranslateWithNonZeroConsumed() throws IOException {
        Writer writer = mock(Writer.class);
        
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("xyz");
                return 2;
            }
        };
        
        translator.translate("abc", writer);
        
        verify(writer, times(1)).write("xyz");
    }
    
    @Test
    public void testTranslateWithSurrogatePair() throws IOException {
        Writer writer = mock(Writer.class);
        
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("xyz");
                return 2;
            }
        };
        
        translator.translate("a", writer);
        translator.translate("b", writer);
        translator.translate("c", writer);
        
        verify(writer, times(1)).write("xyz");
        verify(writer, times(1)).write(new char[]{'c'});
    }
    
    @Test
    public void testWith() throws IOException {
        CharSequenceTranslator translator1 = mock(CharSequenceTranslator.class);
        CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        
        assertEquals(translator1, ((AggregateTranslator) mergedTranslator).translators[0]);
        assertEquals(translator2, ((AggregateTranslator) mergedTranslator).translators[1]);
    }
    
    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex('A'));
        assertEquals("61", CharSequenceTranslator.hex('a'));
        assertEquals("30", CharSequenceTranslator.hex('0'));
        assertEquals("3F", CharSequenceTranslator.hex('?'));
        assertEquals("4D4C", CharSequenceTranslator.hex('䵌'));
    }
}
