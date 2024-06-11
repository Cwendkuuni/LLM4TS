package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test cases for NumberSerializers class.
 */
public class NumberSerializersTest {

    /**
     * Test for addAll method.
     */
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        // Verify that all serializers have been added to the map.
        assertEquals(10, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    /**
     * Test for IntegerSerializer.
     */
    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonSerializer<Object> intSerializer = NumberSerializers.findValueSerializer(
                Object.class, Integer.TYPE, null);
        assertEquals(IntegerSerializer.class, intSerializer.getClass());

        IntegerSerializer intSerializerInstance = (IntegerSerializer) intSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Integer.class, JsonParser.NumberType.INT, "integer");
        assertEquals(testInstance, intSerializerInstance);
    }

    /**
     * Test for LongSerializer.
     */
    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonSerializer<Object> longSerializer = NumberSerializers.findValueSerializer(
                Object.class, Long.TYPE, null);
        assertEquals(LongSerializer.class, longSerializer.getClass());

        LongSerializer longSerializerInstance = (LongSerializer) longSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Long.class, JsonParser.NumberType.LONG, "number");
        assertEquals(testInstance, longSerializerInstance);
    }

    /**
     * Test for ShortSerializer.
     */
    @Test
    public void testShortSerializer() throws JsonProcessingException {
        JsonSerializer<Object> shortSerializer = NumberSerializers.findValueSerializer(
                Object.class, Short.TYPE, null);
        assertEquals(ShortSerializer.class, shortSerializer.getClass());

        ShortSerializer shortSerializerInstance = (ShortSerializer) shortSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Short.class, JsonParser.NumberType.INT, "integer");
        assertEquals(testInstance, shortSerializerInstance);
    }

    /**
     * Test for FloatSerializer.
     */
    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        JsonSerializer<Object> floatSerializer = NumberSerializers.findValueSerializer(
                Object.class, Float.TYPE, null);
        assertEquals(FloatSerializer.class, floatSerializer.getClass());

        FloatSerializer floatSerializerInstance = (FloatSerializer) floatSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Float.class, JsonParser.NumberType.FLOAT, "number");
        assertEquals(testInstance, floatSerializerInstance);
    }

    /**
     * Test for DoubleSerializer.
     */
    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        JsonSerializer<Object> doubleSerializer = NumberSerializers.findValueSerializer(
                Object.class, Double.TYPE, null);
        assertEquals(DoubleSerializer.class, doubleSerializer.getClass());

        DoubleSerializer doubleSerializerInstance = (DoubleSerializer) doubleSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Double.class, JsonParser.NumberType.DOUBLE, "number");
        assertEquals(testInstance, doubleSerializerInstance);
    }

    /**
     * Test for IntegerSerializer with custom schema.
     */
    @Test
    public void testIntegerSerializerWithCustomSchema() throws JsonProcessingException {
        JsonSerializer<Object> intSerializer = NumberSerializers.findValueSerializer(
                Object.class, Integer.TYPE, "customSchema");
        assertEquals(IntegerSerializer.class, intSerializer.getClass());

        IntegerSerializer intSerializerInstance = (IntegerSerializer) intSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Integer.class, JsonParser.NumberType.INT, "customSchema");
        assertEquals(testInstance, intSerializerInstance);
    }

    /**
     * Test for LongSerializer with custom schema.
     */
    @Test
    public void testLongSerializerWithCustomSchema() throws JsonProcessingException {
        JsonSerializer<Object> longSerializer = NumberSerializers.findValueSerializer(
                Object.class, Long.TYPE, "customSchema");
        assertEquals(LongSerializer.class, longSerializer.getClass());

        LongSerializer longSerializerInstance = (LongSerializer) longSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Long.class, JsonParser.NumberType.LONG, "customSchema");
        assertEquals(testInstance, longSerializerInstance);
    }

    /**
     * Test for ShortSerializer with custom schema.
     */
    @Test
    public void testShortSerializerWithCustomSchema() throws JsonProcessingException {
        JsonSerializer<Object> shortSerializer = NumberSerializers.findValueSerializer(
                Object.class, Short.TYPE, "customSchema");
        assertEquals(ShortSerializer.class, shortSerializer.getClass());

        ShortSerializer shortSerializerInstance = (ShortSerializer) shortSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Short.class, JsonParser.NumberType.INT, "customSchema");
        assertEquals(testInstance, shortSerializerInstance);
    }

    /**
     * Test for FloatSerializer with custom schema.
     */
    @Test
    public void testFloatSerializerWithCustomSchema() throws JsonProcessingException {
        JsonSerializer<Object> floatSerializer = NumberSerializers.findValueSerializer(
                Object.class, Float.TYPE, "customSchema");
        assertEquals(FloatSerializer.class, floatSerializer.getClass());

        FloatSerializer floatSerializerInstance = (FloatSerializer) floatSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Float.class, JsonParser.NumberType.FLOAT, "customSchema");
        assertEquals(testInstance, floatSerializerInstance);
    }

    /**
     * Test for DoubleSerializer with custom schema.
     */
    @Test
    public void testDoubleSerializerWithCustomSchema() throws JsonProcessingException {
        JsonSerializer<Object> doubleSerializer = NumberSerializers.findValueSerializer(
                Object.class, Double.TYPE, "customSchema");
        assertEquals(DoubleSerializer.class, doubleSerializer.getClass());

        DoubleSerializer doubleSerializerInstance = (DoubleSerializer) doubleSerializer;
        ObjectReadWriter.Base testInstance = new ObjectReadWriter.Base(Double.class, JsonParser.NumberType.DOUBLE, "customSchema");
        assertEquals(testInstance, doubleSerializerInstance);
    }

    /**
     * Test for custom serializer registration.
     */
    @Test
    public void testCustomSerializerRegistration() throws JsonProcessingException {
        // Create custom serializer
        MyNumberSerializer myNumberSerializer = new MyNumberSerializer();

        // Register custom serializer
        NumberSerializers.addSerializer(MyNumber.class, MyNumberSerializer.class);

        // Find custom serializer
        JsonSerializer<Object> customSerializer = NumberSerializers.findValueSerializer(
                Object.class, MyNumber.class, null);
        assertEquals(MyNumberSerializer.class, customSerializer.getClass());

        // Verify serialization
        MyNumber myNumber = new MyNumber(123);
        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(myNumber);
        assertEquals("123", json);
    }
}

class MyNumberSerializer extends Base<MyNumber> {
    public MyNumberSerializer() {
        super(MyNumber.class, JsonParser.NumberType.INT, "number");
    }

    @Override
    public void serialize(MyNumber value, JsonGenerator gen,
            SerializerProvider provider) throws IOException {
        gen.writeNumber(value.getValue());
    }
}

class MyNumber {
    private int value;

    public MyNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}