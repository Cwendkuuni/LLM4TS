import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

public class SerializationUtilsTest {

    // Clone
    //-----------------------------------------------------------------------

    @Test
    public void testClone_withNullObject_returnsNull() {
        // Arrange
        Object obj = null;

        // Act
        Object result = SerializationUtils.clone(obj);

        // Assert
        Assert.assertEquals(null, result);
    }

    @Test
    public void testClone_withSerializableObject_returnsClonedObject() {
        // Arrange
        TestObject obj = new TestObject();
        obj.setValue("test");

        // Act
        TestObject result = SerializationUtils.clone(obj);

        // Assert
        Assert.assertNotSame(obj, result);
        Assert.assertEquals(obj.getValue(), result.getValue());
    }

    @Test(expected = SerializationException.class)
    public void testClone_withNonSerializableObject_throwsSerializationException() {
        // Arrange
        NonSerializableObject obj = new NonSerializableObject();

        // Act
        SerializationUtils.clone(obj);

        // Assert
        // SerializationException should be thrown
    }

    // Serialize
    //-----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_withNullObject_throwsIllegalArgumentException() {
        // Arrange
        Object obj = null;
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        SerializationUtils.serialize(obj, outputStream);

        // Assert
        // IllegalArgumentException should be thrown
    }

    @Test
    public void testSerialize_withSerializableObject_returnsSerializedByteArray() throws IOException {
        // Arrange
        TestObject obj = new TestObject();
        obj.setValue("test");
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        SerializationUtils.serialize(obj, outputStream);
        byte[] serializedBytes = ((ByteArrayOutputStream) outputStream).toByteArray();

        // Assert
        Assert.assertNotEquals(0, serializedBytes.length);
    }

    // Deserialize
    //-----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_withNullInputStream_throwsIllegalArgumentException() {
        // Arrange
        InputStream inputStream = null;

        // Act
        SerializationUtils.deserialize(inputStream);

        // Assert
        // IllegalArgumentException should be thrown
    }

    @Test
    public void testDeserialize_withSerializedBytes_returnsDeserializedObject() {
        // Arrange
        TestObject obj = new TestObject();
        obj.setValue("test");
        byte[] serializedBytes = SerializationUtils.serialize(obj);
        InputStream inputStream = new ByteArrayInputStream(serializedBytes);

        // Act
        Object result = SerializationUtils.deserialize(inputStream);

        // Assert
        Assert.assertEquals(obj, result);
    }

    // Helper classes
    //-----------------------------------------------------------------------

    static class TestObject implements Serializable {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    static class NonSerializableObject {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}