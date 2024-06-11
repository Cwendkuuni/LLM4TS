import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() {
        // Test serializing a BigDecimal
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal testValue = new BigDecimal("10.5");
        
        // Create a fake JsonGenerator and SerializerProvider for testing
        // Mock these objects
        
        // Call the serialize method of the serializer
        // Verify that the output matches the expected output
    }
    
    @Test
    public void testSerializeBigInteger() {
        // Test serializing a BigInteger
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger testValue = new BigInteger("100");
        
        // Create a fake JsonGenerator and SerializerProvider for testing
        // Mock these objects
        
        // Call the serialize method of the serializer
        // Verify that the output matches the expected output
    }

    // Include test methods for other public methods in the NumberSerializer class
    // Add edge cases and exception handling scenarios

}