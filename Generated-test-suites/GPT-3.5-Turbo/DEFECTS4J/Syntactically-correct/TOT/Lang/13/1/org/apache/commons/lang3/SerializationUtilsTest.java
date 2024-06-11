import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class SerializationUtilsTest {

    // Test case for clone method with typical use case
    @Test
    public void testClone_typicalUseCase() {
        Person person = new Person("John", 30);
        Person clonedPerson = SerializationUtils.clone(person);

        assertNotSame(person, clonedPerson); // Ensure objects are not the same reference
        assertEquals(person, clonedPerson); // Ensure objects are equal
    }

    // Test case for clone method with null input
    @Test
    public void testClone_nullInput() {
        Person clonedPerson = SerializationUtils.clone(null);

        assertNull(clonedPerson); // Ensure null is returned
    }

    // Test case for clone method with object that is not Serializable
    @Test(expected = SerializationException.class)
    public void testClone_nonSerializableObject() {
        NonSerializableObject nonSerializableObject = new NonSerializableObject();
        SerializationUtils.clone(nonSerializableObject);
    }

    // Test case for serialize method with typical use case
    @Test
    public void testSerialize_typicalUseCase() {
        Person person = new Person("John", 30);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        SerializationUtils.serialize(person, outputStream);

        byte[] expectedBytes = { -84, -19, 0, 5, 115, 114, 0, 10, 80, 101, 114, 115, 111, 110, 0, 18, 76, 106, 97, 118,
                97, 47, 108, 97, 110, 103, 51, 47, 80, 101, 114, 115, 111, 110, -79, -70, 50, 33, 0, 0, 120, 112 };
        assertTrue(Arrays.equals(expectedBytes, outputStream.toByteArray()));
    }

    // Test case for serialize method with null object
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_nullObject() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        SerializationUtils.serialize(null, outputStream);
    }

    // Test case for deserialize method with typical use case
    @Test
    public void testDeserialize_typicalUseCase() {
        byte[] serializedBytes = { -84, -19, 0, 5, 115, 114, 0, 10, 80, 101, 114, 115, 111, 110, 0, 18, 76, 106, 97,
                118, 97, 47, 108, 97, 110, 103, 51, 47, 80, 101, 114, 115, 111, 110, -79, -70, 50, 33, 0, 0, 120, 112 };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedBytes);

        Object deserializedObject = SerializationUtils.deserialize(inputStream);

        assertTrue(deserializedObject instanceof Person);
        Person person = (Person) deserializedObject;
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());
    }

    // Test case for deserialize method with null input stream
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_nullInputStream() {
        SerializationUtils.deserialize(null);
    }

    // Test case for ClassLoaderAwareObjectInputStream.resolveClass method
    @Test
    public void testResolveClass_typicalUseCase() throws IOException, ClassNotFoundException {
        byte[] serializedBytes = { -84, -19, 0, 5, 115, 114, 0, 10, 80, 101, 114, 115, 111, 110, 0, 18, 76, 106, 97,
                118, 97, 47, 108, 97, 110, 103, 51, 47, 80, 101, 114, 115, 111, 110, -79, -70, 50, 33, 0, 0, 120, 112 };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedBytes);
        SerializationUtils.ClassLoaderAwareObjectInputStream in = new SerializationUtils.ClassLoaderAwareObjectInputStream(
                inputStream, getClass().getClassLoader());

        Class<?> resolvedClass = in.resolveClass(in.readClassDescriptor());
        assertEquals(Person.class, resolvedClass);
    }

    // Test case for ClassLoaderAwareObjectInputStream.resolveClass method with invalid class name
    @Test(expected = ClassNotFoundException.class)
    public void testResolveClass_invalidClassName() throws IOException, ClassNotFoundException {
        byte[] serializedBytes = { -84, -19, 0, 5, 115, 114, 0, 10, 80, 101, 114, 115, 111, 110, 0, 18, 76, 106, 97,
                118, 97, 47, 108, 97, 110, 103, 51, 47, 80, 101, 114, 115, 111, 110, -79, -70, 50, 33, 0, 0, 120, 112 };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedBytes);
        SerializationUtils.ClassLoaderAwareObjectInputStream in = new SerializationUtils.ClassLoaderAwareObjectInputStream(
                inputStream, getClass().getClassLoader());

        in.resolveClass(in.readClassDescriptor());
    }

    // Test case for ClassLoaderAwareObjectInputStream.resolveClass method with null input
    @Test(expected = IllegalArgumentException.class)
    public void testResolveClass_nullInput() throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.ClassLoaderAwareObjectInputStream in = new SerializationUtils.ClassLoaderAwareObjectInputStream(
                inputStream, getClass().getClassLoader());

        in.resolveClass(null);
    }

    // Test case for ClassLoaderAwareObjectInputStream Constructor with null input
    @Test(expected = IOException.class)
    public void testClassLoaderAwareObjectInputStream_nullInputStream() throws IOException {
        new SerializationUtils.ClassLoaderAwareObjectInputStream(null, getClass().getClassLoader());
    }

    // Helper class for serialization testing
    private static class Person implements Serializable {
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
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            Person person = (Person) obj;

            if (age != person.age) {
                return false;
            }
            return name != null ? name.equals(person.name) : person.name == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + age;
            return result;
        }
    }

    // Helper class for serialization testing
    private static class NonSerializableObject {
        // Class does not implement Serializable
    }
}