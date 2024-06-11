import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NumberSerializerTest {
    // Test cases for each method
    // ...

    // Edge case 1: Test serialization with BigDecimal and BigInteger values
    @Test
    public void testSerializationWithBigDecimalAndBigInteger() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        BigDecimal bigDecimal = new BigDecimal("1234567890.1234567890");
        BigInteger bigInteger = new BigInteger("98765432109876543210");

        // Act
        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(2)).writeNumber(any(Number.class));
    }

    // Edge case 2: Test serialization with Integer, Long, Float, and Double values
    // ...

    // Edge case 3: Test serialization with null value
    // ...

    // Edge case 4: Test serialization with invalid input (String, Boolean, etc.) values
    // ...

    // Exception handling: Test serialization with an IOException
    @Test(expected = IOException.class)
    public void testSerializationWithIOException() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(jsonGenerator.getRewritable().findRaisable(anyInt(), any()))
                .thenThrow(new IOException("Test IOException"));

        // Act and Assert
        numberSerializer.serialize(123, jsonGenerator, serializerProvider);
    }

    // Exception handling: Test serialization with a JsonMappingException
    // ...
}