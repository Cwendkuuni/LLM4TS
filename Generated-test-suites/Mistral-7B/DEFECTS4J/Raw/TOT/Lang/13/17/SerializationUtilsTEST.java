import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import org.junit.Assert;
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
    public void testClone() throws Serializable, IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);

        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);

        // Test with null input
        try {
            SerializationUtils.clone(null);
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }

        // Test with non-serializable input
        try {
            SerializationUtils.clone(new NonSerializableObject());
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testSerialize() throws Serializable, IOException {
        Object original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        Assert.assertNotSame(original, deserialized);
        Assert.assertEquals(original, deserialized);

        // Test with null input
        try {
            SerializationUtils.serialize(null, new ByteArrayOutputStream());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with non-serializable input
        try {
            SerializationUtils.serialize(new NonSerializableObject(), new ByteArrayOutputStream());
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testDeserialize() throws Serializable, IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        Assert.assertNotSame(original, deserialized);
        Assert.assertEquals(original, deserialized);

        // Test with null input
        try {
            SerializationUtils.deserialize(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid byte[]
        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test with non-serializable input
        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(SerializationUtils.serialize(new NonSerializableObject())));
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    private static class NonSerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}