import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();

        // Test numeric entity with decimal codepoint
        String input1 = "&#65;";
        Writer out1 = new StringWriter();
        int result1 = translator.translate(input1, 0, out1);
        assertEquals("Result should be 4", 4, result1);
        assertEquals("Decoded character should be 'A'", 'A', out1.toString().charAt(0));

        // Test numeric entity with hexadecimal codepoint
        String input2 = "&#x41;";
        Writer out2 = new StringWriter();
        int result2 = translator.translate(input2, 0, out2);
        assertEquals("Result should be 5", 5, result2);
        assertEquals("Decoded character should be 'A'", 'A', out2.toString().charAt(0));

        // Test invalid numeric entity
        String input3 = "&#invalid;";
        Writer out3 = new StringWriter();
        int result3 = translator.translate(input3, 0, out3);
        assertEquals("Result should be 0", 0, result3);
        assertEquals("Output should be empty", "", out3.toString());
    }
}