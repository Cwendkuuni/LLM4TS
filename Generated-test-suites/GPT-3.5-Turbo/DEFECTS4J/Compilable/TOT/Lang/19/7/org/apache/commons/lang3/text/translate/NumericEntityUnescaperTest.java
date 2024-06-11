package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import org.junit.Assert;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_WithValidHexadecimalNumericEntity_ReturnsTranslatedCodepoint() throws Exception {
        StringWriter writer = new StringWriter();
        String input = "&#x41;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        Assert.assertEquals(4, writer.toString());
        Assert.assertEquals(5, result);
    }

    @Test
    public void testTranslate_WithValidDecimalNumericEntity_ReturnsTranslatedCodepoint() throws Exception {
        StringWriter writer = new StringWriter();
        String input = "&#65;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        Assert.assertEquals(4, writer.toString());
        Assert.assertEquals(5, result);
    }

    @Test
    public void testTranslate_WithMissingLeadingCharacter_ReturnsZero() throws Exception {
        StringWriter writer = new StringWriter();
        String input = "&#41;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        Assert.assertEquals("", writer.toString());
        Assert.assertEquals(0, result);
    }

    @Test
    public void testTranslate_WithInvalidHexadecimalNumericEntity_ReturnsZero() throws Exception {
        StringWriter writer = new StringWriter();
        String input = "&#xG1;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        Assert.assertEquals("", writer.toString());
        Assert.assertEquals(0, result);
    }

    @Test
    public void testTranslate_WithInvalidDecimalNumericEntity_ReturnsZero() throws Exception {
        StringWriter writer = new StringWriter();
        String input = "&#10E;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        Assert.assertEquals("", writer.toString());
        Assert.assertEquals(0, result);
    }

    @Test
    public void testTranslate_WithMissingSemiColon_ReturnsTranslatedCodepoint() throws Exception {
        StringWriter writer = new StringWriter();
        String input = "&#65";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        Assert.assertEquals(4, writer.toString());
        Assert.assertEquals(5, result);
    }

    @Test
    public void testTranslate_WithInvalidCharacterAfterAmpersand_ReturnsZero() throws Exception {
        StringWriter writer = new StringWriter();
        String input = "&x41;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        Assert.assertEquals("", writer.toString());
        Assert.assertEquals(0, result);
    }
}

