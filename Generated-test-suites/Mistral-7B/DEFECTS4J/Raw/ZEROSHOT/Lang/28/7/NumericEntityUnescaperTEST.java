import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSimpleNumericEntities() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        String input = "& #x26; & #10; & #XA; & #0xFF; & #x2234;";
        StringWriter output = new StringWriter();

        CharSequence inputSequence = new String(input);

        for (int i = 0; i < inputSequence.length(); i++) {
            int translatedLength = translator.translate(inputSequence, i, output);
            String expectedOutput = "";
            switch (inputSequence.charAt(i)) {
                case '&':
                    expectedOutput = "&amp;";
                    break;
                case '#':
                    if (i < inputSequence.length() - 1 && Character.isDigit(inputSequence.charAt(i + 1))) {
                        int entityValue = Character.isDigit(inputSequence.charAt(i + 1)) ? Integer.parseInt(inputSequence.subSequence(i + 1, i + 3).toString(), 10) : Integer.parseInt(inputSequence.subSequence(i + 1, i + 7).toString(), 16);
                        expectedOutput = String.format("&%s;", Integer.toHexString(entityValue).substring(1));
                        i += 5;
                    }
                    break;
                default:
                    expectedOutput = Character.isWhitespace(inputSequence.charAt(i)) ? " " : Character.toString(inputSequence.charAt(i));
            }
            String actualOutput = output.getBuffer().toString();
            Assert.assertEquals(expectedOutput, actualOutput.substring(0, translatedLength));
            output.getBuffer().setLength(0);
        }

        Assert.assertEquals("", output.toString());
    }

    @Test
    public void testTranslateMixedStringAndEntities() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        String input = "Hello & #123; World! & #x61; & #X41;";
        StringWriter output = new StringWriter();

        CharSequence inputSequence = new String(input);

        for (int i = 0; i < inputSequence.length(); i++) {
            int translatedLength = translator.translate(inputSequence, i, output);
            String expectedOutput = "";
            switch (inputSequence.charAt(i)) {
                case '&':
                    expectedOutput = "&amp;";
                    break;
                case '#':
                    if (i < inputSequence.length() - 1 && Character.isDigit(inputSequence.charAt(i + 1))) {
                        int entityValue = Character.isDigit(inputSequence.charAt(i + 1)) ? Integer.parseInt(inputSequence.subSequence(i + 1, i + 3).toString(), 10) : Integer.parseInt(inputSequence.subSequence(i + 1, i + 7).toString(), 16);
                        expectedOutput = String.format("&%s;", Integer.toHexString(entityValue).substring(1));
                        i += 5;
                    } else {
                        expectedOutput = Character.toString(inputSequence.charAt(i));
                    }
                    break;
                default:
                    expectedOutput = Character.toString(inputSequence.charAt(i));
            }
            String actualOutput = output.getBuffer().toString();
            Assert.assertEquals(expectedOutput, actualOutput.substring(0, translatedLength));
            output.getBuffer().setLength(0);
        }

        Assert.assertEquals("Hello &amp; World! &a &A", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidEntity() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        String input = "& %";
        StringWriter output = new StringWriter();

        CharSequence inputSequence = new String(input);

        translator.translate(inputSequence, 0, output);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateOutOfBoundsEntity() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        String input = "& #x26; & #10; & #XA &";
        StringWriter output = new StringWriter();

        CharSequence inputSequence = new String(input);

        translator.translate(inputSequence, inputSequence.length() + 1, output);
    }
}