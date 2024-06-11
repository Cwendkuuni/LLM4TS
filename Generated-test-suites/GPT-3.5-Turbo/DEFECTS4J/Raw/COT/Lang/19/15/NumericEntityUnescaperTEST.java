
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        Writer writer = new StringWriter();
        
        // Basic test case
        input.append("&#65;");
        int result = unescaper.translate(input, 0, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
        writer.flush();
        writer.close();
        input.setLength(0);
        
        // Test case with hex value
        input.append("&#x41;");
        writer = new StringWriter();
        result = unescaper.translate(input, 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
        writer.flush();
        writer.close();
        input.setLength(0);
        
        // Test case without semicolon
        input.append("&#65");
        writer = new StringWriter();
        result = unescaper.translate(input, 0, writer);
        assertEquals(3, result);
        assertEquals("A", writer.toString());
        writer.flush();
        writer.close();
        input.setLength(0);
        
        // Test case with value greater than 0xFFFF
        input.append("&#65536;");
        writer = new StringWriter();
        result = unescaper.translate(input, 0, writer);
        assertEquals(8, result);
        assertEquals("\uD800\uDC00", writer.toString());
        writer.flush();
        writer.close();
        input.setLength(0);
        
        // Test case with invalid numeric entity
        input.append("&#xZZ;");
        writer = new StringWriter();
        result = unescaper.translate(input, 0, writer);
        assertEquals(6, result);
        assertEquals("", writer.toString());
        writer.flush();
        writer.close();
        input.setLength(0);
    }
}
