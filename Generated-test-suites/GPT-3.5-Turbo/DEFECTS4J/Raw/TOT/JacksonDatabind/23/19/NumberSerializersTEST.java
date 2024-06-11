
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        HashMap<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws Exception {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();

        // Test positive value
        Short value1 = Short.valueOf("123");
        assertNull(shortSerializer.getSchema(null, null));
        assertEquals("number", shortSerializer._schemaType);
        assertFalse(shortSerializer._isInt);

        // Test negative value
        Short value2 = Short.valueOf("-456");

        // Test zero value
        Short value3 = Short.valueOf("0");

        // Test large value
        Short value4 = Short.valueOf("32760");

        // Test serialization
        // TODO: Implement serialization test

        // Test contextual serialization
        // TODO: Implement contextual serialization test
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();

        // Test positive value
        Integer value1 = Integer.valueOf("123");
        assertNull(integerSerializer.getSchema(null, null));
        assertEquals("integer", integerSerializer._schemaType);
        assertTrue(integerSerializer._isInt);

        // Test negative value
        Integer value2 = Integer.valueOf("-456");

        // Test zero value
        Integer value3 = Integer.valueOf("0");

        // Test large value
        Integer value4 = Integer.valueOf("1000000");

        // Test serialization
        // TODO: Implement serialization test

        // Test contextual serialization
        // TODO: Implement contextual serialization test
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();

        // Test positive value
        Integer value1 = Integer.valueOf("123");
        assertNull(intLikeSerializer.getSchema(null, null));
        assertEquals("integer", intLikeSerializer._schemaType);
        assertTrue(intLikeSerializer._isInt);

        // Test negative value
        Integer value2 = Integer.valueOf("-456");

        // Test zero value
        Integer value3 = Integer.valueOf("0");

        // Test large value
        Integer value4 = Integer.valueOf("1000000");

        // Test serialization
        // TODO: Implement serialization test

        // Test contextual serialization
        // TODO: Implement contextual serialization test
    }

    @Test
    public void testLongSerializer() throws Exception {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();

        // Test positive value
        Long value1 = Long.valueOf("123");
        assertNull(longSerializer.getSchema(null, null));
        assertEquals("number", longSerializer._schemaType);
        assertFalse(longSerializer._isInt);

        // Test negative value
        Long value2 = Long.valueOf("-456");

        // Test zero value
        Long value3 = Long.valueOf("0");

        // Test large value
        Long value4 = Long.valueOf("99999999999999999");

        // Test serialization
        // TODO: Implement serialization test

        // Test contextual serialization
        // TODO: Implement contextual serialization test
    }

    @Test
    public void testFloatSerializer() throws Exception {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();

        // Test positive value
        Float value1 = Float.valueOf("3.14");
        assertNull(floatSerializer.getSchema(null, null));
        assertEquals("number", floatSerializer._schemaType);
        assertFalse(floatSerializer._isInt);

        // Test negative value
        Float value2 = Float.valueOf("-1.23");

        // Test zero value
        Float value3 = Float.valueOf("0.0");

        // Test large value
        Float value4 = Float.valueOf("1000000.01");

        // Test serialization
        // TODO: Implement serialization test

        // Test contextual serialization
        // TODO: Implement contextual serialization test
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();

        // Test positive value
        Double value1 = Double.valueOf("3.14");
        assertNull(doubleSerializer.getSchema(null, null));
        assertEquals("number", doubleSerializer._schemaType);
        assertFalse(doubleSerializer._isInt);

        // Test negative value
        Double value2 = Double.valueOf("-1.23");

        // Test zero value
        Double value3 = Double.valueOf("0.0");

        // Test large value
        Double value4 = Double.valueOf("1000000.01");

        // Test serialization
        // TODO: Implement serialization test

        // Test contextual serialization
        // TODO: Implement contextual serialization test
    }

    // TODO: Add more test methods for other methods and scenarios

}
