
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

public class SerializationUtilsTest {

    // clone() method test case
    @Test
    public void testClone() {
        // Create a test object
        TestObject obj = new TestObject("test");
        TestObject clonedObj = SerializationUtils.clone(obj);

        // Check if the cloned object is not the same instance as the original object
        assertNotSame(obj, clonedObj);

        // Check if the cloned object has the same values as the original object
        assertEquals(obj.getValue(), clonedObj.getValue());
    }

    // serialize() method test case
    @Test
    public void testSerialize() {
        // Create a test object
        TestObject obj = new TestObject("test");
        byte[] serializedObj = SerializationUtils.serialize(obj);

        // Check if the serialized object is not null
        assertNotNull(serializedObj);

        // Check if the serialized object can be deserialized without errors
        try {
            Object deserializedObj = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObj));
            assertTrue(deserializedObj instanceof TestObject);
            assertEquals(obj.getValue(), ((TestObject) deserializedObj).getValue());
        } catch (SerializationException e) {
            fail("SerializationException should not be thrown");
        }
    }

    // deserialize() method test case
    @Test
    public void testDeserialize() {
        // Create a test object
        TestObject obj = new TestObject("test");
        byte[] serializedObj = SerializationUtils.serialize(obj);

        // Check if the deserialized object is not null
        try {
            Object deserializedObj = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObj));
            assertNotNull(deserializedObj);
            assertTrue(deserializedObj instanceof TestObject);
            assertEquals(obj.getValue(), ((TestObject) deserializedObj).getValue());
        } catch (SerializationException e) {
            fail("SerializationException should not be thrown");
        }
    }

    // inner class used for testing
    private static class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String value;

        public TestObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}