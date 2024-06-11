package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test cases for {@link NumberSerializers} class.
 */
public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        // Check serializers are added
        for (Class<?> numberType : new Class<?>[]{
                Integer.class, Integer.TYPE, Long.class, Long.TYPE,
                Byte.class, Byte.TYPE, Short.class, Short.TYPE,
                Double.class, Double.TYPE, Float.class, Float.TYPE
        }) {
            assertTrue("Serializer for " + numberType.getName() + " not added",
                    allDeserializers.containsKey(numberType.getName()));
            assertTrue("Serializer for " + numberType.getName() + " not instanceof " +
                            NumberSerializers.IntegerSerializer.class.getName(),
                    allDeserializers.get(numberType.getName()) instanceof NumberSerializers.IntegerSerializer);
            assertTrue("Serializer for " + numberType.getName() + " not instanceof " +
                            NumberSerializers.LongSerializer.class.getName(),
                    allDeserializers.get(numberType.getName()) instanceof NumberSerializers.LongSerializer);
            assertTrue("Serializer for " + numberType.getName() + " not instanceof " +
                            NumberSerializers.ShortSerializer.class.getName(),
                    allDeserializers.get(numberType.getName()) instanceof NumberSerializers.ShortSerializer);
            assertTrue("Serializer for " + numberType.getName() + " not instanceof " +
                            NumberSerializers.DoubleSerializer.class.getName(),
                    allDeserializers.get(numberType.getName()) instanceof NumberSerializers.DoubleSerializer);
            assertTrue("Serializer for " + numberType.getName() + " not instanceof " +
                            NumberSerializers.FloatSerializer.class.getName(),
                    allDeserializers.get(numberType.getName()) instanceof NumberSerializers.FloatSerializer);
        }

        // Check serializers for int-like types
        assertEquals(NumberSerializers.IntLikeSerializer.instance,
                allDeserializers.get(Byte.class.getName()));
        assertEquals(NumberSerializers.IntLikeSerializer.instance,
                allDeserializers.get(Byte.TYPE.getName()));
        assertEquals(NumberSerializers.IntLikeSerializer.instance,
                allDeserializers.get(Short.class.getName()));
        assertEquals(NumberSerializers.IntLikeSerializer.instance,
                allDeserializers.get(Short.TYPE.getName()));
    }

    @Test
    public void testBaseClassBehavior() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonSerializer<Object> intSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonSerializer<Object> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        ObjectMapper mapper = new ObjectMapper(jsonFactory);

        // Test getSchema method
        JsonNode intSchema = intSerializer.getSchema(mapper.getSerializerProvider(), Object.class);
        JsonNode intLikeSchema = intLikeSerializer.getSchema(mapper.getSerializerProvider(), Object.class);
        assertEquals("integer", intSchema.get("type").asText());
        assertEquals("integer", intLikeSchema.get("type").asText());

        // Test acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(
                mapper.getSerializerProvider(), new Object());
        intSerializer.acceptJsonFormatVisitor(visitor, Object.class);
        intLikeSerializer.acceptJsonFormatVisitor(visitor, Object.class);
        assertTrue(intSerializer.isEnabled(JsonFormat.Value.class, visitor));
        assertTrue(intLikeSerializer.isEnabled(JsonFormat.Value.class, visitor));
        assertEquals("number", visitor.getCurrentValueFormat(intSerializer).getShape());
        assertEquals("number", visitor.getCurrentValueFormat(intLikeSerializer).getShape());
    }

    @Test
    public void testSerializerBehavior() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);

        // Test serializing integers
        JsonSerializer<Object> intSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonSerializer<Object> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        Object testObject = 123;
        String serializedInt = mapper.writeValueAsString(testObject, intSerializer);
        assertEquals("123", serializedInt);

        String serializedIntLike = mapper.writeValueAsString(testObject, intLikeSerializer);
        assertEquals("123", serializedIntLike);

        // Test serializing longs
        JsonSerializer<Object> longSerializer = NumberSerializers.LongSerializer.instance;

        long testLong = 1234567890123456789L;
        String serializedLong = mapper.writeValueAsString(testLong, longSerializer);
        assertEquals("1234567890123456789", serializedLong);

        // Test serializing shorts
        JsonSerializer<Object> shortSerializer = NumberSerializers.ShortSerializer.instance;

        short testShort = 32767;
        String serializedShort = mapper.writeValueAsString(testShort, shortSerializer);
        assertEquals("32767", serializedShort);

        // Test serializing floats
        JsonSerializer<Object> floatSerializer = NumberSerializers.FloatSerializer.instance;

        float testFloat = 1.23f;
        String serializedFloat = mapper.writeValueAsString(testFloat, floatSerializer);
        assertEquals("1.23", serializedFloat);

        // Test serializing doubles
        JsonSerializer<Object> doubleSerializer = NumberSerializers.DoubleSerializer.instance;

        double testDouble = 1.23456789;
        String serializedDouble = mapper.writeValueAsString(testDouble, doubleSerializer);
        assertEquals("1.23456789", serializedDouble);
    }
}