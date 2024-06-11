import org.junit.Test;
import org.apache.commons.lang3.SerializationUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test case 1: Cloning a non-null object
        MyObject obj = new MyObject("test", 123);
        MyObject clonedObj = SerializationUtils.clone(obj);
        assertEquals(obj, clonedObj);

        // Test case 2: Cloning a null object
        MyObject nullObj = null;
        MyObject clonedNullObj = SerializationUtils.clone(nullObj);
        assertNull(clonedNullObj);
    }

    @Test
    public void testSerialize() throws IOException {
        // Test case 1: Serializing a non-null object
        MyObject obj = new MyObject("test", 123);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        ObjectInputStream ois = new ObjectInputStream(bais);
        MyObject deserializedObj = (MyObject) ois.readObject();
        assertEquals(obj, deserializedObj);
        
        // Test case 2: Serializing a null object
        MyObject nullObj = null;
        ByteArrayOutputStream nullBaos = new ByteArrayOutputStream();
        SerializationUtils.serialize(nullObj, nullBaos);
        byte[] nullSerializedData = nullBaos.toByteArray();
        assertEquals(0, nullSerializedData.length);
    }

    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        // Test case 1: Deserializing a non-null object
        MyObject obj = new MyObject("test", 123);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        byte[] serializedData = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        Object deserializedObj = SerializationUtils.deserialize(bais);
        assertTrue(deserializedObj instanceof MyObject);
        assertEquals(obj, deserializedObj);
        
        // Test case 2: Deserializing a null object
        byte[] nullSerializedData = new byte[0];
        ByteArrayInputStream nullBais = new ByteArrayInputStream(nullSerializedData);
        Object nullDeserializedObj = SerializationUtils.deserialize(nullBais);
        assertNull(nullDeserializedObj);
    }

    // Helper class for testing
    private static class MyObject implements Serializable {
        private String name;
        private int value;

        public MyObject(String name, int value) {
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
        
            MyObject other = (MyObject) obj;
            return name.equals(other.name) && value == other.value;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }
    }
}