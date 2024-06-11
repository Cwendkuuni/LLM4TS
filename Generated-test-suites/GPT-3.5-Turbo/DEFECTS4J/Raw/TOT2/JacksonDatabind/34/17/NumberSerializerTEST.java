import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        
        BigDecimal value = new BigDecimal("123.456");
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify that the writeNumber method was called with the correct value
        // Assuming that the value is written as is for BigDecimal
        // Adjust the assertion as per the actual implementation
        // Example:
        // verify(jsonGenerator).writeNumber(value);
    }
    
    // Add similar test methods for other Number types like BigInteger, Integer, Long, Double, etc.

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        // Mock the Type typeHint if necessary
        
        // Call getSchema method and verify the Schema Node returned
        
    }

    // Add more test methods to cover all public methods in NumberSerializer class
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Verify that the expected method was called on the visitor based on the typeHint provided
        
    }
}