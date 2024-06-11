import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;
    
    @Before
    public void setup() {
        translator = mock(CharSequenceTranslator.class, CALLS_REAL_METHODS);
    }
    
    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        assertEquals("", translator.translate(""));
    }
    
    @Test
    public void testTranslate_withNonEmptyInput_shouldReturnTranslatedString() {
        String input = "Hello World!";
        String expectedOutput = "Translated Hello World!";
        when(translator.translate(eq(input), any(Writer.class))).thenReturn(expectedOutput);
        
        assertEquals(expectedOutput, translator.translate(input));
        verify(translator, times(1)).translate(eq(input), any(Writer.class));
    }
    
    @Test
    public void testTranslate_withErrorInWriter_shouldThrowRuntimeException() throws Exception {
        String input = "Hello World!";
        Writer writer = mock(Writer.class);
        when(writer.toString()).thenThrow(new IOException());
        
        RuntimeException exception = null;
        try {
            translator.translate(input, writer);
        } catch (RuntimeException e) {
            exception = e;
        }
        
        assertEquals(RuntimeException.class, exception.getClass());
        verify(writer, times(1)).toString();
    }
    
    @Test
    public void testTranslate_withNullWriter_shouldThrowIllegalArgumentException() {
        String input = "Hello World!";
        Writer writer = null;
        
        IllegalArgumentException exception = null;
        try {
            translator.translate(input, writer);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        
        assertEquals(IllegalArgumentException.class, exception.getClass());
    }
    
    @Test
    public void testTranslate_withZeroConsumed_shouldTranslateCharacterIndividually() throws Exception {
        String input = "Hello World!";
        StringWriter writer = new StringWriter();
        when(translator.translate(eq(input), anyInt(), any(Writer.class))).thenReturn(0);
        
        translator.translate(input, writer);
        
        String expectedOutput = "Translated HTranslated eTranslated lTranslated lTranslated oTranslated  Translated WTranslated oTranslated rTranslated lTranslated dTranslated !";
        assertEquals(expectedOutput, writer.toString());
        verify(translator, times(input.length())).translate(eq(input), anyInt(), any(Writer.class));
    }
    
    @Test
    public void testTranslate_withPositiveConsumed_shouldIncrementPositionByCodePointCount() throws Exception {
        String input = "Hello World!";
        StringWriter writer = new StringWriter();
        when(translator.translate(eq(input), anyInt(), any(Writer.class))).thenReturn(1);
        
        translator.translate(input, writer);
        
        assertEquals("Translated World!", writer.toString());
        verify(translator, times(1)).translate(eq(input), anyInt(), any(Writer.class));
    }
    
    @Test
    public void testHex_withPositiveCodepoint_shouldReturnUpperCaseHex() {
        int codepoint = 123;
        String expectedOutput = "7B";
        
        assertEquals(expectedOutput, CharSequenceTranslator.hex(codepoint));
    }
    
    @Test
    public void testHex_withNegativeCodepoint_shouldReturnUpperCaseHex() {
        int codepoint = -123;
        String expectedOutput = "FFFFFF85";
        
        assertEquals(expectedOutput, CharSequenceTranslator.hex(codepoint));
    }
    
}