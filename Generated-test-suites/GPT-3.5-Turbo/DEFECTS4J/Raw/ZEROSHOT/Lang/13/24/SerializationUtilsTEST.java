import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {
    
    @Test
    public void testClone() {
        // Test cloning of null object
        assertSerializationEquals(null, SerializationUtils.clone(null));
        
        // Test cloning of Serializable object
        MySerializableObject obj = new MySerializableObject();
        assertSerializationEquals(obj, SerializationUtils.clone(obj));
    }
    
    @Test(expected = SerializationException.class)
    public void testCloneSerializationException() {
        // Test cloning of non-serializable object
        NonSerializableObject obj = new NonSerializableObject();
        SerializationUtils.clone(obj);
    }
    
    @Test
    public void testSerialize() throws IOException {
        // Test serialization of null object
        assertByteArrayEquals(null, SerializationUtils.serialize(null));
        
        // Test serialization of Serializable object
        MySerializableObject obj = new MySerializableObject();
        assertByteArrayEquals(obj, SerializationUtils.serialize(obj));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeIllegalArgumentException() {
        // Test serialization with null output stream
        SerializationUtils.serialize(new MySerializableObject(), null);
    }
    
    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        // Test deserialization of null object
        assertSerializationEquals(null, SerializationUtils.deserialize(null));
        
        // Test deserialization of Serializable object
        MySerializableObject obj = new MySerializableObject();
        ByteArrayInputStream bais = new ByteArrayInputStream(SerializationUtils.serialize(obj));
        assertSerializationEquals(obj, SerializationUtils.deserialize(bais));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeIllegalArgumentException() {
        // Test deserialization with null input stream
        SerializationUtils.deserialize((InputStream) null);
    }
    
    @Test
    public void testDeserializeByteArray() throws IOException, ClassNotFoundException {
        // Test deserialization of null object
        assertSerializationEquals(null, SerializationUtils.deserialize((byte[]) null));
        
        // Test deserialization of Serializable object
        MySerializableObject obj = new MySerializableObject();
        byte[] serializedObj = SerializationUtils.serialize(obj);
        assertSerializationEquals(obj, SerializationUtils.deserialize(serializedObj));
    }
    
    private void assertSerializationEquals(Object expected, Object actual) {
        if (expected == null) {
            assert (actual == null);
        } else {
            assert (expected.equals(actual));
        }
    }
    
    private void assertByteArrayEquals(Object expected, byte[] actual) throws IOException {
        byte[] expectedByteArray = (expected == null) ? null : SerializationUtils.serialize((Serializable) expected);
        
        if (expectedByteArray == null) {
            assert (actual == null);
        } else {
            assert (expectedByteArray.length == actual.length);
            for (int i = 0; i < expectedByteArray.length; i++) {
                assert (expectedByteArray[i] == actual[i]);
            }
        }
    }
    
    private static class MySerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private int value;
        
        public MySerializableObject() {
            value = 123;
        }
        
        public int getValue() {
            return value;
        }
        
        public void setValue(int value) {
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
            MySerializableObject other = (MySerializableObject) obj;
            return value == other.value;
        }
        
        @Override
        public int hashCode() {
            return Integer.hashCode(value);
        }
    }
    
    private static class NonSerializableObject {
        private int value;
        
        public NonSerializableObject() {
            value = 456;
        }
        
        public int getValue() {
            return value;
        }
        
        public void setValue(int value) {
            this.value = value;
        }
    }
}