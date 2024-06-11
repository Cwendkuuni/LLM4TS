import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    // Test data
    private static final String TEST_STRING = "test string";
    private static final int TEST_INT = 10;

    // Test object
    private static class TestObject implements Serializable {
        private String stringData;
        private int intData;

        public TestObject(String stringData, int intData) {
            this.stringData = stringData;
            this.intData = intData;
        }

        public String getStringData() {
            return stringData;
        }

        public void setStringData(String stringData) {
            this.stringData = stringData;
        }

        public int getIntData() {
            return intData;
        }

        public void setIntData(int intData) {
            this.intData = intData;
        }
    }

    // Test clone method
    @Test
    public void testClone() {
        TestObject obj = new TestObject(TEST_STRING, TEST_INT);

        TestObject clone = SerializationUtils.clone(obj);

        assertEquals(obj.getStringData(), clone.getStringData());
        assertEquals(obj.getIntData(), clone.getIntData());
        assertNotSame(obj, clone);
    }

    // Test serialize method
    @Test
    public void testSerialize() {
        TestObject obj = new TestObject(TEST_STRING, TEST_INT);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();

        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }

    // Test deserialize method
    @Test
    public void testDeserialize() {
        TestObject obj = new TestObject(TEST_STRING, TEST_INT);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();

        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);

        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        TestObject deserializedObj = (TestObject) SerializationUtils.deserialize(bais);

        assertNotNull(deserializedObj);
        assertEquals(obj.getStringData(), deserializedObj.getStringData());
        assertEquals(obj.getIntData(), deserializedObj.getIntData());
    }

    // Test invalid inputs
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullOutputStream() {
        TestObject obj = new TestObject(TEST_STRING, TEST_INT);

        SerializationUtils.serialize(obj, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    // Test serialization failure
    @Test(expected = SerializationException.class)
    public void testSerializeWithInvalidObject() {
        TestObject obj = new TestObject(TEST_STRING, TEST_INT);
        obj.setStringData(null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeWithInvalidData() {
        byte[] invalidData = {1, 2, 3, 4, 5};

        ByteArrayInputStream bais = new ByteArrayInputStream(invalidData);
        SerializationUtils.deserialize(bais);
    }

}