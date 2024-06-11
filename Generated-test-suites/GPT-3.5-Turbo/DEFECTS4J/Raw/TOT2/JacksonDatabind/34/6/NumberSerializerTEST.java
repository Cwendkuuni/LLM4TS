import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        
        BigDecimal value = new BigDecimal("123.45");
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        
        BigInteger value = BigInteger.valueOf(123);
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        
        Integer value = 123;
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeNumber(value);
    }
    
    // More test cases for other scenarios and edge cases can be added here

}