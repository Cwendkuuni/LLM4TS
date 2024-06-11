import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_shouldReturnCorrectValueForValidDecimalEntityWithSemicolon() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "This is an example string with a decimal entity &#32;.";
        int index = input.indexOf("&#32;");

        int result = unescaper.translate(input, index, writer);

        assertEquals(6, result);
        assertEquals("This is an example string with a decimal entity  .", writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnCorrectValueForValidDecimalEntityWithoutSemicolon() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "This is an example string with a decimal entity &#32";
        int index = input.indexOf("&#32");

        int result = unescaper.translate(input, index, writer);

        assertEquals(5, result);
        assertEquals("This is an example string with a decimal entity  ", writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnCorrectValueForValidHexadecimalEntityWithSemicolon() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "This is an example string with a hexadecimal entity &#x20;.";
        int index = input.indexOf("&#x20;");

        int result = unescaper.translate(input, index, writer);

        assertEquals(8, result);
        assertEquals("This is an example string with a hexadecimal entity  .", writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnCorrectValueForValidHexadecimalEntityWithoutSemicolon() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "This is an example string with a hexadecimal entity &#x20";
        int index = input.indexOf("&#x20");

        int result = unescaper.translate(input, index, writer);

        assertEquals(7, result);
        assertEquals("This is an example string with a hexadecimal entity  ", writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnZeroForInvalidEntity() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "This is an example string with an invalid entity &#9999999;.";
        int index = input.indexOf("&#9999999;");

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnZeroForInvalidFormatEntity() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "This is an example string with an invalid format entity &#xG;.";
        int index = input.indexOf("&#xG;");

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnZeroIfEntityIsIncomplete() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "This is an example string with an incomplete entity &#";
        int index = input.indexOf("&#");

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnZeroIfEntityIsIncompleteHexadecimal() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "This is an example string with an incomplete hexadecimal entity &#x";
        int index = input.indexOf("&#x");

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnZeroIfEntityIsWithoutNumericCharacters() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "This is an example string with an entity without numeric characters &#;";
        int index = input.indexOf("&#;");

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}