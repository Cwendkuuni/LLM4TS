package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;

import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.util.Locale;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {
    
    private CharSequenceTranslator translator;
    
    @Before
    public void setUp() {
        translator = new AggregateTranslator();
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
        assertEquals("Hello, world!", translator.translate("Hello, world!"));
    }
    
    @Test
    public void testTranslate_withSpecialCharacters_shouldReturnTranslatedString() {
        assertEquals("\\u0026\\u003c\\u003e", translator.translate("&<>"));
    }
    
    @Test
    public void testTranslate_withUnicodeInput_shouldReturnTranslatedString() {
        assertEquals("\\u0026\\u003c\\u003e", translator.translate("\u0026\u003c\u003e"));
    }
    
    @Test
    public void testTranslate_withTranslator_returnMergedTranslator() {
        CharSequenceTranslator anotherTranslator = new AggregateTranslator();
        CharSequenceTranslator mergedTranslator = translator.with(anotherTranslator);
        
        assertNotNull(mergedTranslator);
        assertNotSame(translator, mergedTranslator);
        assertNotSame(anotherTranslator, mergedTranslator);
    }
    
    @Test
    public void testHex_withValidCharacter_shouldReturnHexadecimalString() {
        assertEquals("0041", CharSequenceTranslator.hex(65));
        assertEquals("0061", CharSequenceTranslator.hex(97));
        assertEquals("0024", CharSequenceTranslator.hex(36));
        assertEquals("0026", CharSequenceTranslator.hex(38));
        assertEquals("003c", CharSequenceTranslator.hex(60));
        assertEquals("003e", CharSequenceTranslator.hex(62));
    }
    
    @Test
    public void testTranslate_withCharSequenceWriter_shouldTranslateTextToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "Hello, world!";
        translator.translate(input, writer);
        assertEquals("Hello, world!", writer.toString());
    }
    
    @Test
    public void testTranslate_withCharSequenceWriterAndNullInput_shouldNotTranslateAnythingToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withNullWriter_shouldThrowIllegalArgumentException() throws IOException {
        translator.translate("Hello, world!", null);
    }
    
    @Test
    public void testTranslate_withCharSequenceWriterAndTranslatedInput_shouldTranslateTextToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "&<>Hello, world!";
        translator.translate(input, writer);
        assertEquals("\\u0026\\u003c\\u003eHello, world!", writer.toString());
    }
    
    @Test
    public void testTranslate_withCharSequenceWriterAndUnicodeInput_shouldTranslateTextToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "\u0026\u003c\u003eHello, world!";
        translator.translate(input, writer);
        assertEquals("\\u0026\\u003c\\u003eHello, world!", writer.toString());
    }
    
    @Test
    public void testTranslate_withCharSequenceWriterAndTranslatedInputAndSurrogatePair_shouldTranslateTextToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "Hello, \uD83C\uDFB6!";
        translator.translate(input, writer);
        assertEquals("Hello, \\uD83C\\uDFB6!", writer.toString());
    }
    
    @Test
    public void testTranslate_withCharSequenceWriterAndTranslatedInputAndSurrogatePairAtEnd_shouldTranslateTextToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "Hello, \uD83C\uDFB6";
        translator.translate(input, writer);
        assertEquals("Hello, \\uD83C\\uDFB6", writer.toString());
    }

}