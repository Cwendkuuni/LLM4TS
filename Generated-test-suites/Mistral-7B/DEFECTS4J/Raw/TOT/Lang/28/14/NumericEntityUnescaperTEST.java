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

    private static final String XML_ENTITIES = "&#x1F602;&#xa9;&#x26;#123;";
    private static final String EXPECTED_OUTPUT = "😜⁡&123;";

    /**
     * Test translate method with valid hex numeric entity.
     */
    @Test
    public void testTranslateHexNumericEntity() throws UnsupportedEncodingException {
        String input = "&xml&nbsp;" + XML_ENTITIES + "&xml&nbsp;";
        StringWriter out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(new String(input.getBytes("UTF-8")), 0, out);

        String actualOutput = out.toString().substring(0, length);
        String expectedOutput = new String(input.getBytes("UTF-8"), 0, input.length() - XML_ENTITIES.length()) + EXPECTED_OUTPUT;

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translate method with valid decimal numeric entity.
     */
    @Test
    public void testTranslateDecimalNumericEntity() throws UnsupportedEncodingException {
        String input = "&xml&nbsp;&#123;abc&xml&nbsp;";
        StringWriter out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(new String(input.getBytes("UTF-8")), 0, out);

        String actualOutput = out.toString().substring(0, length);
        String expectedOutput = new String(input.getBytes("UTF-8"), 0, input.length() - String.valueOf(123).length()) + "123";

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translate method with invalid numeric entity.
     */
    @Test
    public void testTranslateInvalidNumericEntity() throws UnsupportedEncodingException {
        String input = "&xml&nbsp;&#xabcd;&xml&nbsp;";
        StringWriter out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(new String(input.getBytes("UTF-8")), 0, out);

        String actualOutput = out.toString().substring(0, length);
        String expectedOutput = new String(input.getBytes("UTF-8"), 0, input.length());

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testTranslateEmptyInput() throws UnsupportedEncodingException {
        String input = "";
        StringWriter out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(new String(input.getBytes("UTF-8")), 0, out);

        String actualOutput = out.toString().substring(0, length);
        String expectedOutput = "";

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translate method with input containing only numeric entities.
     */
    @Test
    public void testTranslateOnlyNumericEntitiesInput() throws UnsupportedEncodingException {
        Random random = new Random();
        String input = "";
        for (int i = 0; i < 10; i++) {
            int entityValue = random.nextInt(65535) + 1;
            if (entityValue > 0xFFFF) {
                input += "&#[x" + Integer.toHexString(entityValue).toUpperCase() + "];";
            } else {
                input += "&#[";
                if (entityValue >= 10) {
                    input += entityValue;
                } else {
                    input += '0';
                }
                input += ']';
                input += ';';
            }
        }

        StringWriter out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(new String(input.getBytes("UTF-8")), 0, out);

        String actualOutput = out.toString().substring(0, length);
        String expectedOutput = input;

        Assert.assertEquals(expectedOutput, actualOutput);
    }
}