import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidHexEntity_shouldReturnCorrectCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Testing &#x2714"; // U+2714 is the check mark symbol

        int result = unescaper.translate(input, 8, out);

        assertEquals(6, result); // 6 characters were consumed
        assertEquals("\u2714", out.toString()); // check mark symbol is successfully unescaped
    }

    @Test
    public void testTranslate_withValidDecimalEntity_shouldReturnCorrectCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Testing &#10003"; // U+2713 is the check mark symbol

        int result = unescaper.translate(input, 8, out);

        assertEquals(7, result); // 7 characters were consumed
        assertEquals("\u2713", out.toString()); // check mark symbol is successfully unescaped
    }

    @Test
    public void testTranslate_withEntityNotStartingWithAmpersand_shouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Testing #10003"; // missing starting & for the entity

        int result = unescaper.translate(input, 8, out);

        assertEquals(0, result); // no entity was processed
        assertEquals("", out.toString()); // output should be empty
    }

    @Test
    public void testTranslate_withInvalidHexEntity_shouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Testing &#xG;"; // G is not a valid hex digit

        int result = unescaper.translate(input, 8, out);

        assertEquals(0, result); // no entity was processed
        assertEquals("", out.toString()); // output should be empty
    }

    @Test
    public void testTranslate_withInvalidDecimalEntity_shouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Testing &#ABC;"; // ABC is not a valid decimal number

        int result = unescaper.translate(input, 8, out);

        assertEquals(0, result); // no entity was processed
        assertEquals("", out.toString()); // output should be empty
    }

    @Test
    public void testTranslate_withMissingSemicolon_shouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Testing &#10003"; // missing ending semicolon for the entity

        int result = unescaper.translate(input, 8, out);

        assertEquals(0, result); // no entity was processed
        assertEquals("", out.toString()); // output should be empty
    }

}