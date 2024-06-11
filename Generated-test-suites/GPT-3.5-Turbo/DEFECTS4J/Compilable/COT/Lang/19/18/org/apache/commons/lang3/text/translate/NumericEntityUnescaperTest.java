package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;


    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence input = "&#65;";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithHex() throws IOException {
        CharSequence input = "&#x41;";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithoutSemicolon() throws IOException {
        CharSequence input = "&#65";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateInvalidHex() throws IOException {
        CharSequence input = "&#xgg;";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateInvalidDec() throws IOException {
        CharSequence input = "&#abc;";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateHighCodePoint() throws IOException {
        CharSequence input = "&#65536;";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
        assertEquals("", writer.toString());
    }
}