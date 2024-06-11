package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    // Clone
    //-----------------------------------------------------------------------

    /**
     * Test method for {@link SerializationUtils#clone(Object)} with non-null input.
     */
    @Test
    public void testCloneNonnull() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
    }

    /**
     * Test method for {@link SerializationUtils#clone(Object)} with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloneNull() {
        SerializationUtils.clone(null);
    }

    // Serialize
    //-----------------------------------------------------------------------

    /**
     * Test method for {@link SerializationUtils#serialize(Object, OutputStream)} with non-null input.
     */
    @Test
    public void testSerializeNonnull() throws IOException {
        String stringToSerialize = "Hello World";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(stringToSerialize, byteArrayOutputStream);
        byte[] serializedData = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        String deserializedData = SerializationUtils.deserialize(byteArrayInputStream);
        Assert.assertEquals(stringToSerialize, deserializedData);
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object)} with non-null input.
     */
    @Test
    public void testSerializeSingleObject() throws IOException {
        String stringToSerialize = "Hello World";
        byte[] serializedData = SerializationUtils.serialize(stringToSerialize);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        String deserializedData = SerializationUtils.deserialize(byteArrayInputStream);
        Assert.assertEquals(stringToSerialize, deserializedData);
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object, OutputStream)} with null output.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        SerializationUtils.serialize(new HashMap<>(), null);
    }

    // Deserialize
    //-----------------------------------------------------------------------

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with non-null input.
     */
    @Test
    public void testDeserializeNonnull() throws IOException, ClassNotFoundException {
        String original = "Hello World";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        String deserializedData = (String) SerializationUtils.deserialize(byteArrayInputStream);
        Assert.assertEquals(original, deserializedData);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])} with non-null input.
     */
    @Test
    public void testDeserializeSingleByteArray() throws IOException, ClassNotFoundException {
        String original = "Hello World";
        byte[] serializedData = SerializationUtils.serialize(original);
        String deserializedData = (String) SerializationUtils.deserialize(serializedData);
        Assert.assertEquals(original, deserializedData);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])} with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullByteArray() {
        SerializationUtils.deserialize(null);
    }

    // Custom ClassLoaderAwareObjectInputStream
    //-----------------------------------------------------------------------

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} using custom ClassLoaderAwareObjectInputStream.
     */
    @Test
    public void testDeserializeCustomClassLoader() throws IOException, ClassNotFoundException {
        String original = "Hello World";
        byte[] serializedData = SerializationUtils.serialize(original);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        CustomClassLoaderAwareObjectInputStream customClassLoaderAwareObjectInputStream = new CustomClassLoaderAwareObjectInputStream(byteArrayInputStream, this.getClass().getClassLoader());
        String deserializedData = (String) customClassLoaderAwareObjectInputStream.readObject();
        Assert.assertEquals(original, deserializedData);
    }

}

class CustomClassLoaderAwareObjectInputStream extends SerializationUtils.ClassLoaderAwareObjectInputStream {
    public CustomClassLoaderAwareObjectInputStream(InputStream inputStream, ClassLoader classLoader) throws IOException {
        super(inputStream, classLoader);
    }
}