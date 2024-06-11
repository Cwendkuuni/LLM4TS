import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SerializationUtilsTest {

    // clone
    @Test
    public void testCloneNullObject() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testCloneSerializableObject() {
        TestObject originalObject = new TestObject("test");
        TestObject clonedObject = SerializationUtils.clone(originalObject);

        assertEquals(originalObject, clonedObject);
    }

    // serialize
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObjectToOutputStream() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test
    public void testSerializeObjectToOutputStream() {
        TestObject object = new TestObject("test");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        SerializationUtils.serialize(object, outputStream);

        byte[] expectedBytes = getBytesFromObject(object);
        byte[] actualBytes = outputStream.toByteArray();
        assertEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObjectToByteArray() {
        SerializationUtils.serialize(null);
    }

    @Test
    public void testSerializeObjectToByteArray() {
        TestObject object = new TestObject("test");
        byte[] expectedBytes = getBytesFromObject(object);
        byte[] actualBytes = SerializationUtils.serialize(object);

        assertEquals(expectedBytes, actualBytes);
    }

    // deserialize
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testDeserializeObjectFromInputStream() {
        TestObject originalObject = new TestObject("test");
        byte[] objectBytes = getBytesFromObject(originalObject);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(objectBytes);

        TestObject deserializedObject = (TestObject) SerializationUtils.deserialize(inputStream);

        assertEquals(originalObject, deserializedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullByteArray() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testDeserializeObjectFromByteArray() {
        TestObject originalObject = new TestObject("test");
        byte[] objectBytes = getBytesFromObject(originalObject);

        TestObject deserializedObject = (TestObject) SerializationUtils.deserialize(objectBytes);

        assertEquals(originalObject, deserializedObject);
    }

    // Helper method to convert object to byte array
    private byte[] getBytesFromObject(Serializable object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Error serializing object", e);
        }
    }

    // Helper class for testing
    private static class TestObject implements Serializable {
        private final String data;

        public TestObject(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TestObject other = (TestObject) obj;
            return data.equals(other.data);
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }
    }

}