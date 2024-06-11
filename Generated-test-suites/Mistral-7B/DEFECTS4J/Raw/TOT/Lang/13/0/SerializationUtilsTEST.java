package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.clone.Cloner;
import org.apache.commons.lang3.exception.SerializationException;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    /**
     * Test cloning an object using SerializationUtils.clone() method
     */
    @Test
    public void testClone() {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("One", 1);
        originalMap.put("Two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        // Test equality
        Assert.assertNotSame(originalMap, clonedMap);
        Assert.assertEquals(originalMap, clonedMap);

        // Test values
        Assert.assertEquals(originalMap.get("One"), clonedMap.get("One"));
        Assert.assertEquals(originalMap.get("Two"), clonedMap.get("Two"));

        // Test deep cloning
        originalMap.put("Three", 3);
        Assert.assertNotEquals(clonedMap.get("Three"), originalMap.get("Three"));
    }

    /**
     * Test serialization and deserialization of a simple object
     */
    @Test
    public void testSimpleObjectSerializationDeserialization() {
        SimpleObject simpleObject = new SimpleObject("TestObject");

        byte[] serializedObject = SerializationUtils.serialize(simpleObject);
        SimpleObject deserializedObject = (SimpleObject) SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));

        // Test equality
        Assert.assertNotSame(simpleObject, deserializedObject);
        Assert.assertEquals(simpleObject, deserializedObject);

        // Test values
        Assert.assertEquals(simpleObject.getName(), deserializedObject.getName());
    }

    /**
     * Test serialization and deserialization of a complex object with exceptions handling
     */
    @Test
    public void testComplexObjectSerializationDeserialization() {
        ComplexObject complexObject = new ComplexObject();
        complexObject.setName("TestComplexObject");
        complexObject.setNestedObject(new NestedObject());

        try {
            byte[] serializedObject = SerializationUtils.serialize(complexObject);
            Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));

            // Test equality
            Assert.assertNotSame(complexObject, deserializedObject);
            Assert.assertEquals(complexObject, deserializedObject);

            // Test values
            Assert.assertEquals(complexObject.getName(), deserializedObject.getName());
            Assert.assertEquals(complexObject.getNestedObject(), deserializedObject.getNestedObject());

        } catch (SerializationException e) {
            // Test exception message
            Assert.assertEquals("SerializationException while reading cloned object data", e.getMessage());
        }
    }

    /**
     * Test cloning an object using SerializationUtils.clone() method with a custom Cloner
     */
    @Test
    public void testCloneWithCustomCloner() {
        MyCustomSerializableObject myObject = new MyCustomSerializableObject("TestMyObject");

        MyCustomSerializableObject clonedObject = (MyCustomSerializableObject) SerializationUtils.clone(myObject, new Cloner());

        // Test equality
        Assert.assertNotSame(myObject, clonedObject);
        Assert.assertEquals(myObject, clonedObject);

        // Test values
        Assert.assertEquals(myObject.getName(), clonedObject.getName());
    }

    /**
     * Test custom ClassLoader in deserialization process
     */
    @Test
    public void testCustomClassLoaderDeserialization() {
        CustomSerializableObject customSerializableObject = new CustomSerializableObject();
        customSerializableObject.setName("TestCustomObject");

        // Serialize the object to a byte array
        byte[] serializedObject = SerializationUtils.serialize(customSerializableObject);

        // Create a ByteArrayInputStream with the serialized object
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);

        // Create a ClassLoaderAwareObjectInputStream with a custom ClassLoader
        ClassLoader customClassLoader = this.getClass().getClassLoader();
        ObjectInputStream customObjectInputStream = new SerializationUtils.ClassLoaderAwareObjectInputStream(byteArrayInputStream, customClassLoader);

        // Deserialize the object from the custom ObjectInputStream
        Object deserializedObject = null;
        try {
            deserializedObject = customObjectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test equality
        Assert.assertNotSame(customSerializableObject, deserializedObject);
        Assert.assertEquals(customSerializableObject, deserializedObject);

        // Test values
        Assert.assertEquals(customSerializableObject.getName(), deserializedObject.getName());
    }

    /**
     * Test exception handling during deserialization
     */
    @Test(expected = SerializationException.class)
    public void testExceptionDuringDeserialization() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        // Attempt to deserialize an object from the empty ByteArrayInputStream
        Object deserializedObject = null;
        try {
            deserializedObject = objectInputStream.readObject();
        } catch (Exception e) {
            // Expected SerializationException
        }
    }
}

class SimpleObject implements Serializable {
    private String name;

    public SimpleObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class ComplexObject implements Serializable {
    private String name;
    private NestedObject nestedObject;

    public ComplexObject() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setNestedObject(NestedObject nestedObject) {
        this.nestedObject = nestedObject;
    }

    public String getName() {
        return name;
    }

    public NestedObject getNestedObject() {
        return nestedObject;
    }
}

class NestedObject implements Serializable {}

class MyCustomSerializableObject implements Serializable {
    private String name;

    public MyCustomSerializableObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class CustomSerializableObject implements Serializable {
    private String name;

    public CustomSerializableObject() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private transient Object nestedObject;

    public Object getNestedObject() {
        return nestedObject;
    }
}