import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtilsTest {

    // Clone Tests
    @Test
    public void testClone_typicalCase() {
        // Arrange
        String input = "Hello, World!";
        
        // Act
        String result = SerializationUtils.clone(input);
        
        // Assert
        Assert.assertEquals(input, result);
    }
    
    @Test
    public void testClone_nullInput() {
        // Arrange
        String input = null;
        
        // Act
        String result = SerializationUtils.clone(input);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testClone_serializationError() {
        // Arrange
        Object input = new Object();
        
        // Act & Assert
        try {
            SerializationUtils.clone(input);
            Assert.fail("SerializationException not thrown");
        } catch (SerializationException e) {
            // Exception expected
        }
    }

    // Serialize Tests
    @Test
    public void testSerialize_typicalCase() {
        // Arrange
        String input = "Hello, World!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // Act
        SerializationUtils.serialize(input, outputStream);
        byte[] result = outputStream.toByteArray();
        
        // Assert
        ByteArrayInputStream inputStream = new ByteArrayInputStream(result);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            String deserialized = (String) objectInputStream.readObject();
            Assert.assertEquals(input, deserialized);
        } catch (Exception e) {
            Assert.fail("Deserialization error");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_nullObject() {
        // Arrange
        String input = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // Act & Assert
        SerializationUtils.serialize(input, outputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_nullOutputStream() {
        // Arrange
        String input = "Hello, World!";
        ByteArrayOutputStream outputStream = null;
        
        // Act & Assert
        SerializationUtils.serialize(input, outputStream);
    }
    
    // Deserialize Tests
    @Test
    public void testDeserialize_typicalCase() {
        // Arrange
        String input = "Hello, World!";
        byte[] serialized = SerializationUtils.serialize(input);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
        
        // Act
        Object result = SerializationUtils.deserialize(inputStream);
        
        // Assert
        Assert.assertEquals(input, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_nullInputStream() {
        // Arrange
        ByteArrayInputStream inputStream = null;
        
        // Act & Assert
        SerializationUtils.deserialize(inputStream);
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserialize_deserializationError() {
        // Arrange
        byte[] serialized = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
        
        // Act & Assert
        SerializationUtils.deserialize(inputStream);
    }
}