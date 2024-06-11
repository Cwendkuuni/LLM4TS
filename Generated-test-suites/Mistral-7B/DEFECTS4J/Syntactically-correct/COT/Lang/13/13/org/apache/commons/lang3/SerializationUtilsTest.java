import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.serializable.Serializable;
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
        String input = "Hello World";
        String expected = "Hello World";
        Serializable object = input;
        Serializable clonedObject = SerializationUtils.clone(object);
        Assert.assertEquals(expected, clonedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(512));
    }

    @Test(expected = SerializationException.class)
    public void testSerializeIOException() {
        Serializable obj = new Object() {
            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException();
            }
        };
        OutputStream outputStream = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(obj, outputStream);
    }

    @Test
    public void testSerializeObject() {
        String input = "Hello World";
        byte[] serializedObject = SerializationUtils.serialize(input);

        String expected = "Hello World";
        String deserializedObject = SerializationUtils.deserialize(serializedObject);
        Assert.assertEquals(expected, deserializedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeClassNotFoundException() {
        ByteArrayOutputStream serializedStream = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(new Object() {
        }, serializedStream);

        ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(serializedStream.toByteArray()), null);
        in.readObject();
    }

    @Test
    public void testDeserializeObject() {
        String input = "Hello World";
        byte[] serializedObject = SerializationUtils.serialize(input);

        String expected = "Hello World";
        String deserializedObject = (String) SerializationUtils.deserialize(serializedObject);
        Assert.assertEquals(expected, deserializedObject);
    }

    @Test
    public void testCloneNullObject() {
        Assert.assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testCloneCollection() {
        Map<Integer, String> inputMap = new HashMap<>();
        inputMap.put(1, "One");
        inputMap.put(2, "Two");

        Map<Integer, String> expectedMap = new HashMap<>();
        expectedMap.put(1, "One");
        expectedMap.put(2, "Two");

        Map<Integer, String> clonedMap = SerializationUtils.clone(inputMap);
        Assert.assertEquals(expectedMap, clonedMap);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullClassLoader() {
        SerializationUtils.serialize((Serializable) null, new ByteArrayOutputStream(512), null);
    }
}