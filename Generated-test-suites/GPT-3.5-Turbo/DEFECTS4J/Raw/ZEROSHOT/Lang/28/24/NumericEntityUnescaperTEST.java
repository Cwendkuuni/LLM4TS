import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithValidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "&#65;";
        int result = unescaper.translate(input, 0, out);
        assertEquals("Translate method should return the number of characters processed", 5, result);
        assertEquals("Translate method should write the translated character to the output stream", "A", out.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "&#invalid;";
        int result = unescaper.translate(input, 0, out);
        assertEquals("Translate method should return 0 if parsing the input fails", 0, result);
        assertEquals("Translate method should not write anything to the output stream if parsing the input fails", "", out.toString());
    }
}