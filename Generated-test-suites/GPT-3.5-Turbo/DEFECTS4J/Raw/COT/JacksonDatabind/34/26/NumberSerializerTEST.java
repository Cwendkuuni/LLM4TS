
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigDecimal value = new BigDecimal("10.5");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigInteger value = new BigInteger("10");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Integer value = 10;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Long value = 10L;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.longValue());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Double value = 10.5;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Float value = 10.5f;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.floatValue());
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Byte value = 10;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Short.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Short value = 10;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeOtherNumber() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number value = 10;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.toString());
    }

}
