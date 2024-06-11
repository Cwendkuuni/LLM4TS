package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper numericEntityUnescaper;

    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_withHexValue() throws IOException {
        StringWriter output = new StringWriter();
        int result = numericEntityUnescaper.translate("&#x41;", 0, output);
        assertEquals(6, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_withDecimalValue() throws IOException {
        StringWriter output = new StringWriter();
        int result = numericEntityUnescaper.translate("&#65;", 0, output);
        assertEquals(5, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_withInvalidValue() throws IOException {
        StringWriter output = new StringWriter();
        int result = numericEntityUnescaper.translate("&#invalid;", 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_withInvalidHexValue() throws IOException {
        StringWriter output = new StringWriter();
        int result = numericEntityUnescaper.translate("&#xinvalid;", 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_endSemiColonMissing() throws IOException {
        StringWriter output = new StringWriter();
        int result = numericEntityUnescaper.translate("&#65", 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_invalidFormat() throws IOException {
        StringWriter output = new StringWriter();
        int result = numericEntityUnescaper.translate("abc", 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_characterCodeOverFFFF() throws IOException {
        StringWriter output = new StringWriter();
        int result = numericEntityUnescaper.translate("&#65536;", 0, output);
        assertEquals(7, result);
        assertEquals("\uD800\uDC00", output.toString());
    }

}