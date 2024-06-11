import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    // Clone
    //-----------------------------------------------------------------------
    @Test
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original, cloned);
    }

    @Test
    public void testCloneNull() {
        String original = null;
        String cloned = SerializationUtils.clone(original);
        Assert.assertNull(cloned);
    }

    // Serialize
    //-----------------------------------------------------------------------
    @Test
    public void testSerialize() {
        String original = "Hello World";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, baos);
        byte[] serialized = baos.toByteArray();
        Assert.assertNotNull(serialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNull() {
        OutputStream outputStream = null;
        SerializationUtils.serialize(null, outputStream);
    }

    // Deserialize
    //-----------------------------------------------------------------------
    @Test
    public void testDeserialize() {
        String original = "Hello World";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, baos);
        byte[] serialized = baos.toByteArray();
        Object deserialized = SerializationUtils.deserialize(serialized);
        Assert.assertEquals(original, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNull() {
        InputStream inputStream = null;
        SerializationUtils.deserialize(inputStream);
    }

}