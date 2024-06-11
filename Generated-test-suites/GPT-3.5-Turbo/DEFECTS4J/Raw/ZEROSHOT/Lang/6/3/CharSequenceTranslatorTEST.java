import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    private final CharSequenceTranslator translator = new CharSequenceTranslator() {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    };
    
    @Test
    public void testTranslate_withNonNullInput_shouldReturnTranslatedText() {
        String input = "abc";
        String expectedOutput = "abc";
        
        String output = translator.translate(input);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        String input = null;
        String expectedOutput = null;
        
        String output = translator.translate(input);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testTranslate_withWriter_shouldWriteTranslatedText() throws IOException {
        String input = "abc";
        String expectedOutput = "abc";
        StringWriter writer = new StringWriter();
        
        translator.translate(input, writer);
        String output = writer.toString();
        
        assertEquals(expectedOutput, output);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withNullWriter_shouldThrowIllegalArgumentException() throws IOException {
        String input = "abc";
        Writer writer = null;
        
        translator.translate(input, writer);
    }
    
    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        String input = "";
        String expectedOutput = "";
        
        String output = translator.translate(input);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testWith_withTranslators_shouldReturnMergedTranslator() {
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator mergedTranslator = translator.with(translator2);
        
        assertTrue(mergedTranslator instanceof AggregateTranslator);
    }
    
    @Test
    public void testHex_withCodepoint_shouldReturnUpperCaseHexString() {
        int codepoint = 65;
        String expectedOutput = "41";
        
        String output = CharSequenceTranslator.hex(codepoint);
        
        assertEquals(expectedOutput, output);
    }
    
}