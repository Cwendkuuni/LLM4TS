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
    public void testClone() throws SerializationException, IOException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap);

        // checking if the cloned map is independent of the original one
        originalMap.put("three", 3);
        Assert.assertNotSame(originalMap, clonedMap);
    }

    @Test
    public void testSerializeDeserializeObject() throws SerializationException, IOException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        byte[] serializedObject = SerializationUtils.serialize(originalMap);

        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));

        Assert.assertEquals(originalMap, deserializedMap);
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidData() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.deserialize(inputStream);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeCustomClassNotFound() throws IOException, ClassNotFoundException {
        // Assuming a custom class called MyCustomClass is not found in the given ClassLoader
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        SerializationUtils.deserialize(inputStream);
    }
}