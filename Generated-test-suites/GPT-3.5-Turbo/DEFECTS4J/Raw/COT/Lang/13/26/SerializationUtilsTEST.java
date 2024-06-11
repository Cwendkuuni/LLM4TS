
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationUtilsTest {

    @Test
    public void testClone_withSerializableObject_success() {
        // Given
        SerializableObject obj = new SerializableObject("test");

        // When
        SerializableObject clonedObj = SerializationUtils.clone(obj);

        // Then
        Assert.assertNotSame(obj, clonedObj);
        Assert.assertEquals(obj, clonedObj);
    }

    @Test
    public void testClone_withNullObject_success() {
        // Given
        SerializableObject obj = null;

        // When
        SerializableObject clonedObj = SerializationUtils.clone(obj);

        // Then
        Assert.assertNull(clonedObj);
    }

    @Test
    public void testSerialize_withSerializableObject_success() {
        // Given
        SerializableObject obj = new SerializableObject("test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // When
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();

        // Then
        Assert.assertNotNull(serializedData);
        Assert.assertTrue(serializedData.length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_withNullObject_exceptionThrown() {
        // Given
        SerializableObject obj = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // When
        SerializationUtils.serialize(obj, baos);

        // Then
        // Expecting IllegalArgumentException
    }

    @Test
    public void testDeserialize_withSerializedObject_success() {
        // Given
        SerializableObject obj = new SerializableObject("test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);

        // When
        Object deserializedObj = SerializationUtils.deserialize(bais);

        // Then
        Assert.assertNotNull(deserializedObj);
        Assert.assertTrue(deserializedObj instanceof SerializableObject);
        Assert.assertEquals(obj, deserializedObj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_withNullInputStream_exceptionThrown() {
        // Given
        ByteArrayInputStream bais = null;

        // When
        SerializationUtils.deserialize(bais);

        // Then
        // Expecting IllegalArgumentException
    }

    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;

        private String data;

        public SerializableObject(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            SerializableObject that = (SerializableObject) obj;

            return data != null ? data.equals(that.data) : that.data == null;
        }

        @Override
        public int hashCode() {
            return data != null ? data.hashCode() : 0;
        }
    }
}

