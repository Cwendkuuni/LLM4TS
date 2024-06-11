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
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original, cloned);
    }

    @Test
    public void testSerializeAndDeserializeString() {
        String original = "Hello World";
        byte[] bytes = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(bytes);
        Assert.assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeAndDeserializeObjectWithHashMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        byte[] bytes = SerializationUtils.serialize(map);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(bytes);
        Assert.assertEquals(map, deserializedMap);
    }

    @Test(expected = SerializationException.class)
    public void testNullObjectToSerializeThrowsException() {
        SerializationUtils.serialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testNullOutputStreamToSerializeThrowsException() {
        SerializationUtils.serialize(new Serializable() {
        }, null);
    }

    @Test(expected = SerializationException.class)
    public void testNullInputSteamToDeserializeThrowsException() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testNullObjectToDeserializeThrowsException() {
        SerializationUtils.deserialize(new byte[0]);
    }
}