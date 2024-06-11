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
 * Test class for SerializationUtils
 */
public class SerializationUtilsTest {

    /**
     * Test method for {@link SerializationUtils#clone(Object)}
     */
    @Test
    public void testClone() {
        Person person = new Person("John Doe", 30);
        Person clonedPerson = SerializationUtils.clone(person);
        Assert.assertNotSame(person, clonedPerson);
        Assert.assertEquals(person.getName(), clonedPerson.getName());
        Assert.assertEquals(person.getAge(), clonedPerson.getAge());
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object, OutputStream)}
     */
    @Test
    public void testSerializeObjectOutputStream() {
        Person person = new Person("John Doe", 30);
        byte[] serializedData = SerializationUtils.serialize(person);
        Person deserializedPerson = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
        Assert.assertEquals(person.getName(), deserializedPerson.getName());
        Assert.assertEquals(person.getAge(), deserializedPerson.getAge());
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object)}
     */
    @Test
    public void testSerializeByteArrayOutputStream() {
        Person person = new Person("John Doe", 30);
        byte[] serializedData = SerializationUtils.serialize(person);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        Person deserializedPerson = SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(person.getName(), deserializedPerson.getName());
        Assert.assertEquals(person.getAge(), deserializedPerson.getAge());
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)}
     */
    @Test
    public void testDeserializeInputStream() {
        Person person = new Person("John Doe", 30);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(person, outputStream);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Person deserializedPerson = SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(person.getName(), deserializedPerson.getName());
        Assert.assertEquals(person.getAge(), deserializedPerson.getAge());
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])}
     */
    @Test
    public void testDeserializeByteArray() {
        Person person = new Person("John Doe", 30);
        byte[] serializedData = SerializationUtils.serialize(person);
        Person deserializedPerson = SerializationUtils.deserialize(serializedData);
        Assert.assertEquals(person.getName(), deserializedPerson.getName());
        Assert.assertEquals(person.getAge(), deserializedPerson.getAge());
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(ObjectInput)}
     */
    @Test
    public void testDeserializeObjectInput() {
        Person person = new Person("John Doe", 30);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        try {
            objectOutputStream.writeObject(person);
            objectOutputStream.close();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInput objectInput = new ObjectInputStream(inputStream);
            Person deserializedPerson = (Person) SerializationUtils.deserialize(objectInput);
            Assert.assertEquals(person.getName(), deserializedPerson.getName());
            Assert.assertEquals(person.getAge(), deserializedPerson.getAge());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (objectInput != null) {
                    objectInput.close();
                }
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    /**
     * Test method for {@link SerializationUtils#clone(Object)} with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object, OutputStream)} with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])} with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullByteArray() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(ObjectInput)} with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullObjectInput() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with empty input
     */
    @Test
    public void testDeserializeEmptyInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            SerializationUtils.deserialize(inputStream);
        } catch (IOException e) {
            Assert.assertEquals("Unexpected end of file", e.getMessage());
        }
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])} with empty byte array
     */
    @Test
    public void testDeserializeEmptyByteArray() {
        byte[] byteArray = new byte[0];
        try {
            SerializationUtils.deserialize(byteArray);
        } catch (IOException e) {
            Assert.assertEquals("Unexpected end of file", e.getMessage());
        }
    }

    /**
     * Test method for {@link SerializationUtils#clone(Object)} with serializable map
     */
    @Test
    public void testCloneSerializableMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        @SuppressWarnings("unchecked")
        Map<String, Integer> clonedMap = (Map<String, Integer>) SerializationUtils.clone(map);
        Assert.assertEquals(map, clonedMap);
        Assert.assertEquals(map.size(), clonedMap.size());
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Assert.assertEquals(entry.getKey(), clonedMap.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), clonedMap.get(entry.getKey()));
        }
    }

    /**
     * Helper class for testing
     */
    static class Person implements Serializable {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}