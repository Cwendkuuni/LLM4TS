package org.apache.commons.lang3.text.translate;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper numericEntityUnescaper;

    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testIsSet() {
        assertFalse(numericEntityUnescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(numericEntityUnescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(numericEntityUnescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence input = "&#65;";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);
        assertEquals(4, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateWithSemiColonRequired() throws IOException {
        CharSequence input = "&#65;";
        numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);
        assertEquals(4, result);
        assertEquals("A", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColon() throws IOException {
        CharSequence input = "&#65;";
        numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        numericEntityUnescaper.translate(input, 0, out);
    }

    @Test
    public void testTranslateWithInvalidEntityValue() throws IOException {
        CharSequence input = "&#AAA;";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithHexEntityValue() throws IOException {
        CharSequence input = "&#x41;";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);
        assertEquals(6, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateWithEntityValueGreaterThan65535() throws IOException {
        CharSequence input = "&#65536;";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);
        assertEquals(8, result);
        assertEquals("\uD800\uDC00", out.toString());
    }

}