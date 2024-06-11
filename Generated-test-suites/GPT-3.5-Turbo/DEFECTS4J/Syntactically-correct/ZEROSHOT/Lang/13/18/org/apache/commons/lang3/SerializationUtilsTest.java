import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        SerializationUtilsTestObject obj = new SerializationUtilsTestObject("Test");
        SerializationUtilsTestObject clone = SerializationUtils.clone(obj);
        assertEquals(obj.getName(), clone.getName());
    }

    @Test
    public void testCloneNull() {
        SerializationUtilsTestObject obj = null;
        SerializationUtilsTestObject clone = SerializationUtils.clone(obj);
        assertNull(clone);
    }

    @Test
    public void testSerialize() {
        SerializationUtilsTestObject obj = new SerializationUtilsTestObject("Test");
        byte[] serialized = SerializationUtils.serialize(obj);
        assertNotNull(serialized);
    }

    @Test
    public void testDeserialize() {
        SerializationUtilsTestObject obj = new SerializationUtilsTestObject("Test");
        byte[] serialized = SerializationUtils.serialize(obj);
        SerializationUtilsTestObject deserialized = (SerializationUtilsTestObject) SerializationUtils.deserialize(serialized);
        assertEquals(obj.getName(), deserialized.getName());
    }

    @Test
    public void testDeserializeNull() {
        byte[] serialized = null;
        Object deserialized = SerializationUtils.deserialize(serialized);
        assertNull(deserialized);
    }

    private static class SerializationUtilsTestObject implements java.io.Serializable {
        private String name;

        public SerializationUtilsTestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}