package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for SerializationUtils.
 */
public class SerializationUtilsTest {

    @Test
    public void testClone() {
        String original = "Original String";
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals("Cloned String should be the same as the original.", original, cloned);
    }

    @Test
    public void testSerializeAndDeserializeString() {
        String original = "Original String";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        Assert.assertEquals("Deserialized String should be the same as the original.", original, deserialized);
    }

    @Test
    public void testSerializeAndDeserializeHashMap() {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("One", 1);
        originalMap.put("Two", 2);

        byte[] serialized = SerializationUtils.serialize(originalMap);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(serialized);
        Assert.assertEquals("Deserialized Map should be the same as the original.", originalMap, deserializedMap);
    }

    @Test
    public void testNullObject() {
        Assert.assertNull("SerializationUtils.clone should return null for a null object.", SerializationUtils.clone(null));
    }

    @Test(expected = SerializationException.class)
    public void testFailingSerialization() {
        Object obj = new Object() {
            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException("Simulated serialization exception.");
            }
        };
        byte[] serialized = new byte[1024];
        SerializationUtils.serialize(obj, new ByteArrayOutputStream(serialized.length));
    }

    @Test(expected = SerializationException.class)
    public void testFailingDeserialization() {
        byte[] serialized = new byte[] {1, 2, 3};
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serialized));
        in.readObject();
    }

    @Test
    public void testIOExceptionOnClose() {
        byte[] serialized = SerializationUtils.serialize(new byte[1024]);
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = new ObjectInputStream(bais);
        try {
            ois.readObject();
        } catch (Exception e) {
            // swallow the exception
        }
        try {
            ois.close();
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown when closing the InputStream.");
        }
    }

    @Test
    public void testClassNotFoundException() {
        byte[] serialized = SerializationUtils.serialize(new Object() {
            private void writeObject(ObjectOutputStream out) throws IOException {
            }
        });
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais, getClass().getClassLoader());
        try {
            in.readObject();
            Assert.fail("ClassNotFoundException should be thrown.");
        } catch (ClassNotFoundException e) {
            // Expected exception
        }
    }

}