package org.apache.commons.lang3.SerializationUtils; // Make sure to import necessary packages and classes

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.Serializable;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() throws SerializationException {
        Cloneable original = new HashMap<>();
        Serializable cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
        Assert.assertTrue(original.getClass().isInstance(cloned));
    }

    @Test(expected = SerializationException.class)
    public void testCloneNullInput() {
        SerializationUtils.clone(null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerializeToOutputStream() throws SerializationException, IOException {
        Object input = new HashMap<>();
        OutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(input, outputStream);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream((Byte[]) outputStream.toByteArray()));
        Assert.assertEquals(input, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeToOutputStreamNullInput() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerializeToByteArray() throws SerializationException {
        Object input = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(input);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals(input, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeToByteArrayNullInput() {
        SerializationUtils.serialize(null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFromInputStream() throws SerializationException, IOException {
        Object input = new HashMap<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(input, byteArrayOutputStream);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Assert.assertEquals(input, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeFromInputStreamNullInput() {
        SerializationUtils.deserialize(null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFromByteArray() throws SerializationException {
        Object input = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(input);
        Object deserialized = SerializationUtils.deserialize(serialized);
        Assert.assertEquals(input, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeFromByteArrayNullInput() {
        SerializationUtils.deserialize(null);
    }
}