import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_shouldUnescapeValidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&amp;#65;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals("The result should be the length of the numeric entity including start ampersand and end semicolon characters",
                8, result);
        assertEquals("The output writer should contain the corresponding character 'A'",
                "A", writer.toString());
    }

    @Test
    public void testTranslate_shouldNotUnescapeIfNotNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&amp;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals("The result should be 0 as there is no valid numeric entity",
                0, result);
        assertEquals("The output writer should be empty as no character is unescaped",
                "", writer.toString());
    }

    @Test
    public void testTranslate_shouldNotUnescapeIfNotStartingWithAmpersand() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "65;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals("The result should be 0 as there is no start ampersand",
                0, result);
        assertEquals("The output writer should be empty as no character is unescaped",
                "", writer.toString());
    }

    @Test
    public void testTranslate_shouldNotUnescapeIfIncorrectHexadecimalFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&#xG;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals("The result should be 0 as the hexadecimal format is incorrect",
                0, result);
        assertEquals("The output writer should be empty as no character is unescaped",
                "", writer.toString());
    }

    @Test
    public void testTranslate_shouldNotUnescapeIfIncorrectDecimalFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&#10.5;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals("The result should be 0 as the decimal format is incorrect",
                0, result);
        assertEquals("The output writer should be empty as no character is unescaped",
                "", writer.toString());
    }

    @Test
    public void testTranslate_shouldNotUnescapeIfNumberFormatException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "&#A;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals("The result should be 0 as the number format is incorrect",
                0, result);
        assertEquals("The output writer should be empty as no character is unescaped",
                "", writer.toString());
    }

    @Test
    public void testTranslate_shouldNotUnescapeIfIOExceptionIsThrown() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Simulated IOException");
            }
        };
        StringWriter writer = new StringWriter();
        String input = "&#65;";
        int index = 0;

        try {
            unescaper.translate(input, index, writer);
            fail("Should throw an IOException");
        } catch (IOException e) {
            assertEquals("Simulated IOException", e.getMessage());
        }
        assertEquals("The output writer should be empty as IOException was thrown",
                "", writer.toString());
    }

}