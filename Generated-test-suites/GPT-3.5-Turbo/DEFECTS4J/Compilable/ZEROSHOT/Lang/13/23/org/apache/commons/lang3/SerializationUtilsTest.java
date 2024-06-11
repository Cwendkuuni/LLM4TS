package org.apache.commons.lang3;
import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;

public class SerializationUtilsTest {

    // Test clone method
    @Test
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original, cloned);
    }

    // Test serialize methods
    @Test
    public void testSerialize() {
        String object = "Hello World";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(object, baos);
        byte[] serialized = baos.toByteArray();
        Assert.assertNotNull(serialized);
    }

    // Test deserialize methods
    @Test
    public void testDeserialize() {
        String object = "Hello World";
        byte[] serialized = SerializationUtils.serialize(object);
        Object deserialized = SerializationUtils.deserialize(serialized);
        Assert.assertEquals(object, deserialized);
    }

    // Test ClassLoaderAwareObjectInputStream
    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        String object = "Hello World";
        byte[] serialized = SerializationUtils.serialize(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        SerializationUtils.ClassLoaderAwareObjectInputStream in =
                new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, getClass().getClassLoader());
        Object deserialized = in.readObject();
        Assert.assertEquals(object, deserialized);
    }
}