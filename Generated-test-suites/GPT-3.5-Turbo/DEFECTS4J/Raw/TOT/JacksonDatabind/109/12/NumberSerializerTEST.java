
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonSerializer<Object> serializer = numberSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        BigDecimal value = new BigDecimal("123.4567");
        String json = serialize(value, serializer);
        Assert.assertEquals("123.4567", json);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonSerializer<Object> serializer = numberSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        BigInteger value = new BigInteger("123456789");
        String json = serialize(value, serializer);
        Assert.assertEquals("123456789", json);
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        JsonSerializer<Object> serializer = numberSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        Long value = 123456789L;
        String json = serialize(value, serializer);
        Assert.assertEquals("123456789", json);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonSerializer<Object> serializer = numberSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        Double value = 123.4567;
        String json = serialize(value, serializer);
        Assert.assertEquals("123.4567", json);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        JsonSerializer<Object> serializer = numberSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        Float value = 123.45f;
        String json = serialize(value, serializer);
        Assert.assertEquals("123.45", json);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonSerializer<Object> serializer = numberSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        Integer value = 123;
        String json = serialize(value, serializer);
        Assert.assertEquals("123", json);
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        JsonSerializer<Object> serializer = numberSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        Byte value = 12;
        String json = serialize(value, serializer);
        Assert.assertEquals("12", json);
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Short.class);
        JsonSerializer<Object> serializer = numberSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        Short value = 123;
        String json = serialize(value, serializer);
        Assert.assertEquals("123", json);
    }

    @Test
    public void testSerializeOtherNumberTypes() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonSerializer<Object> serializer = numberSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        Number value = new TestNumber(1234);
        String json = serialize(value, serializer);
        Assert.assertEquals("1234", json);
    }

    private String serialize(Object value, JsonSerializer<Object> serializer) throws IOException {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper.writeValueAsString(serializer.serialize(value, objectMapper.getJsonFactory().createGenerator(System.out)));
    }

    private static class TestNumber extends Number {

        private final int value;

        public TestNumber(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public float floatValue() {
            return value;
        }

        @Override
        public double doubleValue() {
            return value;
        }
    }
}
