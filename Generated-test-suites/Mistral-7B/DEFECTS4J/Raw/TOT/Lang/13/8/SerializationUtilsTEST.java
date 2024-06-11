import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import org.junit.Assert;
import org.junit.rules.ExpectedException;

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
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);

        Map<String, Integer> cloned = SerializationUtils.clone(original);

        Assert.assertNotNull(cloned);
        Assert.assertEquals(original, cloned);

        // Test modification of cloned map
        cloned.put("three", 3);
        Assert.assertNotSame(original, cloned);
    }

    @Test
    public void testSerializeDeserialize() throws IOException, SerializationException {
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);

        byte[] serialized = SerializationUtils.serialize(original);
        Map<String, Integer> deserialized = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        Assert.assertNotNull(deserialized);
        Assert.assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeDeserializeWithException() {
        ExpectedException.assertThrows(SerializationException.class, () -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SerializationUtils.serialize(null, outputStream);
        });

        ExpectedException.assertThrows(SerializationException.class, () -> {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
            SerializationUtils.deserialize(inputStream);
        });
    }
}