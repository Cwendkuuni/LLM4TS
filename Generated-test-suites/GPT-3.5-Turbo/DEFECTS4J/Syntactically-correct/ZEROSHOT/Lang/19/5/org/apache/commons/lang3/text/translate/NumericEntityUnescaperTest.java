package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper numericEntityUnescaper;
    
    @Before
    public void setup() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }
    
    @Test
    public void testTranslate_withNumericEntity_returnsLengthOfTranslatedEntity() throws IOException {
        CharSequence input = "&amp;";
        int index = 0;
        Writer out = mock(Writer.class);
        
        int result = numericEntityUnescaper.translate(input, index, out);
        
        assertEquals(5, result);
        verify(out).write('&');
    }
    
    @Test
    public void testTranslate_withHexNumericEntity_returnsLengthOfTranslatedEntity() throws IOException {
        CharSequence input = "&#x41;";
        int index = 0;
        Writer out = mock(Writer.class);
        
        int result = numericEntityUnescaper.translate(input, index, out);
        
        assertEquals(6, result);
        verify(out).write('A');
    }
    
    @Test
    public void testTranslate_withInvalidNumericEntity_returnsZero() throws IOException {
        CharSequence input = "&#xg1;";
        int index = 0;
        Writer out = mock(Writer.class);
        
        int result = numericEntityUnescaper.translate(input, index, out);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_withNoSemicolonNumericEntity_returnsLengthOfTranslatedEntity() throws IOException {
        CharSequence input = "&#65";
        int index = 0;
        Writer out = mock(Writer.class);
        
        int result = numericEntityUnescaper.translate(input, index, out);
        
        assertEquals(5, result);
        verify(out).write('A');
    }
    
    @Test
    public void testTranslate_withLargeNumericEntity_returnsLengthOfTranslatedEntity() throws IOException {
        CharSequence input = "&#Ω;";
        int index = 0;
        Writer out = mock(Writer.class);
        
        int result = numericEntityUnescaper.translate(input, index, out);
        
        assertEquals(7, result);
        verify(out).write('\u03A9');
    }
    
    @Test
    public void testTranslate_withNonNumericEntity_returnsZero() throws IOException {
        CharSequence input = "&lt;";
        int index = 0;
        Writer out = mock(Writer.class);
        
        int result = numericEntityUnescaper.translate(input, index, out);
        
        assertEquals(0, result);
    }
}