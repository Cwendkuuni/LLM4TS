import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.Cloneable;
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
    public void testClone() throws SerializationException, CloneNotSupportedException {
        // Test cloning an empty HashMap
        HashMap<String, Integer> original = new HashMap<>();
        HashMap<String, Integer> cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);

        // Test cloning a HashMap with elements
        HashMap<String, Integer> mapToClone = new HashMap<>();
        mapToClone.put("one", 1);
        mapToClone.put("two", 2);

        HashMap<String, Integer> clonedMap = SerializationUtils.clone(mapToClone);
        Assert.assertNotSame(mapToClone, clonedMap);
        Assert.assertEquals(mapToClone, clonedMap);

        // Test cloning a null HashMap
        HashMap<String, Integer> nullMap = null;
        try {
            SerializationUtils.clone(nullMap);
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }

        // Test cloning a non-Serializable object
        Object nonSerializableObject = new Object() {
            @Override
            public Object clone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException();
            }
        };
        try {
            SerializationUtils.clone(nonSerializableObject);
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testSerializeAndDeserialize() throws SerializationException, IOException {
        // Test serialization and deserialization of an empty HashMap
        HashMap<String, Integer> original = new HashMap<>();
        byte[] serializedData = SerializationUtils.serialize(original);
        HashMap<String, Integer> deserializedData = (HashMap<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
        Assert.assertEquals(original, deserializedData);

        // Test serialization and deserialization of a HashMap with elements
        HashMap<String, Integer> mapToSerialize = new HashMap<>();
        mapToSerialize.put("one", 1);
        mapToSerialize.put("two", 2);
        byte[] serializedDataWithElements = SerializationUtils.serialize(mapToSerialize);
        HashMap<String, Integer> deserializedDataWithElements = (HashMap<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedDataWithElements));
        Assert.assertEquals(mapToSerialize, deserializedDataWithElements);

        // Test serialization and deserialization of a null HashMap
        HashMap<String, Integer> nullMap = null;
        try {
            byte[] serializedNullMap = SerializationUtils.serialize(nullMap);
            SerializationUtils.deserialize(new ByteArrayInputStream(serializedNullMap));
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }

        // Test serialization and deserialization of a non-Serializable object
        Object nonSerializableObject = new Object() {
            @Override
            public Object writeReplace() throws ObjectStreamException {
                throw new IOException("Object is not serializable");
            }
        };
        try {
            SerializationUtils.serialize(nonSerializableObject);
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }
}