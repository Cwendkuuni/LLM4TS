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

public class SerializationUtilsTest {

    private static final byte[] SERIALIZED_OBJECT = TestUtils.loadResourceAsByteArray("SerializationUtilsTest_TestObject.ser");

    @Test
    public void testClone() {
        Object original = new TestObject("Original");
        Object cloned = SerializationUtils.clone(original);

        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original.getClass(), cloned.getClass());
        Assert.assertEquals(original.toString(), cloned.toString());

        TestObject clonedObject = (TestObject) cloned;
        Assert.assertNotNull(clonedObject.map);
        Assert.assertEquals(original.map.size(), clonedObject.map.size());
        for (Map.Entry<String, Integer> entry : original.map.entrySet()) {
            Assert.assertEquals(entry.getKey(), clonedObject.map.get(entry.getKey()));
        }
    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        TestObject original = new TestObject("Original");
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        Assert.assertNotSame(original, deserialized);
        Assert.assertEquals(original.getClass(), deserialized.getClass());
        Assert.assertEquals(original.toString(), deserialized.toString());

        TestObject deserializedObject = (TestObject) deserialized;
        Assert.assertNotNull(deserializedObject.map);
        Assert.assertEquals(original.map.size(), deserializedObject.map.size());
        for (Map.Entry<String, Integer> entry : original.map.entrySet()) {
            Assert.assertEquals(entry.getKey(), deserializedObject.map.get(entry.getKey()));
        }
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeFailed() throws IOException, ClassNotFoundException {
        TestObject original = new TestObject(null);
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
    }

    @Test
    public void testSerializeNullObject() {
        Object nullObject = null;
        byte[] serialized = SerializationUtils.serialize(nullObject);
        try {
            Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
            Assert.fail("Expected SerializationException but didn't get one.");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeNullInput() {
        try {
            Object deserialized = SerializationUtils.deserialize(null);
            Assert.fail("Expected IOException but didn't get one.");
        } catch (IOException e) {
            // Expected
        }
    }

    static class TestObject implements Serializable {
        private static final long serialVersionUID = -1522183538248357572L;
        private String name;
        private Map<String, Integer> map = new HashMap<>();

        TestObject(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "TestObject{" +
                    "name='" + name + '\'' +
                    ", map=" + map +
                    '}';
        }
    }
}