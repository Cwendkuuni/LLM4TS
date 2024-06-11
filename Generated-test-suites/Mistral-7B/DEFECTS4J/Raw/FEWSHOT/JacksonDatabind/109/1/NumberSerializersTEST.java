package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(11, allDeserializers.size());
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

    @Test
    public void testBaseClass() {
        class Test extends NumberSerializers.Base<Object> {
            Test() {
                super(Integer.class, JsonParser.NumberType.INT, "number");
            }
        }

        Test test = new Test();

        assertEquals(Integer.class, test.handledType());
        assertEquals(JsonParser.NumberType.INT, test._numberType);
        assertEquals("number", test._schemaType);
        assertTrue(test._isInt);
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;

        assertEquals(Short.class, shortSerializer.handledType());
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("number", shortSerializer._schemaType);
        assertTrue(shortSerializer._isInt);

        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode node = objectWriter.writeValueAsTree(new Short("123"));
        assertEquals(123, node.numberValue().intValue());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

        assertEquals(Integer.class, integerSerializer.handledType());
        assertEquals(JsonParser.NumberType.INT, integerSerializer._numberType);
        assertEquals("integer", integerSerializer._schemaType);
        assertTrue(integerSerializer._isInt);

        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode node = objectWriter.writeValueAsTree(new Integer(123));
        assertEquals(123, node.numberValue().intValue());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        assertEquals(Number.class, intLikeSerializer.handledType());
        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        assertEquals("integer", intLikeSerializer._schemaType);
        assertTrue(intLikeSerializer._isInt);

        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode node = objectWriter.writeValueAsTree(new Integer(123));
        assertEquals(123, node.numberValue().intValue());

        node = objectWriter.writeValueAsTree(new Byte((byte) 123));
        assertEquals(123, node.numberValue().intValue());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);

        assertEquals(Long.class, longSerializer.handledType());
        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        assertEquals("number", longSerializer._schemaType);
        assertFalse(longSerializer._isInt);

        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode node = objectWriter.writeValueAsTree(new Long(123L));
        assertEquals(123, node.numberValue().longValue());
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;

        assertEquals(Float.class, floatSerializer.handledType());
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        assertEquals("number", floatSerializer._schemaType);
        assertFalse(floatSerializer._isInt);

        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode node = objectWriter.writeValueAsTree(new Float(123.45f));
        assertEquals(123.45f, Float.intBitsToFloat((int) node.numberValue().bits()), 0.0f);
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);

        assertEquals(Double.class, doubleSerializer.handledType());
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        assertEquals("number", doubleSerializer._schemaType);
        assertFalse(doubleSerializer._isInt);

        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode node = objectWriter.writeValueAsTree(new Double(123.45));
        assertEquals(123.45, node.numberValue().doubleValue(), 0.0);
    }
}