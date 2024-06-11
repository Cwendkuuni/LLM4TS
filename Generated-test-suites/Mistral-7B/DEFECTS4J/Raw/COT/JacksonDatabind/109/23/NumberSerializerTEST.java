import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

    @Test
    public void testInstance() {
        assertEquals(NumberSerializer.class, NumberSerializer.instance.getClass());
    }

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        Number number = numbers.getNumber();

        serializer.serialize(number, generator, provider);

        assertNull(generator.getCurrentEventId());
        assertNull(generator.getCurrentOutputValue());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        BigDecimal bigDecimal = numbers.getBigDecimal();

        serializer.serialize(bigDecimal, generator, provider);

        assertEquals(JsonGenerator.Event.NUMBER, generator.getCurrentEventId());
        assertEquals(bigDecimal.toString(), generator.getCurrentOutputValue().asText());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        BigInteger bigInteger = numbers.getBigInteger();

        serializer.serialize(bigInteger, generator, provider);

        assertEquals(JsonGenerator.Event.NUMBER, generator.getCurrentEventId());
        assertEquals(bigInteger.toString(), generator.getCurrentOutputValue().asText());
    }

    @Test(expected = IOException.class)
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        long number = numbers.getLong();

        serializer.serialize(number, generator, provider);
    }

    @Test(expected = IOException.class)
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        double number = numbers.getDouble();

        serializer.serialize(number, generator, provider);
    }

    @Test(expected = IOException.class)
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        float number = numbers.getFloat();

        serializer.serialize(number, generator, provider);
    }

    @Test(expected = IOException.class)
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        int number = numbers.getInt();

        serializer.serialize(number, generator, provider);
    }

    @Test(expected = IOException.class)
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        byte number = numbers.getByte();

        serializer.serialize(number, generator, provider);
    }

    @Test(expected = IOException.class)
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        short number = numbers.getShort();

        serializer.serialize(number, generator, provider);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonFormat format = mock(JsonFormat.class);
        when(provider.findFormatOverrides(property, Number.class)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);

        JsonSerializer<?> result = serializer.createContextual(provider, property);

        assertEquals(ToStringSerializer.class, result.getClass());
    }

    @Test
    public void testSerializeWithJsonFormat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = new DefaultSerializerProvider();

        ObjectNumbers numbers = new ObjectNumbers();
        BigDecimal bigDecimal = numbers.getBigDecimal();

        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.NUMBER);

        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode node = objectWriter.writeValueAsTree(bigDecimal);
        JsonParser parser = objectMapper.getFactory().createParser(node.toString());

        when(parser.getCurrentName()).thenReturn("test");
        when(parser.getCodec()).thenReturn(mock(ObjectMapper.class));
        when(parser.getCodec().findValueSerializer(BigDecimal.class)).thenReturn(serializer);

        when(generator.getOutputFactory()).thenReturn(parser.getOutputFactory());

        serializer.serialize(bigDecimal, generator, provider, null, format, null);

        assertEquals(JsonGenerator.Event.START_OBJECT, generator.getCurrentEventId());
        assertEquals("test", generator.getCurrentName());
        assertEquals(JsonGenerator.Event.FIELD_NAME, generator.getCurrentEventId());
        assertEquals("number", generator.getCurrentName());
        assertEquals(JsonGenerator.Event.NUMBER, generator.getCurrentEventId());
        assertEquals(bigDecimal.toString(), generator.getCurrentOutputValue().asText());
        assertEquals(JsonGenerator.Event.END_OBJECT, generator.getCurrentEventId());
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    private static class ObjectNumbers {
        private BigDecimal bigDecimal;
        private BigInteger bigInteger;
        private long number;
        private double doubleValue;
        private float floatValue;
        private int intValue;
        private byte byteValue;
        private short shortValue;

        public Number getNumber() {
            return number;
        }

        public BigDecimal getBigDecimal() {
            return bigDecimal;
        }

        public BigInteger getBigInteger() {
            return bigInteger;
        }

        public long getLong() {
            return number;
        }

        public double getDouble() {
            return doubleValue;
        }

        public float getFloat() {
            return floatValue;
        }

        public int getInt() {
            return intValue;
        }

        public byte getByte() {
            return byteValue;
        }

        public short getShort() {
            return shortValue;
        }
    }
}