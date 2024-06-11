
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    // Clone

    @Test
    public void testClone() {
        TestObject obj = new TestObject("Test");
        TestObject clonedObj = SerializationUtils.clone(obj);
        assertNotSame(obj, clonedObj);
        assertEquals(obj.getName(), clonedObj.getName());
    }

    @Test
    public void testCloneWithNullObject() {
        assertNull(SerializationUtils.clone(null));
    }

    // Serialize

    @Test
    public void testSerialize() {
        TestObject obj = new TestObject("Test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullObject() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, baos);
    }

    // Deserialize

    @Test
    public void testDeserialize() {
        TestObject obj = new TestObject("Test");
        byte[] serializedData = SerializationUtils.serialize(obj);
        TestObject deserializedObj = (TestObject) SerializationUtils.deserialize(serializedData);
        assertNotNull(deserializedObj);
        assertNotSame(obj, deserializedObj);
        assertEquals(obj.getName(), deserializedObj.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    // TestObject

    private static class TestObject implements Serializable {
        private String name;

        public TestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
