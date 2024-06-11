import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Create a test object
        TestObject originalObject = new TestObject();
        originalObject.setValue("test");

        // Call the clone method
        TestObject clonedObject = SerializationUtils.clone(originalObject);

        // Assert that the cloned object is equal to the original object
        Assert.assertEquals(originalObject, clonedObject);
    }

    @Test
    public void testSerialize() {
        // Create a test object
        TestObject testObject = new TestObject();
        testObject.setValue("test");

        // Serialize the object
        byte[] serializedData = SerializationUtils.serialize(testObject);

        // Assert that the serialized data is not empty
        Assert.assertNotNull(serializedData);
        Assert.assertTrue(serializedData.length > 0);
    }

    @Test
    public void testDeserialize() {
        // Create a test object
        TestObject testObject = new TestObject();
        testObject.setValue("test");

        // Serialize the object
        byte[] serializedData = SerializationUtils.serialize(testObject);

        // Deserialize the serialized data
        TestObject deserializedObject = SerializationUtils.deserialize(serializedData);

        // Assert that the deserialized object is equal to the original object
        Assert.assertEquals(testObject, deserializedObject);
    }

    private static class TestObject implements Serializable {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
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
            return value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }
}