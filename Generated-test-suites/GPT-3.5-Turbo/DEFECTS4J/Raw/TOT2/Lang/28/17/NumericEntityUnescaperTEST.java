import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Typical use case
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
        
        // Edge case: Hexadecimal input
        result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());
        
        // Error scenario: Non-numeric input
        result = unescaper.translate("&#A;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
        
        // Edge case: Reading past the end of input
        result = unescaper.translate("&#65;", 3, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}