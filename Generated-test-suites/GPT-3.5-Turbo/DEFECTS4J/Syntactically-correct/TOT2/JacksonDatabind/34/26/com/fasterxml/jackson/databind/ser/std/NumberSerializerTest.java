import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        BigDecimal value = new BigDecimal("123.456");
        serializer.serialize(value, jsonGenerator, provider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        BigInteger value = BigInteger.valueOf(123456789);
        serializer.serialize(value, jsonGenerator, provider);

        verify(jsonGenerator).writeNumber(value);
    }

    // Additional test cases for other scenarios can be added here
}