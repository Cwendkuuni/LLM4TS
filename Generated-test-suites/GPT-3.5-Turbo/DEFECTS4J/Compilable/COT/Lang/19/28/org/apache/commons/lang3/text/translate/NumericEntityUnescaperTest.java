package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_NoNumericEntity_ReturnsZero() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        CharSequence input = "Sample Text";
        StringWriter writer = new StringWriter();
        int result = translator.translate(input, 0, writer);
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_NumericEntity_HandlesDecimalValue() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        CharSequence input = "&#65;";
        StringWriter writer = new StringWriter();
        int result = translator.translate(input, 0, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_NumericEntity_HandlesHexadecimalValue() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        CharSequence input = "&#x61;";
        StringWriter writer = new StringWriter();
        int result = translator.translate(input, 0, writer);
        assertEquals(5, result);
        assertEquals("a", writer.toString());
    }
    
    @Test
    public void testTranslate_NumericEntity_WithoutSemicolon_HandlesDecimalValue() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        CharSequence input = "&#65";
        StringWriter writer = new StringWriter();
        int result = translator.translate(input, 0, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_NumericEntity_WithoutSemicolon_HandlesHexadecimalValue() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        CharSequence input = "&#x61";
        StringWriter writer = new StringWriter();
        int result = translator.translate(input, 0, writer);
        assertEquals(5, result);
        assertEquals("a", writer.toString());
    }
    
    @Test
    public void testTranslate_NumericEntity_InvalidNumericFormat_ReturnsZero() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        CharSequence input = "&#notANumber;";
        StringWriter writer = new StringWriter();
        int result = translator.translate(input, 0, writer);
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_NumericEntity_BeyondBMP_ReturnsSurrogatePair() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        CharSequence input = "&#128512;";
        StringWriter writer = new StringWriter();
        int result = translator.translate(input, 0, writer);
        assertEquals(11, result);
        assertEquals("\uD83D\uDE00", writer.toString());
    }
    
}