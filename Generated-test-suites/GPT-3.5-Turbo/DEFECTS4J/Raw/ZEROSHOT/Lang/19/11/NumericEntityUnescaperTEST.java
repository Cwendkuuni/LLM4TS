import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test when input is "&amp;"
        // Expected output is "&"
        int result = translator.translate("&amp;", 0, writer);
        assertEquals(1, result);
        assertEquals("&", writer.toString());
        writer.getBuffer().setLength(0);

        // Test when input is "&#65;"
        // Expected output is "A"
        result = translator.translate("&#65;", 0, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Test when input is "&#x41;"
        // Expected output is "A"
        result = translator.translate("&#x41;", 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0);

        // Test when input is "&#99999999;" (Hex value exceeds maximum value of integer)
        // Expected output is empty string
        result = translator.translate("&#99999999;", 0, writer);
        assertEquals(11, result);
        assertEquals("", writer.toString());
        writer.getBuffer().setLength(0);

        // Test when input is "No numeric entity"
        // Expected output is empty string
        result = translator.translate("No numeric entity", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
        writer.getBuffer().setLength(0);
    }
}