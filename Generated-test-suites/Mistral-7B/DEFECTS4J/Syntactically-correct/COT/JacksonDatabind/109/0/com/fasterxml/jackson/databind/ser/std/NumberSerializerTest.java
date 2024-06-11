import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.JavaType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumberSerializerTest {

    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;

    @Before
    public void setup() {
        serializerProvider = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
    }

    @Test
    public void testInstance() {
        Assert.assertEquals(NumberSerializer.class, NumberSerializer.instance.getClass());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        serializer.serialize(new BigDecimal("123.45"), jsonGenerator, serializerProvider);
        verifySerializeNumber("123.45", jsonGenerator);
    }

    @Test
    public void testSerializeBigInteger() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        serializer.serialize(new BigInteger("456"), jsonGenerator, serializerProvider);
        verifySerializeNumber("456", jsonGenerator);
    }

    @Test
    public void testSerializeLong() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        serializer.serialize(123, jsonGenerator, serializerProvider);
        verifySerializeNumber("123", jsonGenerator);
    }

    @Test
    public void testSerializeDouble() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        serializer.serialize(123.45, jsonGenerator, serializerProvider);
        verifySerializeNumber("123.45", jsonGenerator);
    }

    @Test
    public void testSerializeFloat() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        serializer.serialize(123.45f, jsonGenerator, serializerProvider);
        verifySerializeNumber("123.45", jsonGenerator);
    }

    @Test
    public void testSerializeInteger() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        serializer.serialize(123, jsonGenerator, serializerProvider);
        verifySerializeNumber("123", jsonGenerator);
    }

    @Test
    public void testSerializeByte() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        serializer.serialize((byte) 123, jsonGenerator, serializerProvider);
        verifySerializeNumber("123", jsonGenerator);
    }

    @Test
    public void testSerializeShort() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        serializer.serialize((short) 123, jsonGenerator, serializerProvider);
        verifySerializeNumber("123", jsonGenerator);
    }

    @Test
    public void testSerializeCustomNumber() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(CustomNumber.class);
        serializer.serialize(new CustomNumber(123), jsonGenerator, serializerProvider);
        verifySerializeNumber("123", jsonGenerator);
    }

    @Test(expected = IOException.class)
    public void testSerializeNull() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.serialize(null, jsonGenerator, serializerProvider);
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializeNaN() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        serializer.serialize(Double.NaN, jsonGenerator, serializerProvider);
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializeInfinity() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        serializer.serialize(Double.POSITIVE_INFINITY, jsonGenerator, serializerProvider);
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializeNegativeInfinity() throws IOException, JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        serializer.serialize(Double.NEGATIVE_INFINITY, jsonGenerator, serializerProvider);
    }

    @Test
    public void testCreateContextualWithFormat() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        when(serializerProvider.findValueType(JsonToken.VALUE_STRING)).thenReturn(String.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.createContextual(serializerProvider, null);
        Assert.assertEquals(ToStringSerializer.instance, serializer);
    }

    @Test
    public void testCreateContextualWithoutFormat() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.createContextual(serializerProvider, null);
        Assert.assertEquals(serializer, serializer);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Assert.assertEquals("number", serializerProvider.constructValueType(Number.class).getName());
        Assert.assertEquals("number", NumberSerializer.instance.getSchema(serializerProvider, Number.class).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.expectNumberFormat(Number.class)).thenReturn(null);
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    private void verifySerializeNumber(String expectedString, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.flush();
        String actualString = jsonGenerator.getOutputFactory().createSerializerProvider().getValueAsString(jsonGenerator.getOutputBuffer());
        Assert.assertEquals(expectedString, actualString);
    }

    private static class CustomNumber extends Number {
        private static final long serialVersionUID = 1L;

        private final int value;

        public CustomNumber(int value) {
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

        @Override
        public short shortValue() {
            return (short) value;
        }

        @Override
        public byte byteValue() {
            return (byte) value;
        }

        @Override
        public BigDecimal bigDecimalValue() {
            return new BigDecimal(String.valueOf(value));
        }

        @Override
        public BigInteger bigIntegerValue() {
            return new BigInteger(String.valueOf(value));
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public Class<? extends Number> getClass() {
            return Number.class;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            CustomNumber other = (CustomNumber) obj;
            return value == other.value;
        }
    }
}