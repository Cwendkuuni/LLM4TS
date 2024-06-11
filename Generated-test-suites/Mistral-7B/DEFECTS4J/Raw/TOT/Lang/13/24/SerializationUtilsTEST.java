package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.exception.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * A simple JavaBean class used for serialization/deserialization testing.
     */
    static class TestBean implements Serializable {
        private static final long serialVersionUID = -5178642229393234952L;
        private int id;
        private String name;
        private Map<String, String> map;

        public TestBean(int id, String name, Map<String, String> map) {
            this.id = id;
            this.name = name;
            this.map = map;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Map<String, String> getMap() {
            return map;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestBean that = (TestBean) o;
            return new EqualsBuilder()
                    .append(getId(), that.getId())
                    .append(getName(), that.getName())
                    .append(getMap(), that.getMap())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(getId())
                    .append(getName())
                    .append(getMap())
                    .toHashCode();
        }
    }

    /**
     * Test for {@link SerializationUtils#clone(Object)} method.
     */
    @Test
    public void testClone() throws IOException, SerializationException {
        // Given
        TestBean original = new TestBean(1, "Original", new HashMap<>());

        // When
        TestBean cloned = SerializationUtils.clone(original);

        // Then
        Assert.assertEquals(original, cloned);
        Assert.assertTrue(original != cloned);
    }

    /**
     * Test for {@link SerializationUtils#serialize(Object, OutputStream)} method.
     */
    @Test
    public void testSerialize() throws IOException, SerializationException {
        // Given
        TestBean original = new TestBean(1, "Original", new HashMap<>());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // When
        SerializationUtils.serialize(original, baos);
        byte[] serializedData = baos.toByteArray();

        // Then
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        TestBean deserializedObject = SerializationUtils.deserialize(bais);

        // And
        Assert.assertEquals(original, deserializedObject);
    }

    /**
     * Test for {@link SerializationUtils#deserialize(InputStream)} method.
     */
    @Test
    public void testDeserialize() throws IOException, SerializationException {
        // Given
        TestBean original = new TestBean(1, "Original", new HashMap<>());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, baos);
        byte[] serializedData = baos.toByteArray();

        // When
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        TestBean deserializedObject = SerializationUtils.deserialize(bais);

        // Then
        Assert.assertEquals(original, deserializedObject);
    }

    /**
     * Test for {@link SerializationUtils#deserialize(byte[])} method.
     */
    @Test
    public void testDeserializeByteArray() throws IOException, SerializationException {
        // Given
        TestBean original = new TestBean(1, "Original", new HashMap<>());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, baos);
        byte[] serializedData = baos.toByteArray();

        // When
        TestBean deserializedObject = SerializationUtils.deserialize(serializedData);

        // Then
        Assert.assertEquals(original, deserializedObject);
    }

    /**
     * Test for {@link SerializationUtils#serialize(Object, OutputStream)} method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        // Given
        OutputStream outputStream = new ByteArrayOutputStream();

        // When
        SerializationUtils.serialize(null, outputStream);

        // Then exception
    }

    /**
     * Test for {@link SerializationUtils#serialize(Object, OutputStream)} method with null output stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        // Given
        Serializable objectToSerialize = new Object() {
        };

        // When
        SerializationUtils.serialize(objectToSerialize, null);

        // Then exception
    }

    /**
     * Test for {@link SerializationUtils#deserialize(InputStream)} method with null input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        // Given

        // When
        SerializationUtils.deserialize(null);

        // Then exception
    }

    /**
     * Test for {@link SerializationUtils#deserialize(byte[])} method with null byte array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullByteArray() {
        // Given

        // When
        SerializationUtils.deserialize(null);

        // Then exception
    }

    /**
     * Test for {@link SerializationUtils#deserialize(byte[])} method with invalid byte array data.
     */
    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidByteArrayData() {
        // Given
        byte[] invalidByteArrayData = new byte[10];

        // When
        SerializationUtils.deserialize(invalidByteArrayData);

        // Then exception
    }
}