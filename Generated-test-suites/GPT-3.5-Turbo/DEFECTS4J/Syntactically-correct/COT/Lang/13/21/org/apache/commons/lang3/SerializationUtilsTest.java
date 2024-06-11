
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test deep cloning of an object
        MyClass obj = new MyClass("test", 10);
        MyClass clonedObj = SerializationUtils.clone(obj);
        assertNotSame(obj, clonedObj);
        assertEquals(obj.getName(), clonedObj.getName());
        assertEquals(obj.getValue(), clonedObj.getValue());
    }

    @Test
    public void testSerialize() {
        // Test serialization of an object to output stream
        MyClass obj = new MyClass("test", 10);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedObj = baos.toByteArray();
        assertNotNull(serializedObj);
    }

    @Test
    public void testDeserialize() {
        // Test deserialization of an object from input stream
        MyClass obj = new MyClass("test", 10);
        byte[] serializedObj = SerializationUtils.serialize(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        MyClass deserializedObj = (MyClass) SerializationUtils.deserialize(bais);
        assertNotNull(deserializedObj);
        assertEquals(obj.getName(), deserializedObj.getName());
        assertEquals(obj.getValue(), deserializedObj.getValue());
    }

    class MyClass implements Serializable {
        private String name;
        private int value;

        public MyClass(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}

