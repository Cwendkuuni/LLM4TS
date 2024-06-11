
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidHexadecimalNumericEntity_returnsCorrectCharacter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#x1E;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals(4, result);
        assertEquals((char) 30, writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_withValidDecimalNumericEntity_returnsCorrectCharacter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#30;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals(4, result);
        assertEquals((char) 30, writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_withInvalidHexadecimalNumericEntity_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#xInvalidHex;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withInvalidDecimalNumericEntity_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#InvalidDecimal;";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withNoNumericEntity_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "This is a test";
        int index = 0;

        int result = unescaper.translate(input, index, writer);

        assertEquals(0, result);
    }
}
