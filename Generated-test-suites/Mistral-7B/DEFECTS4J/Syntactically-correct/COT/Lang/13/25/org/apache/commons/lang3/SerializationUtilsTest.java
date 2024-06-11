import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals("Cloned string should be equal to original", original, cloned);
    }

    @Test
    public void testSerializeObjectStream() {
        String original = "Hello World";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals("Deserialized string should be equal to original", original, deserialized);
    }

    @Test
    public void testSerializeOutputStream() {
        String original = "Hello World";
        OutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, outputStream);
        byte[] serialized = ((ByteArrayOutputStream) outputStream).toByteArray();
        String deserialized = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals("Deserialized string should be equal to original", original, deserialized);
    }

    @Test
    public void testDeserializeByteArray() {
        String original = "Hello World";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        Assert.assertEquals("Deserialized string should be equal to original", original, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test
    public void testDeserializeNullInputStream() {
        Assert.assertNull("Deserialized object should be null with null inputStream", SerializationUtils.deserialize(null));
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidData() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Invalid data".getBytes());
        Assert.assertNull("Deserialized object should be null with invalid data", SerializationUtils.deserialize(inputStream));
    }

    @Test
    public void testCloneNullObject() {
        Assert.assertNull("Cloned object should be null with null input", SerializationUtils.clone(null));
    }

    @Test(expected = SerializationException.class)
    public void testCloneNonSerializableObject() {
        Object nonSerializableObject = new Object() {
        };
        Assert.assertNull("Cloned object should be null with non-serializable object", SerializationUtils.clone(nonSerializableObject));
    }

    @Test
    public void testCloneMap() {
        Map<Integer, String> original = new HashMap<>();
        original.put(1, "One");
        original.put(2, "Two");

        Map<Integer, String> cloned = (Map<Integer, String>) SerializationUtils.clone(original);
        Assert.assertEquals("Cloned map should be equal to original", original, cloned);
    }

}