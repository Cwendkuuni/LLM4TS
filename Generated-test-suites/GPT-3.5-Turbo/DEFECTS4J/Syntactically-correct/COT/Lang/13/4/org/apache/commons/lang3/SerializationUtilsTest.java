import org.junit.Assert;
import org.junit.Test;
import java.io.*;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        // Arrange
        SerializableObject obj = new SerializableObject();
        obj.setProperty("Test Property");

        // Act
        SerializableObject clonedObj = SerializationUtils.clone(obj);

        // Assert
        Assert.assertNotSame(obj, clonedObj);
        Assert.assertEquals(obj.getProperty(), clonedObj.getProperty());
    }

    @Test
    public void testSerialize() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializableObject obj = new SerializableObject();
        obj.setProperty("Test Property");

        // Act
        SerializationUtils.serialize(obj, baos);
        byte[] serializedBytes = baos.toByteArray();

        // Assert
        Assert.assertNotNull(serializedBytes);
        Assert.assertTrue(serializedBytes.length > 0);
    }

    @Test
    public void testDeserialize() throws IOException {
        // Arrange
        byte[] serializedBytes = getSerializedObjectBytes();

        // Act
        Object deserializedObj = SerializationUtils.deserialize(new ByteArrayInputStream(serializedBytes));

        // Assert
        Assert.assertNotNull(deserializedObj);
        Assert.assertTrue(deserializedObj instanceof SerializableObject);
        Assert.assertEquals("Test Property", ((SerializableObject) deserializedObj).getProperty());
    }

    @Test
    public void testDeserialize_InvalidData() {
        // Arrange
        byte[] serializedBytes = new byte[] {1, 2, 3, 4}; // Invalid serialized object data

        // Act & Assert
        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(serializedBytes));
            Assert.fail("Expected SerializationException not thrown");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @SuppressWarnings("unchecked")
    private byte[] getSerializedObjectBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializableObject obj = new SerializableObject();
        obj.setProperty("Test Property");
        SerializationUtils.serialize(obj, baos);
        return baos.toByteArray();
    }

    static class SerializableObject implements Serializable {
        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

}