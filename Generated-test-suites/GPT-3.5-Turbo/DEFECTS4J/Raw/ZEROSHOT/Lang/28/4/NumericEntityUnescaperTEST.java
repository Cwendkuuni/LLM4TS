import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidInput_shouldReturnValidOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidInput_shouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}