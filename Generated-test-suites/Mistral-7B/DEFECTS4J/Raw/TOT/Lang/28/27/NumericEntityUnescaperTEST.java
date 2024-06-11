package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.test.TestUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    private static final String ENTITY_WITH_HEX = "&#[xAA]FF;";
    private static final String ENTITY_WITH_DEC = "&#[123];";

    private static final String INPUT_WITH_ENTITY = "This is a &#[xAA]FF; text &This is a &#[123] text.";

    private static final String EXPECTED_OUTPUT = "This is a ｢｡ｩﾜﾞｦﾙﾝ text This is a ３ text.";

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslateHexEntity() throws UnsupportedEncodingException {
        StringWriter out = new StringWriter();
        unescaper.translate(new String(ENTITY_WITH_HEX), 0, out);
        Assert.assertEquals(EXPECTED_OUTPUT.substring(0, 2) + (char) 0xFFFF + EXPECTED_OUTPUT.substring(2), out.toString());
    }

    @Test
    public void testTranslateDecEntity() throws UnsupportedEncodingException {
        StringWriter out = new StringWriter();
        unescaper.translate(new String(ENTITY_WITH_DEC), 0, out);
        Assert.assertEquals(EXPECTED_OUTPUT.substring(0, 2) + '３' + EXPECTED_OUTPUT.substring(2), out.toString());
    }

    @Test
    public void testTranslateComplexInput() throws UnsupportedEncodingException {
        String input = "This is a &#[xAA]FF;&#[123] &This is a &#[xAA]FF;&#[123] text.";
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 0, out);
        String expectedOutput = EXPECTED_OUTPUT + EXPECTED_OUTPUT;
        Assert.assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslateEmptyInput() throws UnsupportedEncodingException {
        StringWriter out = new StringWriter();
        unescaper.translate(CharSequenceUtils.EMPTY, 0, out);
        Assert.assertEquals(CharSequenceUtils.EMPTY, out.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslateNullInput() {
        StringWriter out = new StringWriter();
        unescaper.translate(null, 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInputOutOfBounds() {
        String input = "&";
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 1, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidEntity() {
        String input = "& invalidEntity;";
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 0, out);
    }

    @Test
    public void testTranslateRandomEntity() throws UnsupportedEncodingException {
        String input = getRandomEntity();
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 0, out);
        String expectedOutput = getRandomOutput(input);
        Assert.assertEquals(expectedOutput, out.toString());
    }

    private String getRandomEntity() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("&#[");
        sb.append(random.nextInt(0x10FFFF) + 1);
        if (random.nextBoolean()) {
            sb.append("x");
        }
        sb.append("]");
        String entity = sb.toString();
        return entity;
    }

    private String getRandomOutput(String entity) {
        StringWriter out = new StringWriter();
        int codePoint = entity.substring(2).length() > 1 ? Integer.parseInt(entity.substring(2), 16) : Integer.parseInt(entity.substring(2));
        String expectedOutput = String.valueOf((char) codePoint);
        unescaper.translate(new String(entity), 0, out);
        return expectedOutput;
    }
}