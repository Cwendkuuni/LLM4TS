import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws SerializationException {
        // Given: An instance of a Serializable class
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        // When: Cloning the map using SerializationUtils.clone()
        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        // Then: The cloned map is equal to the original map
        Assert.assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testCloneNullObject() {
        // Given: A null object
        SerializationException exception = null;

        // When: Trying to clone a null object using SerializationUtils.clone()
        try {
            SerializationUtils.clone(null);
        } catch (SerializationException e) {
            exception = e;
        }

        // Then: A SerializationException is thrown
        Assert.assertNotNull(exception);
    }

    @Test
    public void testSerialize() throws SerializationException, IOException {
        // Given: An instance of a Serializable class
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        // When: Serializing the map to a byte array using SerializationUtils.serialize()
        byte[] serializedMap = SerializationUtils.serialize(originalMap);

        // Then: The serialized byte array is not empty
        Assert.assertNotEquals(0, serializedMap.length);

        // And: The deserialized object is equal to the original object
        Map<String, Integer> deserializedMap = SerializationUtils.deserialize(serializedMap);
        Assert.assertEquals(originalMap, deserializedMap);
    }

    @Test
    public void testSerializeNullObject() {
        // Given: A null object
        SerializationException exception = null;

        // When: Trying to serialize a null object using SerializationUtils.serialize()
        try {
            SerializationUtils.serialize(null);
        } catch (SerializationException e) {
            exception = e;
        }

        // Then: A SerializationException is thrown
        Assert.assertNotNull(exception);
    }

    @Test
    public void testDeserialize() throws SerializationException, IOException {
        // Given: A serialized byte array
        byte[] serializedMap = SerializationUtils.serialize(new HashMap<>());

        // When: Deserializing the byte array to an object using SerializationUtils.deserialize()
        Object deserializedObject = SerializationUtils.deserialize(serializedMap);

        // Then: The deserialized object is an instance of HashMap
        Assert.assertTrue(deserializedObject instanceof Map);
        @SuppressWarnings("unchecked") // we know it's a HashMap
        Map<String, Object> deserializedMap = (Map<String, Object>) deserializedObject;

        // And: The deserialized map is empty
        Assert.assertEquals(0, deserializedMap.size());
    }

    @Test
    public void testDeserializeNullInputStream() {
        // Given: A null InputStream
        InputStream inputStream = null;

        // When: Trying to deserialize an object using SerializationUtils.deserialize()
        SerializationException exception = null;

        try {
            SerializationUtils.deserialize(inputStream);
        } catch (SerializationException e) {
            exception = e;
        }

        // Then: A SerializationException is thrown
        Assert.assertNotNull(exception);
    }

}