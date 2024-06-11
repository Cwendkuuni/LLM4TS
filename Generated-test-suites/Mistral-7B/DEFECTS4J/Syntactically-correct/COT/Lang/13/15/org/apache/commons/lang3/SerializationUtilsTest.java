package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SerializationUtilsTest {

    @Test
    public void testSerializeAndDeserializeNullObject() {
        Serializable nullObject = null;
        byte[] serializedNullObject = SerializationUtils.serialize(nullObject);
        Object deserializedNullObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedNullObject));
        assertEquals(null, deserializedNullObject);
    }

    @Test
    public void testCloneNullObject() {
        Serializable nullObject = null;
        try {
            SerializationUtils.clone(nullObject);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // expected
        }
    }

    @Test
    public void testSerializeAndDeserializeEmptyArray() {
        Object[] emptyArray = new Object[0];
        byte[] serializedEmptyArray = SerializationUtils.serialize(emptyArray);
        Object deserializedEmptyArray = SerializationUtils.deserialize(new ByteArrayInputStream(serializedEmptyArray));
        assertArrayEquals(new Object[0], (Object[]) deserializedEmptyArray);
    }

    @Test
    public void testSerializeAndDeserializePrimitiveTypes() {
        int intValue = 1;
        double doubleValue = 1.2;
        byte[] serializedInt = SerializationUtils.serialize(intValue);
        int deserializedInt = (int) SerializationUtils.deserialize(new ByteArrayInputStream(serializedInt))[0];
        assertEquals(intValue, deserializedInt);

        byte[] serializedDouble = SerializationUtils.serialize(doubleValue);
        double deserializedDouble = (double) SerializationUtils.deserialize(new ByteArrayInputStream(serializedDouble))[0];
        assertEquals(doubleValue, deserializedDouble, 0.001);
    }

    @Test
    public void testSerializeAndDeserializeMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);

        byte[] serializedMap = SerializationUtils.serialize(map);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedMap));
        assertEquals(map, deserializedMap);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeAndDeserializeException() {
        throw new RuntimeException("Throwing exception to test exception handling");
    }

    @Test
    public void testDeserializeWithCustomClassLoader() {
        MyObject myObject = new MyObject();
        byte[] serializedMyObject = SerializationUtils.serialize(myObject);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedMyObject);
        ClassLoader customClassLoader = this.getClass().getClassLoader();
        Object deserializedMyObject = SerializationUtils.deserialize(bais, customClassLoader);
        assertEquals(myObject, deserializedMyObject);
    }

}

class MyObject implements Serializable {
    // any complex object that needs custom classloader for deserialization
}