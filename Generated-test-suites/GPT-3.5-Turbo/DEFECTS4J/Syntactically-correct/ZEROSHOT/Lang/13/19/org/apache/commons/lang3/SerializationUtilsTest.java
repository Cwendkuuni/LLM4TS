import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test cloning a Serializable object
        TestObject obj = new TestObject("Test", 10);
        TestObject clonedObj = SerializationUtils.clone(obj);
        assertNotSame("Cloned object should not be same as original object", obj, clonedObj);
        assertEquals("Cloned object should be equal to original object", obj, clonedObj);
    }

    @Test(expected = SerializationException.class)
    public void testCloneNull() {
        // Test cloning a null object
        TestObject obj = null;
        SerializationUtils.clone(obj);
    }

    @Test
    public void testSerialize() {
        // Test serializing an object
        TestObject obj = new TestObject("Test", 10);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();
        assertNotNull("Serialized data should not be null", serializedData);
        assertTrue("Serialized data should not be empty", serializedData.length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        // Test serializing a null object
        TestObject obj = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullStream() {
        // Test serializing to a null stream
        TestObject obj = new TestObject("Test", 10);
        OutputStream outputStream = null;
        SerializationUtils.serialize(obj, outputStream);
    }

    @Test
    public void testDeserialize() {
        // Test deserializing an object
        TestObject obj = new TestObject("Test", 10);
        byte[] serializedData = SerializationUtils.serialize(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        Object deserializedObj = SerializationUtils.deserialize(bais);
        assertNotNull("Deserialized object should not be null", deserializedObj);
        assertEquals("Deserialized object should be equal to original object", obj, deserializedObj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullStream() {
        // Test deserializing from a null stream
        InputStream inputStream = null;
        SerializationUtils.deserialize(inputStream);
    }

    @Test
    public void testDeserializeNullData() {
        // Test deserializing null data
        byte[] serializedData = null;
        Object deserializedObj = SerializationUtils.deserialize(serializedData);
        assertNull("Deserialized object should be null", deserializedObj);
    }

    public class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private int value;

        public TestObject(String name, int value) {
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
            TestObject other = (TestObject) obj;
            return name.equals(other.name) && value == other.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }
    }
}