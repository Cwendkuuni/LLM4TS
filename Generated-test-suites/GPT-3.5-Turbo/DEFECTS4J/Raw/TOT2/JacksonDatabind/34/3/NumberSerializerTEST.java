import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {

    @Mock
    JsonGenerator jsonGeneratorMock;
    
    @Mock
    SerializerProvider serializerProviderMock;

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal testValue = new BigDecimal("123.45");
        numberSerializer.serialize(testValue, jsonGeneratorMock, serializerProviderMock);
        // Add assertions here
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigInteger testValue = new BigInteger("12345");
        numberSerializer.serialize(testValue, jsonGeneratorMock, serializerProviderMock);
        // Add assertions here
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Integer testValue = 123;
        numberSerializer.serialize(testValue, jsonGeneratorMock, serializerProviderMock);
        // Add assertions here
    }

    // Add more test methods for other scenarios...

}