package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() throws Exception {
        Serializable obj = new HashMap<Object, Object>();
        Serializable clonedObj = SerializationUtils.clone(obj);

        assertEquals(obj, clonedObj);
        assertNotSame(obj, clonedObj);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerialAndDeserialEmptyObject() throws Exception {
        Serializable obj = new HashMap<Object, Object>();
        byte[] serializedObj = SerializationUtils.serialize(obj);
        Serializable deserializedObj = SerializationUtils.deserialize(serializedObj);

        assertEquals(obj, deserializedObj);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerialAndDeserialObjectWithValues() throws Exception {
        Serializable obj = new HashMap<Object, Object>();
        obj.put("key1", "value1");
        obj.put("key2", "value2");
        byte[] serializedObj = SerializationUtils.serialize(obj);
        Serializable deserializedObj = SerializationUtils.deserialize(serializedObj);

        Map<Object, Object> deserializedMap = (Map<Object, Object>) deserializedObj;
        Map<Object, Object> originalMap = (Map<Object, Object>) obj;

        assertArrayEquals(originalMap.keySet().toArray(), deserializedMap.keySet().toArray());
        assertArrayEquals(originalMap.values().toArray(), deserializedMap.values().toArray());
    }

    @Test
    public void testSerializeNullObject() throws Exception {
        Serializable obj = null;
        byte[] serializedObj = SerializationUtils.serialize(obj);

        SerializationUtils.deserialize(serializedObj);
        // Expected to not reach this point
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeFail() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeInt(-1); // write an invalid stream header
        out.close();

        SerializationUtils.deserialize(bos.toByteArray());
    }

    @Test(expected = StreamCorruptedException.class)
    public void testDeserializeInvalidStream() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeInt(1); // write a valid stream header
        out.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        bis.read(); // read the first byte, then throw an exception

        SerializationUtils.deserialize(bis);
    }
}