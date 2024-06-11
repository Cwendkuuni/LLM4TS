import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal value = new BigDecimal("123.456");
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of BigDecimal
            // This is a typical use case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger value = new BigInteger("1234567890");
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of BigInteger
            // This is a typical use case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        Long value = Long.MAX_VALUE;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of Long
            // This is a typical use case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeDouble() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        Double value = Double.MAX_VALUE;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of Double
            // This is a typical use case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeFloat() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        Float value = Float.MAX_VALUE;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of Float
            // This is a typical use case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeInteger() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Integer value = Integer.MAX_VALUE;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of Integer
            // This is a typical use case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeByte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        Byte value = Byte.MAX_VALUE;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of Byte
            // This is a typical use case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeShort() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        Short value = Short.MAX_VALUE;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of Short
            // This is a typical use case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeOtherNumberType() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number value = new TestNumber();
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of custom Number type
            // This is a typical use case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeNullValue() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number value = null;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of null value
            // This is an edge case
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeUnsupportedNumberType() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number value = new TestNumber();
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            // Test serialization of unsupported Number type
            // This is an error scenario
            serializer.serialize(value, generator, provider);
            fail("Expected JsonMappingException was not thrown");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (JsonMappingException e) {
            // This is expected
            assertEquals("Unable to serialize Number", e.getMessage());
        }
    }
    
    private class TestNumber extends Number {
        private static final long serialVersionUID = 1L;

        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public double doubleValue() {
            return 0;
        }
    }

}