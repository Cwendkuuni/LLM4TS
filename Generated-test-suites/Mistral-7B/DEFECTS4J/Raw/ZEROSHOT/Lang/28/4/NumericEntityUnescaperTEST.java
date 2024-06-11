import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringWriter;
import java.io.IOException;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        String input = "&#x41;Hello &#[x61]world! &amp; &&#10;Test &#127; &#160; &#x201c; &#x201d; &#153; &#154;";
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(new CharSequence() {
                @Override
                public CharSequence subSequence(int start, int end) {
                    return new String(input.subSequence(index, end));
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, writer);
            index += length;
        }

        String output = writer.toString();
        String expectedOutput = StringEscapeUtils.unescapeXml("AHello world! &amp; &amp;Test &#128; &#160; &#x2018; &#x2019; &#153; &#154;");
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateDecimals() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        String input = "&#[128]; &#[160]; &#[127]; &#[153]; &#[154];";
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(new CharSequence() {
                @Override
                public CharSequence subSequence(int start, int end) {
                    return new String(input.subSequence(index, end));
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, writer);
            index += length;
        }

        String output = writer.toString();
        String expectedOutput = StringEscapeUtils.unescapeXml("&#128; &#160; &#127; &#153; &#154;");
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateInvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        String input = "&abcd;";
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(new CharSequence() {
                @Override
                public CharSequence subSequence(int start, int end) {
                    return new String(input.subSequence(index, end));
                }

                @Override
                public int length() {
                    return input.length();
                }
            }, index, writer);
            index += length;
        }

        String output = writer.toString();
        assertEquals("", output);
    }

    @Test
    public void testTranslateEmptyString() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        String input = "";
        int index = 0;

        int length = unescaper.translate(new CharSequence() {
            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.subSequence(index, end));
            }

            @Override
            public int length() {
                return input.length();
            }
        }, index, writer);

        String output = writer.toString();
        assertEquals("", output);
    }
}