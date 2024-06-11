
import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SerializationUtilsTest {

    // test cases for clone method
    @Test
    public void testClone() {
        // test with null object
        Object nullClone = SerializationUtils.clone(null);
        Assert.assertNull(nullClone);

        // test with non-serializable object
        NotSerializableObject obj = new NotSerializableObject();
        try {
            SerializationUtils.clone(obj);
        } catch (SerializationException e) {
            Assert.assertTrue(true);
        }

        // test with serializable object
        SerializableObject serObj = new SerializableObject();
        SerializableObject clone = SerializationUtils.clone(serObj);
        Assert.assertEquals(serObj, clone);
    }

    // test cases for serialize methods
    @Test
    public void testSerialize() {
        // test with null object
        ByteArrayOutputStream nullOutput = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(null, nullOutput);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        // test with serializable object
        SerializableObject serObj = new SerializableObject();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        SerializationUtils.serialize(serObj, output);
        byte[] serializedData = output.toByteArray();
        Assert.assertTrue(serializedData.length > 0);

    }

    // test cases for deserialize methods
    @Test
    public void testDeserialize() {
        // test with null input stream
        try {
            SerializationUtils.deserialize(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        // test with invalid serialized data
        byte[] invalidData = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream invalidInput = new ByteArrayInputStream(invalidData);
        try {
            SerializationUtils.deserialize(invalidInput);
        } catch (SerializationException e) {
            Assert.assertTrue(true);
        }

        // test with valid serialized data
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        SerializableObject serObj = new SerializableObject();
        SerializationUtils.serialize(serObj, output);
        byte[] serializedData = output.toByteArray();
        ByteArrayInputStream validInput = new ByteArrayInputStream(serializedData);
        Object deserializedObj = SerializationUtils.deserialize(validInput);
        Assert.assertEquals(serObj, deserializedObj);
    }

    // additional helper classes for testing
    private static class NotSerializableObject {
        private int value;
    }

    private static class SerializableObject implements Serializable {
        private int value;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SerializableObject that = (SerializableObject) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }
}
