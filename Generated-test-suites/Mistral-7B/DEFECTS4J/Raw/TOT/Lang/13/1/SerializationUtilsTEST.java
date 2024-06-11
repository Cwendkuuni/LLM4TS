import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.bytecode.Bytecode;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class SerializationUtilsTest {

    private static final int TEST_OBJECT_SIZE = 1024;
    private static final int TEST_OBJECT_DEEP_SIZE = 2048;

    @Parameters(name = "{index}: test object serialization and deserialization with size={0} and deep size={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Integer(1), TEST_OBJECT_SIZE, TEST_OBJECT_DEEP_SIZE},
                {new String("test"), TEST_OBJECT_SIZE, TEST_OBJECT_DEEP_SIZE},
                {new HashMap<Integer, Integer>(){{put(1, 1);}}, TEST_OBJECT_SIZE * 2, TEST_OBJECT_DEEP_SIZE * 2},
                {new CloneableTestObject(), TEST_OBJECT_SIZE, TEST_OBJECT_DEEP_SIZE},
                {new BytecodeTestObject(), TEST_OBJECT_SIZE, TEST_OBJECT_DEEP_SIZE}
        });
    }

    @BeforeClass
    public static void setup() {
        // Initialize SerializationUtils for test use
    }

    private Object testObject;
    private int objectSize;
    private int objectDeepSize;

    public SerializationUtilsTest(Object testObject, int objectSize, int objectDeepSize) {
        this.testObject = testObject;
        this.objectSize = objectSize;
        this.objectDeepSize = objectDeepSize;
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        byte[] serialized = SerializationUtils.serialize(testObject);
        Assert.assertNotNull(serialized);
        Assert.assertTrue(serialized.length > 0);
    }

    @Test
    public void testDeepSerialization() throws CloneException, IOException, ClassNotFoundException {
        byte[] serialized = SerializationUtils.serialize(testObject);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        Assert.assertEquals(testObject, deserialized);

        if (testObject instanceof Cloneable) {
            Object cloned = SerializationUtils.clone((Cloneable) testObject);
            byte[] clonedSerialized = SerializationUtils.serialize(cloned);
            Object clonedDeserialized = SerializationUtils.deserialize(new ByteArrayInputStream(clonedSerialized));

            Assert.assertEquals(cloned, clonedDeserialized);
        }

        if (testObject instanceof Serializable && (testObject.getClass().getDeclaredConstructor().isAccessible())) {
            Constructor<Serializable> constructor = (Constructor<Serializable>) testObject.getClass().getDeclaredConstructor();
            constructor.setAccessible(true);

            Serializable original = (Serializable) testObject;
            Serializable cloned = constructor.newInstance();

            byte[] originalSerialized = SerializationUtils.serialize(original);
            byte[] clonedSerialized = SerializationUtils.serialize(cloned);

            Assert.assertArrayNotEquals(originalSerialized, clonedSerialized);

            Object originalDeserialized = SerializationUtils.deserialize(new ByteArrayInputStream(originalSerialized));
            Object clonedDeserialized = SerializationUtils.deserialize(new ByteArrayInputStream(clonedSerialized));

            Assert.assertEquals(original, originalDeserialized);
            Assert.assertEquals(cloned, clonedDeserialized);
        }
    }

    @Test(expected = SerializationException.class)
    public void testExceptionSerialization() throws IOException, ClassNotFoundException {
        SerializationUtils.serialize(new SerializationException("Test Exception"));
    }

    @Test(expected = SerializationException.class)
    public void testExceptionDeserialization() throws IOException, ClassNotFoundException {
        byte[] serializedException = SerializationUtils.serialize(new SerializationException("Test Exception"));
        SerializationUtils.deserialize(new ByteArrayInputStream(serializedException));
    }

    @Test(expected = Serializable.InvalidObjectException.class)
    public void testInvalidObjectDeserialization() throws IOException, ClassNotFoundException {
        byte[] serializedInvalidObject = new byte[]{(byte) 0xCA, (byte) 0xCA, (byte) 0xCA, (byte) 0xCA};
        SerializationUtils.deserialize(new ByteArrayInputStream(serializedInvalidObject));
    }

    @Test
    public void testSerializationPerformance() throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            byte[] serialized = SerializationUtils.serialize(testObject);
            SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        }
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        System.out.printf("Serialization and deserialization performance test with object of size %d and deep size %d: %d ms\n",
                objectSize, objectDeepSize, duration);
    }

    private static class CloneableTestObject implements Cloneable {
        private int id;

        public CloneableTestObject() {
            id = 1;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    private static class BytecodeTestObject implements Serializable {
        private int id;

        public BytecodeTestObject() {
            id = 1;
        }
    }
}