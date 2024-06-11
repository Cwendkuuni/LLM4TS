import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidHexEntity_shouldTranslateToCorrectCodepoint() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#x0041;";

        int index = 0;
        int translated = translator.translate(input, index, writer);

        assert(translated == 8); // Expected translation length
        assert(writer.toString().equals("A")); // Expected translated output
    }

    @Test
    public void testTranslate_withValidDecEntity_shouldTranslateToCorrectCodepoint() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65;";

        int index = 0;
        int translated = translator.translate(input, index, writer);

        assert(translated == 6); // Expected translation length
        assert(writer.toString().equals("A")); // Expected translated output
    }

    @Test
    public void testTranslate_withInvalidEntity_shouldReturnZero() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&invalid;";

        int index = 0;
        int translated = translator.translate(input, index, writer);

        assert(translated == 0); // Expected translation length
        assert(writer.toString().equals("")); // Expected translated output
    }

    // Add more test methods to cover all scenarios

}