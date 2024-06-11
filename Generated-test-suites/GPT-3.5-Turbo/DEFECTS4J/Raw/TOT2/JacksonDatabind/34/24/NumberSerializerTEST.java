import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

    @Mock
    JsonGenerator mockJsonGenerator;
    
    @Mock
    SerializerProvider mockSerializerProvider;
    
    NumberSerializer numberSerializer = new NumberSerializer(Number.class);
    
    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.45");
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("12345");
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 123;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        Long value = 123L;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value.longValue());
    }
    
    // Additional test cases for other types of Numbers can be added here
    
    // Add edge case and exception handling test cases
    
}