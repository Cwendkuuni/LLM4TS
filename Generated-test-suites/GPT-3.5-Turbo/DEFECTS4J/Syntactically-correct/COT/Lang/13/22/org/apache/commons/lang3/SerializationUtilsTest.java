import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test case for cloning an object using serialization
        // Create a test object
        TestObject obj = new TestObject("testName", 123);
        
        // Clone the test object
        TestObject clonedObj = SerializationUtils.clone(obj);
        
        // Check if the cloned object is equal to the original object
        assertEquals(obj.getName(), clonedObj.getName());
        assertEquals(obj.getValue(), clonedObj.getValue());
    }
    
    @Test
    public void testSerialize() {
        // Test case for serializing an object
        // Create a test object
        TestObject obj = new TestObject("testName", 123);
        
        // Serialize the test object
        byte[] serializedObj = SerializationUtils.serialize(obj);
        
        // Check if the serialized object is not null
        assertNotNull(serializedObj);
    }
    
    @Test
    public void testDeserialize() {
        // Test case for deserializing an object
        // Create a test object
        TestObject obj = new TestObject("testName", 123);
        
        // Serialize and then deserialize the test object
        byte[] serializedObj = SerializationUtils.serialize(obj);
        TestObject deserializedObj = (TestObject) SerializationUtils.deserialize(serializedObj);
        
        // Check if the deserialized object is equal to the original object
        assertEquals(obj.getName(), deserializedObj.getName());
        assertEquals(obj.getValue(), deserializedObj.getValue());
    }
    
    // Test cases for potential edge cases and exception handling scenarios
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        // Test case for serializing an object with a null output stream
        SerializationUtils.serialize(new TestObject("testName", 123), null);
    }
    
    @Test(expected = SerializationException.class)
    public void testSerializeIOException() {
        // Test case for serializing an object with an output stream that throws an IOException
        SerializationUtils.serialize(new TestObject("testName", 123), new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        // Test case for deserializing an object with a null input stream
        SerializationUtils.deserialize(null);
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserializeIOException() {
        // Test case for deserializing an object with an input stream that throws an IOException
        SerializationUtils.deserialize(new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        });
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserializeClassNotFoundException() {
        // Test case for deserializing an object with a class that cannot be found
        byte[] serializedObj = new byte[] { 1, 2, 3 };
        SerializationUtils.deserialize(serializedObj);
    }
}