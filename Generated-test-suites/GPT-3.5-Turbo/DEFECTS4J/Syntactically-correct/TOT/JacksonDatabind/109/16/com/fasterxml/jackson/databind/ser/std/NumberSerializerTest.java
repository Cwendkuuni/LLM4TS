import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal value = new BigDecimal("123.45");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.flush();

        String json = out.toString();
        assertEquals("123.45", json);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger value = new BigInteger("123");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.flush();

        String json = out.toString();
        assertEquals("123", json);
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        Long value = 123L;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.flush();

        String json = out.toString();
        assertEquals("123", json);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        Double value = 123.45;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.flush();

        String json = out.toString();
        assertEquals("123.45", json);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        Float value = 123.45f;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.flush();

        String json = out.toString();
        assertEquals("123.45", json);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Integer value = 123;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.flush();

        String json = out.toString();
        assertEquals("123", json);
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        Byte value = 123;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.flush();

        String json = out.toString();
        assertEquals("123", json);
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        Short value = 123;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.flush();

        String json = out.toString();
        assertEquals("123", json);
    }

    @Test
    public void testSerializeOtherNumberType() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number value = new MyCustomNumber(123);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(value, generator, provider);
        generator.flush();

        String json = out.toString();
        assertEquals("123", json);
    }

    private static class MyCustomNumber extends Number {
        private final int value;

        public MyCustomNumber(int value) {
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