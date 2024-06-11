import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test basic decimal numeric entity
        String input1 = "&#65;";
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, writer);
        assertEquals(4, result1);
        assertEquals("A", writer.toString());
        
        writer.flush();
        writer.getBuffer().setLength(0);
        
        // Test basic hex numeric entity
        String input2 = "&#x41;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, writer);
        assertEquals(6, result2);
        assertEquals("A", writer.toString());
        
        writer.flush();
        writer.getBuffer().setLength(0);
        
        // Test missing semicolon after numeric entity
        String input3 = "&#65";
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, writer);
        assertEquals(3, result3);
        assertEquals("A", writer.toString());
        
        writer.flush();
        writer.getBuffer().setLength(0);
        
        // Test numeric entity with missing hex indicator
        String input4 = "&65;";
        int index4 = 0;
        int result4 = unescaper.translate(input4, index4, writer);
        assertEquals(3, result4);
        assertEquals("A", writer.toString());
        
        writer.flush();
        writer.getBuffer().setLength(0);
        
        // Test numeric entity with character code larger than 0xFFFF
        String input5 = "&#128514;";
        int index5 = 0;
        int result5 = unescaper.translate(input5, index5, writer);
        assertEquals(10, result5);
        assertEquals("😂", writer.toString());
    }

}