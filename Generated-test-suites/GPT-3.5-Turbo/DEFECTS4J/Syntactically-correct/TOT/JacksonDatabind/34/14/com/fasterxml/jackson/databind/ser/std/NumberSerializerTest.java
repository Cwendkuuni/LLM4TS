import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        BigDecimal value = new BigDecimal(123.456);
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(eq(value));
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        BigInteger value = BigInteger.valueOf(1234567890);
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(eq(value));
    }

    @Test
    public void testSerializeInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Integer value = 123;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(eq(value.intValue()));
    }

    @Test
    public void testSerializeLong() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Long value = 123L;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(eq(value.longValue()));
    }

    @Test
    public void testSerializeDouble() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Double value = 123.456;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(eq(value.doubleValue()));
    }

    @Test
    public void testSerializeFloat() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Float value = 123.456f;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(eq(value.floatValue()));
    }

    @Test
    public void testSerializeByte() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Byte value = 123;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(eq(value.intValue()));
    }

    @Test
    public void testSerializeShort() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Short value = 123;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(eq(value.intValue()));
    }

    @Test
    public void testSerializeOtherNumberType() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Number value = new MyCustomNumber(12345);
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(eq(value.toString()));
    }

    @Test
    public void testGetSchemaForBigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonNode expectedSchema = new ObjectMapper().createObjectNode().put("type", "integer");

        JsonNode schema = serializer.getSchema(provider, BigInteger.class);

        assert(schema.equals(expectedSchema));
    }

    @Test
    public void testGetSchemaForBigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonNode expectedSchema = new ObjectMapper().createObjectNode().put("type", "number");

        JsonNode schema = serializer.getSchema(provider, BigDecimal.class);

        assert(schema.equals(expectedSchema));
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(eq(typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).visitFloatFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_INTEGER));
    }

    @Test
    public void testAcceptJsonFormatVisitorForOtherNumberType() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(eq(typeHint));
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