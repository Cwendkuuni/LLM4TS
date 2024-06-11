package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    private static final String NUMERIC_ENTITY = "&#[x%#x]; &#[x00FF]; &#[x1F4A9]; &#[x1F600]; &#[1024]; &#[-1];";

    @Test
    public void testTranslateNumericEntities() throws Exception {
        String input = NUMERIC_ENTITY;
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        int translationLength = unescaper.translate(new StringReader(input), 0, out);
        String output = out.toString();

        String expectedOutput = StringUtils.replaceEach(NUMERIC_ENTITY, new String[]{"&#[x%#x];", "&#[x00FF];", "&#[x1F4A9];", "&#[x1F600];", "&#[1024];", "&#[-1];"}, new String[]{"\u0025%\u0025", "\u00FF", "\uD83C\uDFFF\uD83E\uDD17", "\uD83C\uDDFA\uDD18", "\u0400", "\uFFFD"});

        assertEquals(expectedOutput, output.substring(0, translationLength));
    }

    // Add more test cases for edge and error scenarios.

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumericEntity() throws Exception {
        String input = "&invalidEntity;";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(new StringReader(input), 0, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() throws Exception {
        String input = "&a";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(new StringReader(input), 0, out);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidNumericEntityValue() throws Exception {
        String input = "&#abc;";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(new StringReader(input), 0, out);
    }
}