package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws InstantiationException, IllegalAccessException, SerializableException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap);
        Assert.assertNotSame(originalMap, clonedMap);
    }

    @Test
    public void testSerializationAndDeserialization() throws SerializableException, IOException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        byte[] serializedData = SerializationUtils.serialize(originalMap);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        Assert.assertEquals(originalMap, deserializedMap);
        Assert.assertNotSame(originalMap, deserializedMap);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws SerializableException, IOException {
        Object obj = new Object() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused")
            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException("This class should not be serialized.");
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(obj, baos);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializationException() throws SerializableException, IOException {
        byte[] invalidData = new byte[]{-1, 1, 2, 3};

        ByteArrayInputStream bais = new ByteArrayInputStream(invalidData);
        SerializationUtils.deserialize(bais);
    }
}