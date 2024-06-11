import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case: cloning an object
        SerializableObject obj1 = new SerializableObject("test", 100);
        SerializableObject obj2 = SerializationUtils.clone(obj1);
        Assert.assertNotSame(obj1, obj2);
        Assert.assertEquals(obj1, obj2);

        // Edge case: cloning null object
        SerializableObject obj3 = null;
        SerializableObject obj4 = SerializationUtils.clone(obj3);
        Assert.assertNull(obj4);

        // Error scenario: cloning a non-serializable object
        NonSerializableObject obj5 = new NonSerializableObject();
        try {
            NonSerializableObject obj6 = SerializationUtils.clone(obj5);
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Exception expected
        }
    }

    @Test
    public void testSerialize() {
        // Typical use case: serializing an object to an output stream
        SerializableObject obj1 = new SerializableObject("test", 100);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj1, outputStream);
        byte[] data = outputStream.toByteArray();
        Assert.assertNotNull(data);
        Assert.assertNotEquals(0, data.length);

        // Edge case: serializing a null object
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, outputStream2);
        byte[] data2 = outputStream2.toByteArray();
        Assert.assertNotNull(data2);
        Assert.assertEquals(0, data2.length);
    }

    @Test
    public void testDeserialize() {
        // Typical use case: deserializing an object from an input stream
        SerializableObject obj1 = new SerializableObject("test", 100);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj1, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Object obj2 = SerializationUtils.deserialize(inputStream);
        Assert.assertFalse(obj1 == obj2);
        Assert.assertEquals(obj1, obj2);

        // Edge case: deserializing from null input stream
        try {
            Object obj3 = SerializationUtils.deserialize(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Error scenario: deserializing invalid data
        byte[] invalidData = {1, 2, 3, 4};
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(invalidData);
        try {
            Object obj4 = SerializationUtils.deserialize(inputStream2);
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Exception expected
        }

    }
}

class SerializableObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int value;

    public SerializableObject(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SerializableObject other = (SerializableObject) obj;
        return value == other.value && (name != null && name.equals(other.name));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + value;
        return result;
    }

}

class NonSerializableObject {
    private String name;

    public NonSerializableObject() {
        this.name = "non-serializable";
    }

    public String getName() {
        return name;
    }
}