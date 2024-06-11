import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        String original = "Hello";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
        
        // Edge case
        String nullString = null;
        assertNull(SerializationUtils.clone(nullString));
        
        // Error scenario
        assertThrows(SerializationException.class, () -> {
            SerializationUtils.clone(new Object());
        });
    }

    @Test
    public void testSerializeOutputStream() {
        // Typical use case
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String input = "Serialize me!";
        SerializationUtils.serialize(input, outputStream);
        assertNotNull(outputStream.toByteArray());
        
        // Edge case
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.serialize(input, null);
        });
    }

    @Test
    public void testSerializeSerializable() {
        // Typical use case
        String input = "Serialize me!";
        byte[] result = SerializationUtils.serialize(input);
        assertNotNull(result);
        
        // Edge case
        assertNull(SerializationUtils.serialize(null));
    }

    @Test
    public void testDeserializeInputStream() {
        // Typical use case
        String expected = "Deserialize me!";
        byte[] input = SerializationUtils.serialize(expected);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        String result = (String) SerializationUtils.deserialize(inputStream);
        assertEquals(expected, result);
        
        // Edge case
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.deserialize(null);
        });
    }

    @Test
    public void testDeserializeByteArray() {
        // Typical use case
        String expected = "Deserialize me!";
        byte[] input = SerializationUtils.serialize(expected);
        String result = (String) SerializationUtils.deserialize(input);
        assertEquals(expected, result);

        // Edge case
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.deserialize((byte[]) null);
        });
    }

}