package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&amp;", 0, output);
        assertEquals(5, result);
        assertEquals("&", output.toString());
    }

    @Test
    public void testTranslateWithOptionalSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#32", 0, output);
        assertEquals(4, result);
        assertEquals(" ", output.toString());
    }

    @Test
    public void testTranslateHexCode() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, output);
        assertEquals(7, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslateInvalidCode() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#asdf;", 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslateCodeBeyondFFFF() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int result = unescaper.translate("&#65536;", 0, output);
        assertEquals(10, result);
        assertEquals("𐀀", output.toString());
    }
}