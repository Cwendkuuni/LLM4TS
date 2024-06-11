package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder();
        input.append("&amp;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        Assert.assertEquals(5, result);
        Assert.assertEquals("&", writer.toString());
    }

    @Test
    public void testTranslate_withHexadecimalValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder();
        input.append("&#x41;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        Assert.assertEquals(7, result);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder();
        input.append("&InvalidEntity;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withMissingSemicolon_andOptionSetToError() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder input = new StringBuilder();
        input.append("&#65");
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
    }

    @Test
    public void testIsSet_withOptionsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.semiColonOptional);
        boolean result = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSet_withOptionsNotSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        boolean result = unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional);
        Assert.assertFalse(result);
    }

}