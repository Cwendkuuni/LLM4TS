import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;


public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws JsonMappingException, IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = null;
        
        // Typical use cases
        Assert.assertEquals(10, stdKeyDeserializer.deserializeKey("10", context)); // Key is int
        
        // Edge cases
        Assert.assertEquals(-2147483648, stdKeyDeserializer.deserializeKey("-2147483648", context)); // Key is the minimum value of int
        Assert.assertEquals(2147483647, stdKeyDeserializer.deserializeKey("2147483647", context)); // Key is the maximum value of int
        
        // Error scenarios
        try {
            stdKeyDeserializer.deserializeKey("abc", context); // Key is not int
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("java.lang.NumberFormatException: For input string: \"abc\"", e.getMessage());
        }
        
        try {
            stdKeyDeserializer.deserializeKey(null, context); // Key is null
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("java.lang.NullPointerException", e.getMessage());
        }
    }
}