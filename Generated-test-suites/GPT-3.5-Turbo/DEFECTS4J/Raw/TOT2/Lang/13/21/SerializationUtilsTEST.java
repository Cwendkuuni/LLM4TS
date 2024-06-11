import org.junit.Test;
import org.junit.Assert;

public class SerializationUtilsTest {
    
    @Test
    public void testClone() {
        // Typical use case
        String input = "Hello, world!";
        String cloned = SerializationUtils.clone(input);
        Assert.assertEquals(input, cloned);
        
        // Edge case
        String nullInput = null;
        String clonedNull = SerializationUtils.clone(nullInput);
        Assert.assertNull(clonedNull);
        
        // Error scenario
        try {
            SerializationUtils.clone(new Object()); // Trying to clone a non-serializable object
            Assert.fail("SerializationException expected");
        } catch (SerializationException e) {
            // Exception expected
        }
    }

    @Test
    public void testSerialize() {
        // Typical use case
        String input = "SerializationUtils";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(input, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        String serialized = new String(byteArray);
        Assert.assertEquals(input, serialized);

        // Edge case
        try {
            SerializationUtils.serialize(null, outputStream);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        String input = "Deserialization";
        byte[] serializedData = SerializationUtils.serialize(input);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        String deserialized = (String) SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(input, deserialized);

        // Edge case
        try {
            SerializationUtils.deserialize((InputStream) null);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Error scenario
        try {
            SerializationUtils.deserialize(new byte[0]); // Deserializing an empty byte array
            Assert.fail("SerializationException expected");
        } catch (SerializationException e) {
            // Exception expected
        }
    }
}