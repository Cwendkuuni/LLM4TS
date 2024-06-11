import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RunWith(PowerMockRunner.class)
public class SerializationUtilsTest {

    private static final String TEST_CLASS_NAME = "SerializationUtilsTestData";
    private static final int TEST_INT_VALUE = 123;
    private static final String TEST_STRING_VALUE = "SerializationUtilsTest";
    private static final byte[] TEST_BYTE_ARRAY = new byte[] {1, 2, 3};

    @PowerMockRule
    public PowerMockRule powerMockRule;

    @After
    public void setupClassLoader() {
        ClassLoader originalClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader classLoader = new AnnotatedClassLoader(SerializationUtilsTest.class);
        ClassLoaderAwareObjectInputStream.setClassLoader(classLoader);
        ClassLoader.setDefaultClassLoader(classLoader);
    }

    @After
    public void afterEachTest() {
        ClassLoader.setDefaultClassLoader(originalClassLoader);
    }

    @Test
    public void testClone() throws SerializationException {
        Object originalObject = new Object() {
            private static final long serialVersionUID = 1L;
            private int intValue = TEST_INT_VALUE;
            private String stringValue = TEST_STRING_VALUE;
        };

        Serializable clonedObject = SerializationUtils.clone(originalObject);

        Assert.assertNotNull(clonedObject);
        Assert.assertNotSame(originalObject, clonedObject);
        Assert.assertEquals(originalObject.getClass(), clonedObject.getClass());
        Assert.assertEquals(originalObject.getIntValue(), clonedObject.getIntValue());
        Assert.assertEquals(originalObject.getStringValue(), clonedObject.getStringValue());
    }

    @Test
    public void testSerializationAndDeserializationOfBasicTypes() throws SerializationException {
        int intValue = TEST_INT_VALUE;
        String stringValue = TEST_STRING_VALUE;
        byte[] byteArray = TEST_BYTE_ARRAY;

        byte[] serializedIntValue = SerializationUtils.serialize(intValue);
        byte[] serializedStringValue = SerializationUtils.serialize(stringValue);
        byte[] serializedByteArray = SerializationUtils.serialize(byteArray);

        int deserializedIntValue = (int) SerializationUtils.deserialize(new ByteArrayInputStream(serializedIntValue));
        String deserializedStringValue = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serializedStringValue));
        byte[] deserializedByteArray = (byte[]) SerializationUtils.deserialize(new ByteArrayInputStream(serializedByteArray));

        Assert.assertEquals(intValue, deserializedIntValue);
        Assert.assertEquals(stringValue, deserializedStringValue);
        Assert.assertArrayEquals(byteArray, deserializedByteArray);
    }

    @Test
    public void testSerializationAndDeserializationOfHashMap() throws SerializationException {
        Map<String, Integer> map = new HashMap<>();
        map.put(TEST_STRING_VALUE, TEST_INT_VALUE);

        byte[] serializedMap = SerializationUtils.serialize(map);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedMap));

        Assert.assertEquals(map.size(), deserializedMap.size());
        Assert.assertEquals(map.get(TEST_STRING_VALUE), deserializedMap.get(TEST_STRING_VALUE));
    }

    @Test(expected = SerializationException.class)
    public void testSerializeIOException() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;

        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(new Object() {
                private static final long serialVersionUID = 1L;
            });
        } catch (IOException e) {
            throw new SerializationException("IOException while writing object to outputstream", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw new SerializationException("IOException on closing outputstream", e);
            }
        }
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeIOException() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {});
        ObjectInputStream in = null;

        try {
            in = new ObjectInputStream(bais);
            in.readObject();
        } catch (IOException e) {
            throw new SerializationException("IOException while reading object from inputstream", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new SerializationException("IOException on closing inputstream", e);
            }
        }
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeClassNotFoundException() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais, getClass().getClassLoader());

        try {
            in.readObject();
        } catch (IOException e) {
            throw new SerializationException("IOException while reading object from inputstream", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new SerializationException("IOException on closing inputstream", e);
            }
        }
    }
}