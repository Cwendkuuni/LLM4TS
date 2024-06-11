import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test case for decimal numeric entity
        String input1 = "&#65;";
        int result1 = unescaper.translate(input1, 0, writer);
        Assert.assertEquals("Result should be 6", 6, result1);
        Assert.assertEquals("Resulting output should be 'A'", "A", writer.toString());
        writer.getBuffer().setLength(0); // Clear the writer
        
        // Test case for hexadecimal numeric entity
        String input2 = "&#x1F601;";
        int result2 = unescaper.translate(input2, 0, writer);
        Assert.assertEquals("Result should be 10", 10, result2);
        Assert.assertEquals("Resulting output should be '😁'", "😁", writer.toString());
        writer.getBuffer().setLength(0); // Clear the writer
        
        // Test case for numeric entity without semicolon
        String input3 = "&#8364";
        int result3 = unescaper.translate(input3, 0, writer);
        Assert.assertEquals("Result should be 6", 6, result3);
        Assert.assertEquals("Resulting output should be '€'", "€", writer.toString());
        writer.getBuffer().setLength(0); // Clear the writer
        
        // Test case for invalid numeric entity
        String input4 = "&#xinvalid;";
        int result4 = unescaper.translate(input4, 0, writer);
        Assert.assertEquals("Result should be 0", 0, result4);
        Assert.assertEquals("Resulting output should be empty", "", writer.toString());
        writer.getBuffer().setLength(0); // Clear the writer
    }
}