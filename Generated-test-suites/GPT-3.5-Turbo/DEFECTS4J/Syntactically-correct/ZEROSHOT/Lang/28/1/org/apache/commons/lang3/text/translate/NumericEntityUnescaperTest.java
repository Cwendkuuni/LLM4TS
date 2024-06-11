import org.junit.Test;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test empty input
        {
            String input = "";
            int index = 0;
            StringWriter writer = new StringWriter();
            try {
                int result = unescaper.translate(input, index, writer);
                assertEquals(0, result);
                assertEquals("", writer.toString());
            } catch (Exception e) {
                fail("Exception occurred: " + e.getMessage());
            }
        }
        
        // Test unsupported format
        {
            String input = "abc";
            int index = 0;
            StringWriter writer = new StringWriter();
            try {
                int result = unescaper.translate(input, index, writer);
                assertEquals(0, result);
                assertEquals("", writer.toString());
            } catch (Exception e) {
                fail("Exception occurred: " + e.getMessage());
            }
        }
        
        // Test decimal numeric entity
        {
            String input = "&#65;";
            int index = 0;
            StringWriter writer = new StringWriter();
            try {
                int result = unescaper.translate(input, index, writer);
                assertEquals(6, result);
                assertEquals("A", writer.toString());
            } catch (Exception e) {
                fail("Exception occurred: " + e.getMessage());
            }
        }
        
        // Test hexadecimal numeric entity
        {
            String input = "&#x61;";
            int index = 0;
            StringWriter writer = new StringWriter();
            try {
                int result = unescaper.translate(input, index, writer);
                assertEquals(7, result);
                assertEquals("a", writer.toString());
            } catch (Exception e) {
                fail("Exception occurred: " + e.getMessage());
            }
        }
    }
}