
import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone_NullInput_ReturnNull() {
        // Arrange
        Object object = null;
        
        // Act
        Object result = SerializationUtils.clone(object);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testClone_SerializableObject_ReturnClonedObject() {
        // Arrange
        SerializableObject obj = new SerializableObject();
        
        // Act
        SerializableObject result = SerializationUtils.clone(obj);
        
        // Assert
        Assert.assertNotSame(obj, result);
        Assert.assertEquals(obj. getValue(), result.getValue());
    }
    
    @Test
    public void testClone_NonSerializableObject_ThrowSerializationException() {
        // Arrange
        NonSerializableObject obj = new NonSerializableObject();
        
        // Act & Assert
        try {
            SerializationUtils.clone(obj);
            Assert.fail("Should throw SerializationException");
        } catch (SerializationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSerialize_NullObject_ThrowIllegalArgumentException() {
        // Arrange
        Serializable obj = null;
        OutputStream outputStream = new ByteArrayOutputStream();
        
        // Act & Assert
        try {
            SerializationUtils.serialize(obj, outputStream);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSerialize_SerializableObject_CorrectlySerializeObject() {
        // Arrange
        SerializableObject obj = new SerializableObject();
        OutputStream outputStream = new ByteArrayOutputStream();
        
        // Act
        SerializationUtils.serialize(obj, outputStream);
        byte[] serializedData = ((ByteArrayOutputStream) outputStream).toByteArray();
        
        // Assert
        Assert.assertNotNull(serializedData);
        Assert.assertNotEquals(serializedData.length, 0);
    }
    
    @Test
    public void testDeserialize_NullStream_ThrowIllegalArgumentException() {
        // Arrange
        InputStream inputStream = null;
        
        // Act & Assert
        try {
            SerializationUtils.deserialize(inputStream);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testDeserialize_ValidSerializedData_CorrectlyDeserializeObject() {
        // Arrange
        SerializableObject expected = new SerializableObject();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(expected);
        
        byte[] serializedData = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        
        // Act
        SerializableObject result = (SerializableObject) SerializationUtils.deserialize(bais);
        
        // Assert
        Assert.assertEquals(expected.getValue(), result.getValue());
    }
    
    @Test
    public void testDeserialize_InvalidSerializedData_ThrowSerializationException() {
        // Arrange
        byte[] corruptedData = new byte[10];
        ByteArrayInputStream bais = new ByteArrayInputStream(corruptedData);
        
        // Act & Assert
        try {
            SerializationUtils.deserialize(bais);
            Assert.fail("Should throw SerializationException");
        } catch (SerializationException e) {
            // Expected exception
        }
    }
    
    private static class SerializableObject implements Serializable {
        private String value = "default";

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    
    private static class NonSerializableObject {
        // Non-serializable object
    }
}

