
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void translate_shouldReturnZero_whenInputIsEmpty() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int index = 0;

        int result = unescaper.translate("", index, writer);

        assertEquals(0, result);
    }

    @Test
    public void translate_shouldReturnZero_whenInputDoesNotContainNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int index = 0;
        String input = "This is a test string without numeric entities";

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
    }

    @Test
    public void translate_shouldTranslateDecimalNumericEntityToCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int index = 0;
        String input = "This is a test string with &#65;";

        int result = unescaper.translate(input, index, writer);

        assertEquals(7, result);
        assertEquals("This is a test string with A", writer.toString());
    }

    @Test
    public void translate_shouldTranslateHexadecimalNumericEntityToCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int index = 0;
        String input = "This is a test string with &#x41;";

        int result = unescaper.translate(input, index, writer);

        assertEquals(8, result);
        assertEquals("This is a test string with A", writer.toString());
    }

    @Test
    public void translate_shouldNotTranslateInvalidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int index = 0;
        String input = "This is a test string with invalid numeric entity: &#invalid;";

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}

