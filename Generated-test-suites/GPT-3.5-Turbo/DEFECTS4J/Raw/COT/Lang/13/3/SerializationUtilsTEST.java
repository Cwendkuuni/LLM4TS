import org.junit.Test;
import org.junit.Assert;
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
        MyClass obj1 = new MyClass("Hello");
        MyClass obj2 = SerializationUtils.clone(obj1);
        Assert.assertEquals(obj1, obj2);
    }

    @Test
    public void testSerializeAndDeserialize() {
        MyClass obj1 = new MyClass("Hello");
        byte[] serialized = SerializationUtils.serialize(obj1);
        MyClass obj2 = (MyClass) SerializationUtils.deserialize(serialized);
        Assert.assertEquals(obj1, obj2);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testCloneWithNullObject() {
        MyClass obj1 = null;
        MyClass obj2 = SerializationUtils.clone(obj1);
        Assert.assertEquals(obj1, obj2);
    }
    
    @Test(expected = SerializationException.class)
    public void testCloneWithInvalidSerializableObject() {
        InvalidSerializableClass obj = new InvalidSerializableClass();
        SerializationUtils.clone(obj);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullOutputStream() {
        MyClass obj = new MyClass("Hello");
        SerializationUtils.serialize(obj, null);
    }
    
    @Test(expected = SerializationException.class)
    public void testSerializeWithIOException() {
        MyClass obj = new MyClass("Hello");
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        SerializationUtils.serialize(obj, outputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullInputStream() {
        SerializationUtils.deserialize(null);
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserializeWithClassNotFoundException() {
        byte[] serialized = new byte[] { 1, 2, 3 };
        SerializationUtils.deserialize(serialized);
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserializeWithIOException() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        SerializationUtils.deserialize(inputStream);
    }
    
    public static class MyClass implements Serializable {
        private String message;

        public MyClass(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            MyClass myClass = (MyClass) obj;
            return message.equals(myClass.message);
        }

        @Override
        public int hashCode() {
            return message.hashCode();
        }
    }
    
    public static class InvalidSerializableClass {
        // This class does not implement Serializable
    }

}