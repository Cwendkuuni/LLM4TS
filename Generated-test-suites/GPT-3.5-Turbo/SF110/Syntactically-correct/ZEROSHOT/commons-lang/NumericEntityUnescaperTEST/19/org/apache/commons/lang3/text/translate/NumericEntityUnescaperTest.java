package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslateWithSemiColonRequired() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#65;", 0, out);
        
        assertEquals(6, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateWithSemiColonOptional() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#65;", 0, out);
        
        assertEquals(7, result);
        assertEquals("A", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        unescaper.translate("&#65;", 0, out);
    }

    @Test
    public void testTranslateWithInvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, out);
        
        assertEquals(7, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateWithEntityValueGreaterThan65535() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#65536;", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithEntityValueLessThan65535() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#65;", 0, out);
        
        assertEquals(6, result);
        assertEquals("A", out.toString());
    }
}