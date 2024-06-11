
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

public class SerializationUtilsTest {

    // Test for SerializationUtils clone() method
    
    // Test for cloning a null object
    @Test
    public void testClone_NullObject() {
        assertNull(SerializationUtils.clone(null));
    }
    
    // Test for cloning an object
    @Test
    public void testClone_Object() {
        TestClass obj = new TestClass();
        TestClass clonedObj = SerializationUtils.clone(obj);
        assertEquals(obj.getNumber(), clonedObj.getNumber());
        assertEquals(obj.getString(), clonedObj.getString());
    }
    
    // Test for cloning a non-serializable object
    @Test(expected = SerializationException.class)
    public void testClone_NonSerializableObject() {
        NonSerializableClass obj = new NonSerializableClass();
        SerializationUtils.clone(obj);
    }
    
    // Test for SerializationUtils.serialize() methods
    
    // Test for serializing a null object
    @Test
    public void testSerialize_NullObject() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, baos);
        byte[] serializedData = baos.toByteArray();
        assertEquals(0, serializedData.length);
    }
    
    // Test for serializing an object
    @Test
    public void testSerialize_Object() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TestClass obj = new TestClass();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();
        assertTrue(serializedData.length > 0);
    }
    
    // Test for SerializationUtils.serialize() method with null OutputStream
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_NullOutputStream() {
        SerializationUtils.serialize(new TestClass(), null);
    }

    // Test for SerializationUtils.deserialize() methods
    
    // Test for deserializing from a null input stream
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_NullInputStream() {
        SerializationUtils.deserialize(null);
    }
    
    // Test for deserializing a null object
    @Test
    public void testDeserialize_NullObject() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        assertNull(SerializationUtils.deserialize(is));
    }
    
    // Test for deserializing an object
    @Test
    public void testDeserialize_Object() {
        TestClass obj1 = new TestClass();
        byte[] serializedData = SerializationUtils.serialize(obj1);
        InputStream is = new ByteArrayInputStream(serializedData);
        TestClass obj2 = (TestClass) SerializationUtils.deserialize(is);
        assertEquals(obj1.getNumber(), obj2.getNumber());
        assertEquals(obj1.getString(), obj2.getString());
    }
    
    // Test for deserializing an invalid object (unsupported class)
    @Test(expected = SerializationException.class)
    public void testDeserialize_InvalidObject() {
        byte[] serializedData = new byte[] {1, 2, 3, 4, 5};
        InputStream is = new ByteArrayInputStream(serializedData);
        SerializationUtils.deserialize(is);
    }
    
}

// Sample TestClass for testing purposes
class TestClass implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int number;
    private String string;
    
    public TestClass() {
        number = 10;
        string = "Test";
    }
    
    public int getNumber() {
        return number;
    }
    
    public String getString() {
        return string;
    }
}

// Sample NonSerializableClass for testing purposes
class NonSerializableClass {
    private int number;
    
    public NonSerializableClass() {
        number = 10;
    }
    
    public int getNumber() {
        return number;
    }
}

