package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CharSequenceTranslatorTest {
    private final CharSequenceTranslator translator = new CharSequenceTranslator() {
      
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0; // Mocked method
        }
    };
    
    @Test
    public void testTranslate_WithNullInput_ReturnsNull() {
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testTranslate_WithNonNullInput_WritesToWriter() throws IOException {
        final CharSequence input = "input";
        Writer mockedWriter = mock(Writer.class);
        
        translator.translate(input, mockedWriter);
        
        ArgumentCaptor<CharSequence> inputCaptor = ArgumentCaptor.forClass(CharSequence.class);
        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Writer> writerCaptor = ArgumentCaptor.forClass(Writer.class);
        verify(translator).translate(inputCaptor.capture(), indexCaptor.capture(), writerCaptor.capture());
        
        assertEquals(input, inputCaptor.getValue());
        assertEquals(0, (int) indexCaptor.getValue());
        assertEquals(mockedWriter, writerCaptor.getValue());
    }
    
    @Test
    public void testTranslate_WithNonNullInputAndNullWriter_ThrowsIllegalArgumentException() {
        final CharSequence input = "input";
        Writer mockedWriter = null;
        
        try {
            translator.translate(input, mockedWriter);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            // Error should not occur
            assert false;
        }
    }
    
    @Test
    public void testTranslate_Always_CallsTranslateMethod() throws IOException {
        final CharSequence input = "input";
        Writer mockedWriter = mock(Writer.class);
        
        translator.translate(input, mockedWriter);
        
        verify(translator).translate(input, 0, mockedWriter);
    }
    
    @Test
    public void testWith_Always_ReturnsAggregateTranslator() {
        CharSequenceTranslator[] translators = {translator, translator};
        
        CharSequenceTranslator result = translator.with(translators);
        
        assertEquals(AggregateTranslator.class, result.getClass());
    }
    
    @Test
    public void testHex_Always_ReturnsUpperCaseHexString() {
        assertEquals("A", CharSequenceTranslator.hex(10));
        assertEquals("FF", CharSequenceTranslator.hex(255));
        assertEquals("1F4", CharSequenceTranslator.hex(500));
    }
}