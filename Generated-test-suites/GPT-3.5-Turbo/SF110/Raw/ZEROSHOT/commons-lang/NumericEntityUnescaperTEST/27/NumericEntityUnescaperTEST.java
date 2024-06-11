package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_whenSemiColonRequired_returnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder sb = new StringBuilder();
        sb.append("&#65");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_whenNoSemiColonAndSemiColonRequired_returnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder sb = new StringBuilder();
        sb.append("&#65");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_whenNoSemiColonAndErrorIfNoSemiColon_throwIllegalArgumentException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder sb = new StringBuilder();
        sb.append("&#65");
        StringWriter writer = new StringWriter();
        unescaper.translate(sb, 0, writer);
    }

    @Test
    public void testTranslate_whenNoSemiColonAndSemiColonOptional_returnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder sb = new StringBuilder();
        sb.append("&#65");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_whenValidEntityValue_returnTranslatedValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("&#65;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_whenValidHexEntityValue_returnTranslatedValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("&#x41;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_whenInvalidEntityValue_returnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("&#9999;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_whenEntityValueGreaterThan65535_returnTranslatedValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("&#65536;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(9, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }

}