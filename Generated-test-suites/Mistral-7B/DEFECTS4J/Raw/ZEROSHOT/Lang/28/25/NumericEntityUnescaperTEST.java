package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.ArrayUtils.toObject;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.class$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Tests for {@link NumericEntityUnescaper} class.
 *
 * @author John Smith
 */
public class NumericEntityUnescaperTest {

    /**
     * Test the unescaping of numeric entities using {@link NumericEntityUnescaper}.
     */
    @Test
    public void testUnescapeNumericEntities() {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        String input = "&amp;#160;&#32; &#x201C;&#x201D;&#167; &#55357;&#56861;&#55356;&#56865; &#251;"
                + " &#x20AC;&#240;&#224;&#230;&#228; &#161; &#241; &#35; &#xa9; &#x2013; &#164; &#169; &#171; &#183; &#194; &#196; &#216; &#225; &#377; &#402; &#8212; &#8240; &#8249; &#8250; &#8260; &#8287; &#8364; &#8457; &#8514; &#8592; &#8593; &#8594; &#8595; &#8596; &#8597; &#8629; &#8804; &#8834; &#8835; &#8845; &#8853; &#8855; &#8869; &#8871; &#8940; &#8941; &#8942; &#8943; &#8944; &#8945; &#8946; &#8947; &#8953; &#8960; &#8963; &#8983; &#8993; &#9674; &#9675; &#9677;";
        byte[] expectedOutput = new byte[] { 160, 32, 220, 168, 8201, 8202, 167, 55357, 56861, 55356, 56865, 251, 128, 130, 145, 146, 147, 148, 154, 155, 156, 157, 158, 159, 160, 163, 165, 166, 167, 168, 169, 171, 177, 194, 195, 214, 225, 376, 401, 8212, 8240, 8249, 8250, 8260, 8286, 8363, 8456, 8513, 8591, 8592, 8593, 8594, 8595, 8596, 8597, 8628, 8803, 8833, 8834, 8844, 8852, 8854, 8859, 8868, 8940, 8941, 8942, 8943, 8944, 8945, 8946, 8947, 8952, 8960, 8962, 8982, 8992, 9632, 9633, 9635 };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        int index = 0;
        while (index < input.length()) {
            int translatedLength = translator.translate(input, index, writer);
            index += translatedLength;
        }
        byte[] actualOutput = outputStream.toByteArray();

        assertThat(CharSequenceUtils.toString(input), Arrays.equals(expectedOutput, actualOutput));
        assertEquals(expectedOutput.length, actualOutput.length);
    }

    /**
     * Test the unescaping of numeric entities using {@link NumericEntityUnescaper} with empty input.
     */
    @Test
    public void testUnescapeEmptyNumericEntities() {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        String input = EMPTY;
        byte[] expectedOutput = new byte[0];

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        int index = 0;
        while (index < input.length()) {
            int translatedLength = translator.translate(input, index, writer);
            index += translatedLength;
        }
        byte[] actualOutput = outputStream.toByteArray();

        assertEquals(expectedOutput.length, actualOutput.length);
        assertThat(actualOutput, toObject(expectedOutput));
    }

    /**
     * Test the unescaping of numeric entities using {@link NumericEntityUnescaper} with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testUnescapeNullNumericEntities() {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        String input = null;
        Writer writer = new StringBuilder();

        int index = 0;
        translator.translate(input, index, writer);
    }
}