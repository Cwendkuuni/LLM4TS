import org.junit.Test;
import org.apache.commons.lang3.SerializationUtils;
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
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone_withNullObject() {
        Object obj = null;
        Object clonedObj = SerializationUtils.clone(obj);
        
        assertNull(clonedObj);
    }
    
    @Test
    public void testClone_withSerializableObject() {
        SerializableObject obj = new SerializableObject("test", 10);
        SerializableObject clonedObj = SerializationUtils.clone(obj);
        
        assertEquals(obj.getName(), clonedObj.getName());
        assertEquals(obj.getValue(), clonedObj.getValue());
        assertNotSame(obj, clonedObj);
    }
    
    @Test
    public void testSerialize_withNullObjectOutputStream() {
        Serializable obj = null;
        OutputStream outputStream = null;
        
        try {
            SerializationUtils.serialize(obj, outputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The OutputStream must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testSerialize_withObjectOutputStream() {
        SerializableObject obj = new SerializableObject("test", 10);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            SerializationUtils.serialize(obj, outputStream);
            byte[] serializedData = outputStream.toByteArray();
            
            assertNotNull(serializedData);
            assertTrue(serializedData.length > 0);
        } catch (SerializationException e) {
            fail("Unexpected SerializationException");
        }
    }
    
    @Test
    public void testSerialize_withSerializableObject() {
        SerializableObject obj = new SerializableObject("test", 10);
        byte[] serializedData = SerializationUtils.serialize(obj);
        
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }
    
    @Test
    public void testDeserialize_withNullInputStream() {
        InputStream inputStream = null;
        
        try {
            Object deserializedObj = SerializationUtils.deserialize(inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The InputStream must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_withInvalidSerializedData() {
        byte[] serializedData = new byte[]{1, 2, 3, 4};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        
        try {
            Object deserializedObj = SerializationUtils.deserialize(inputStream);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            assertNotNull(e.getCause());
        }
    }
    
    @Test
    public void testDeserialize_withValidSerializedData() {
        SerializableObject obj = new SerializableObject("test", 10);
        byte[] serializedData = SerializationUtils.serialize(obj);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        
        try {
            Object deserializedObj = SerializationUtils.deserialize(inputStream);
            
            assertNotNull(deserializedObj);
            assertTrue(deserializedObj instanceof SerializableObject);
            assertEquals(obj.getName(), ((SerializableObject) deserializedObj).getName());
            assertEquals(obj.getValue(), ((SerializableObject) deserializedObj).getValue());
        } catch (SerializationException e) {
            fail("Unexpected SerializationException");
        }
    }
    
    private static class SerializableObject implements Serializable {
        private String name;
        private int value;
        
        public SerializableObject(String name, int value) {
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