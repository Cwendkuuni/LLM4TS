package org.apache.commons.lang3.test; // Make sure to import necessary classes

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.clone.Clonable;
import org.apache.commons.lang3.serialization.SerializationException;
import org.apache.commons.lang3.SerializationUtils.ClassLoaderAwareObjectInputStream;
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

/**
 * Test class for SerializationUtils
 */
public class SerializationUtilsTest {

    // Test Utils
    //-----------------------------------------------------------------------

    /**
     * Create a test object for testing serialization and deserialization
     */
    static class TestSerializable implements Serializable, Clonable {
        private static final long serialVersionUID = 1L;
        private int value;

        public TestSerializable(int value) {
            this.value = value;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public String toString() {
            return "TestSerializable{" +
                    "value=" + value +
                    '}';
        }
    }

    // Basic Test Cases
    //-----------------------------------------------------------------------

    @Test
    public void testClone() throws SerializationException {
        TestSerializable original = new TestSerializable(42);
        TestSerializable cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original, cloned);
    }

    @Test
    public void testSerializeDeserialize() throws IOException, SerializationException {
        TestSerializable original = new TestSerializable(42);
        byte[] objectData = SerializationUtils.serialize(original);
        TestSerializable deserialized = (TestSerializable) SerializationUtils.deserialize(objectData);
        Assert.assertEquals(original, deserialized);
    }

    @Test
    public void testStreamClosing() throws IOException, SerializationException {
        TestSerializable original = new TestSerializable(42);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(original, outputStream);
        Assert.assertNotNull(outputStream.size());
    }

    // Edge Cases and Exception Handling
    //-----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutputStream() {
        SerializationUtils.serialize(new Object(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullObject() {
        SerializationUtils.clone((Serializable) null);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeWithUnsupportedClassLoader() throws IOException, ClassNotFoundException {
        InputStream inputStream = new ByteArrayInputStream(SerializationUtils.serialize(new Object()).toString().getBytes());
        ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(inputStream, new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                throw new ClassNotFoundException(name);
            }
        });
        in.readObject();
    }

    @Test(expected = SerializationException.class)
    public void testSerializableFailure() throws IOException, ClassNotFoundException {
        InputStream inputStream = new ByteArrayInputStream("invalid byte array".getBytes());
        ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(inputStream, TestSerializable.class.getClassLoader());
        in.readObject();
    }

    @Test(expected = SerializationException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    // Merge all test cases into a single test suite
    //-----------------------------------------------------------------------

    

    @org.junit.runner.RunWith(org.junit.runners.Suite.class)
    @org.junit.Suite.SuiteClasses({
            SerializationUtilsTest.class
    })
    public static class AllTestsSuite {
    }

    
}