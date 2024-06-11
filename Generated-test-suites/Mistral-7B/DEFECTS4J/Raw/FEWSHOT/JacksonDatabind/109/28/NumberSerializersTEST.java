package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

/**
 * Test cases for NumberSerializers class
 */
public class NumberSerializersTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = JsonMapper.builder().build();
    }

    /**
     * Test for ShortSerializer
     */
    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Short serializableShort = new Short((short) 123);
        Base<?> shortSerializer = mapper.findValueSerializer(Short.class);
        JsonGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(
                Short.class, null).getSerializerProvider().getGeneratorForStartValue();

        shortSerializer.serialize(serializableShort, generator, null);
        assertEquals("123", generator.getOutputBuffer().toString());

        Short deserializedShort = mapper.readValue("123", Short.class);
        assertEquals(serializableShort.shortValue(), deserializedShort.shortValue());
    }

    /**
     * Test for IntegerSerializer
     */
    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer serializableInteger = new Integer(123);
        Base<?> integerSerializer = mapper.findValueSerializer(Integer.class);
        JsonGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(
                Integer.class, null).getSerializerProvider().getGeneratorForStartValue();

        integerSerializer.serialize(serializableInteger, generator, null);
        assertEquals("123", generator.getOutputBuffer().toString());

        Integer deserializedInteger = mapper.readValue("123", Integer.class);
        assertEquals(serializableInteger.intValue(), deserializedInteger.intValue());
    }

    /**
     * Test for IntLikeSerializer
     */
    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        Byte serializableByte = new Byte((byte) 123);
        Short serializableShort = new Short((short) 123);
        Integer serializableInteger = new Integer(123);
        Long serializableLong = new Long(123);
        Base<?> intLikeSerializer = mapper.findValueSerializer(Number.class);

        for (Object serializableNumber : new Object[] {serializableByte, serializableShort, serializableInteger, serializableLong}) {
            JsonGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(
                    Number.class, null).getSerializerProvider().getGeneratorForStartValue();

            intLikeSerializer.serialize(serializableNumber, generator, null);
            assertEquals("123", generator.getOutputBuffer().toString());

            Object deserializedNumber = mapper.readValue("123", Number.class);
            assertEquals(serializableNumber.getClass(), deserializedNumber.getClass());
            assertEquals(serializableNumber.byteValue(), deserializedNumber.byteValue());
            assertEquals(serializableNumber.shortValue(), deserializedNumber.shortValue());
            assertEquals(serializableNumber.intValue(), deserializedNumber.intValue());
            assertEquals(serializableNumber.longValue(), deserializedNumber.longValue());
        }
    }

    /**
     * Test for LongSerializer
     */
    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long serializableLong = new Long(123456);
        Base<?> longSerializer = mapper.findValueSerializer(Long.class);
        JsonGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(
                Long.class, null).getSerializerProvider().getGeneratorForStartValue();

        longSerializer.serialize(serializableLong, generator, null);
        assertEquals("123456", generator.getOutputBuffer().toString());

        Long deserializedLong = mapper.readValue("123456", Long.class);
        assertEquals(serializableLong.longValue(), deserializedLong.longValue());
    }

    /**
     * Test for FloatSerializer
     */
    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float serializableFloat = new Float(123.45F);
        Base<?> floatSerializer = mapper.findValueSerializer(Float.class);
        JsonGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(
                Float.class, null).getSerializerProvider().getGeneratorForStartValue();

        floatSerializer.serialize(serializableFloat, generator, null);
        assertEquals("123.45", generator.getOutputBuffer().toString());

        Float deserializedFloat = mapper.readValue("123.45", Float.class);
        assertEquals(serializableFloat.floatValue(), deserializedFloat.floatValue());
    }

    /**
     * Test for DoubleSerializer
     */
    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double serializableDouble = new Double(123.456);
        Base<?> doubleSerializer = mapper.findValueSerializer(Double.class);
        JsonGenerator generator = mapper.getPolymorphicSerializerProvider().findValueSerializer(
                Double.class, null).getSerializerProvider().getGeneratorForStartValue();

        doubleSerializer.serialize(serializableDouble, generator, null);
        assertEquals("123.456", generator.getOutputBuffer().toString());

        Double deserializedDouble = mapper.readValue("123.456", Double.class);
        assertEquals(serializableDouble.doubleValue(), deserializedDouble.doubleValue());
    }
}