import org.junit.Assert;
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
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test cloning a Serializable object
        SerializableObject obj = new SerializableObject("test");
        SerializableObject clone = SerializationUtils.clone(obj);
        Assert.assertEquals(obj.getValue(), clone.getValue());

        // Test cloning a null object
        SerializableObject nullObj = null;
        SerializableObject nullClone = SerializationUtils.clone(nullObj);
        Assert.assertNull(nullClone);
    }

    @Test
    public void testSerialize() {
        // Test serializing a Serializable object
        SerializableObject obj = new SerializableObject("test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);

        byte[] serializedData = baos.toByteArray();
        Assert.assertNotNull(serializedData);

        // Test serializing a null object
        ByteArrayOutputStream nullBaos = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, nullBaos);

        byte[] nullSerializedData = nullBaos.toByteArray();
        Assert.assertNotNull(nullSerializedData);
    }

    @Test
    public void testDeserialize() {
        // Test deserializing a serialized object
        SerializableObject obj = new SerializableObject("test");
        byte[] serializedData = SerializationUtils.serialize(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);

        Object deserializedObj = SerializationUtils.deserialize(bais);
        Assert.assertTrue(deserializedObj instanceof SerializableObject);
        SerializableObject deserializedSerializableObject = (SerializableObject) deserializedObj;
        Assert.assertEquals(obj.getValue(), deserializedSerializableObject.getValue());

        // Test deserializing a null object
        byte[] nullSerializedData = new byte[0];
        ByteArrayInputStream nullBais = new ByteArrayInputStream(nullSerializedData);

        Object nullDeserializedObj = SerializationUtils.deserialize(nullBais);
        Assert.assertNull(nullDeserializedObj);
    }

    // Additional test cases for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        SerializableObject obj = new SerializableObject("test");
        SerializationUtils.serialize(obj, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidSerializedData() {
        byte[] serializedData = new byte[] {1, 2, 3, 4};
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        SerializationUtils.deserialize(bais);
    }

    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String value;

        public SerializableObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}