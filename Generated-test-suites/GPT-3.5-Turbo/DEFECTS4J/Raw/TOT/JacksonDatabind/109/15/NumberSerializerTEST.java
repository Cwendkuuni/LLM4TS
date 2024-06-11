import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {
    
    private NumberSerializer numberSerializer;
    
    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }
    
    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        JsonGenerator generator = null; // Mock JsonGenerator
        SerializerProvider provider = null; // Mock SerializerProvider
        BigDecimal value = new BigDecimal("123.45");
        
        // Act
        numberSerializer.serialize(value, generator, provider);
        
        // Assert
        // Assert that the correct BigDecimal has been written to the JsonGenerator
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        JsonGenerator generator = null; // Mock JsonGenerator
        SerializerProvider provider = null; // Mock SerializerProvider
        BigInteger value = BigInteger.valueOf(123);
        
        // Act
        numberSerializer.serialize(value, generator, provider);
        
        // Assert
        // Assert that the correct BigInteger has been written to the JsonGenerator
    }
    
    // Add more test methods for the remaining scenarios
    
}