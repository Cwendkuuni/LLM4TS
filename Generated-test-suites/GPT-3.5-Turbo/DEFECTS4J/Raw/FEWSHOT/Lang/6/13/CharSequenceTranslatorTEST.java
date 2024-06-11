
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithWriter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("foo", writer);
        assertEquals("bar", writer.toString());
    }
    
    @Test
    public void testTranslateWithNullWriter() throws IOException {
        MyTranslator translator = new MyTranslator();
        try {
            translator.translate("foo", null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testTranslateWithNullInput() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateWithNullInputAndNullWriter() throws IOException {
        MyTranslator translator = new MyTranslator();
        try {
            translator.translate(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testTranslateWithInputContainingSurrogatePairs() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("Hello \uD83D\uDE00", writer);
        assertEquals("Goodbye \uD83D\uDE01", writer.toString());
    }
    
    @Test
    public void testTranslateWithInputContainingNonTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("ABC", writer);
        assertEquals("ABC", writer.toString());
    }
    
    @Test
    public void testTranslateWithInputContainingTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("\uD83D\uDE00", writer);
        assertEquals("\uD83D\uDE01", writer.toString());
    }
    
    @Test
    public void testTranslateWithNullInputAndNonTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateWithNullInputAndTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateWithInputAndNullWriter() throws IOException {
        MyTranslator translator = new MyTranslator();
        try {
            translator.translate("foo", null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testTranslateWithTranslatedInput() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("foo", writer);
        assertEquals("bar", writer.toString());
    }
    
    @Test
    public void testTranslateWithTranslatedInputContainingSurrogatePairs() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("Hello \uD83D\uDE00", writer);
        assertEquals("Goodbye \uD83D\uDE01", writer.toString());
    }
    
    @Test
    public void testTranslateWithTranslatedInputContainingNonTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("ABC", writer);
        assertEquals("ABC", writer.toString());
    }
    
    @Test
    public void testTranslateWithTranslatedInputContainingTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("\uD83D\uDE00", writer);
        assertEquals("\uD83D\uDE01", writer.toString());
    }
    
    @Test
    public void testTranslateWithTranslatedNullInputAndNonTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateWithTranslatedNullInputAndTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithTranslatedInputAndNullWriter() throws IOException {
        MyTranslator translator = new MyTranslator();
        try {
            translator.translate("foo", null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateWithEmptyInputAndNullWriter() throws IOException {
        MyTranslator translator = new MyTranslator();
        try {
            translator.translate("", null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        }
    }

    @Test
    public void testTranslateWithNonEmptyInput() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("foo", writer);
        assertEquals("bar", writer.toString());
    }
    
    @Test
    public void testTranslateWithNonEmptyInputContainingSurrogatePairs() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("Hello \uD83D\uDE00", writer);
        assertEquals("Goodbye \uD83D\uDE01", writer.toString());
    }
    
    @Test
    public void testTranslateWithNonEmptyInputContainingNonTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("ABC", writer);
        assertEquals("ABC", writer.toString());
    }
    
    @Test
    public void testTranslateWithNonEmptyInputContainingTranslatableCharacter() throws IOException {
        MyTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("\uD83D\uDE00", writer);
        assertEquals("\uD83D\uDE01", writer.toString());
    }
    
    @Test
    public void testTranslateWithNonEmptyAndNullWriter() throws IOException {
        MyTranslator translator = new MyTranslator();
        try {
            translator.translate("foo", null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testWith() {
        MyTranslator translator = new MyTranslator();
        MyTranslator translator2 = new MyTranslator();
        MyTranslator translator3 = new MyTranslator();
        assertEquals(Arrays.asList(translator, translator2, translator3), translator.with(translator2, translator3).getTranslators());
    }
    
    @Test
    public void testHex() {
        assertEquals("4D2", CharSequenceTranslator.hex(1234));
        assertEquals("1F600", CharSequenceTranslator.hex(128512));
    }

    private class MyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'f') {
                out.write("b");
                return 1;
            }
            if (input.charAt(index) == 'o') {
                out.write("a");
                return 1;
            }
            return 0;
        }

        public List<CharSequenceTranslator> getTranslators() {
            return Arrays.asList(this);
        }
    }
}
