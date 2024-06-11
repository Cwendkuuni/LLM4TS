package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Tests for {@link SerializationUtils} class.
 */
public class SerializationUtilsTest {

    private static final String TEST_STRING = "TestString";
    private static final int TEST_INT = 123;
    private static final long TEST_LONG = 1234567890123L;
    private static final float TEST_FLOAT = 1.1F;
    private static final double TEST_DOUBLE = 1.2;
    private static final byte[] TEST_BYTE_ARRAY = {1, 2, 3, 4};
    private static final boolean TEST_BOOLEAN = true;
    private static final Object TEST_OBJECT = new Object() {
        private static final long serialVersionUID = 1L;
    };

    private Serializable testSerializable;

    @Before
    public void setup() {
        testSerializable = new SerializableWithClassLoader() {
            private static final long serialVersionUID = 1L;

            static class SerializableWithClassLoader implements Serializable {
                private static final long serialVersionUID = 1L;
            }
        };
    }

    @After
    public void teardown() {
        testSerializable = null;
    }

    @Test
    public void testClone() {
        Object original = new HashMap<>();
        original.put("test", TEST_STRING);

        HashMap<String, String> cloned = SerializationUtils.clone(original);

        assertEquals(original, cloned);
        assertNotNull(cloned);
    }

    @Test
    public void testSerializeDeserializeString() throws IOException {
        String original = TEST_STRING;
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(original, deserialized);
        assertNotNull(deserialized);
    }

    @Test
    public void testSerializeDeserializeInt() throws IOException {
        int original = TEST_INT;
        byte[] serialized = SerializationUtils.serialize(original);
        int deserialized = (int) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(original, deserialized);
        assertNotNull(deserialized);
    }

    @Test
    public void testSerializeDeserializeLong() throws IOException {
        long original = TEST_LONG;
        byte[] serialized = SerializationUtils.serialize(original);
        long deserialized = (long) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(original, deserialized);
        assertNotNull(deserialized);
    }

    @Test
    public void testSerializeDeserializeFloat() throws IOException {
        float original = TEST_FLOAT;
        byte[] serialized = SerializationUtils.serialize(original);
        float deserialized = (float) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(original, deserialized, 0.001F);
        assertNotNull(deserialized);
    }

    @Test
    public void testSerializeDeserializeDouble() throws IOException {
        double original = TEST_DOUBLE;
        byte[] serialized = SerializationUtils.serialize(original);
        double deserialized = (double) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(original, deserialized, 0.001);
        assertNotNull(deserialized);
    }

    @Test
    public void testSerializeDeserializeByteArray() throws IOException {
        byte[] original = TEST_BYTE_ARRAY;
        byte[] serialized = SerializationUtils.serialize(original);
        byte[] deserialized = (byte[]) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertArrayEquals(original, deserialized);
        assertNotNull(deserialized);
    }

    @Test
    public void testSerializeDeserializeBoolean() throws IOException {
        boolean original = TEST_BOOLEAN;
        byte[] serialized = SerializationUtils.serialize(original);
        boolean deserialized = (boolean) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(original, deserialized);
        assertNotNull(deserialized);
    }

    @Test
    public void testSerializeDeserializeCustomClass() throws IOException, ClassNotFoundException {
        Serializable serializeObject = testSerializable;
        byte[] serialized = SerializationUtils.serialize(serializeObject);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(serializeObject.getClass(), deserialized.getClass());
        assertNotNull(deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeException() throws IOException {
        try {
            SerializationUtils.deserialize(null);
        } catch (NullPointerException ex) {
            fail("Expected SerializationException, but got " + ex.getClass().getName());
        }
        throw new AssertionError("Test should have thrown SerializationException");
    }

    @Test
    public void testCloneNullObject() {
        try {
            SerializationUtils.clone(null);
            fail("Expected SerializationException, but did not get one");
        } catch (SerializationException e) {
            assertEquals("NullPointerException: null", e.getCause().getMessage());
        }
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerializeNullObject() {
        try {
            SerializationUtils.serialize(null, new ByteArrayOutputStream(1024));
            fail("Expected SerializationException, but did not get one");
        } catch (IllegalArgumentException e) {
            assertEquals("NullPointerException: null", e.getCause().getMessage());
        }
    }

    @Test
    public void testDeserializeNullObject() {
        try {
            SerializationUtils.deserialize(null);
            fail("Expected SerializationException, but did not get one");
        } catch (NullPointerException e) {
            assertEquals("NullPointerException: null", e.getMessage());
        }
        assertNull(SerializationUtils.deserialize(null));
    }

    private static void assertArrayEquals(byte[] expected, byte[] actual) {
        TestUtils.assertArrayEquals(expected, actual);
    }

    static class SerializableWithClassLoader implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}