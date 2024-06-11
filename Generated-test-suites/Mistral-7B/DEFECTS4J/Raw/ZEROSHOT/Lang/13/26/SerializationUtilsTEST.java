import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.ByteArrayInputStream;
import org.apache.commons.lang3.ByteArrayOutputStream;
import org.apache.commons.lang3.ObjectInputStream;
import org.apache.commons.lang3.ObjectOutputStream;
import org.apache.commons.lang3.ObjectStreamClass;
import org.apache.commons.lang3.ClassLoader;
import org.apache.commons.lang3.ClassLoaderAwareObjectInputStream;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    private Serializable serializeObject;
    private Serializable deserializeObject;

    @Before
    public void setUp() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("one", 1);
        map.put("two", 2);
        this.serializeObject = map;
    }

    @Test
    public void testClone() throws SerializationException {
        Serializable clonedObject = SerializationUtils.clone(this.serializeObject);
        Assert.assertNotNull(clonedObject);
        Assert.assertEquals(this.serializeObject, clonedObject);
        Map clonedMap = (Map) clonedObject;
        Assert.assertEquals(2, clonedMap.size());
        Assert.assertEquals(1, clonedMap.get("one"));
        Assert.assertEquals(2, clonedMap.get("two"));
    }

    @Test
    public void testSerialize() throws SerializationException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(this.serializeObject, outputStream);
        byte[] serializedObject = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(serializedObject);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
        Assert.assertNotNull(deserializedObject);
        Map deserializedMap = (Map) deserializedObject;
        Assert.assertEquals(2, deserializedMap.size());
        Assert.assertEquals(1, deserializedMap.get("one"));
        Assert.assertEquals(2, deserializedMap.get("two"));
        Assert.assertEquals(this.serializeObject, deserializedObject);
    }

    @Test
    public void testSerializableException() {
        try {
            SerializationUtils.serialize(null, new ByteArrayOutputStream());
            Assert.fail();
        } catch (SerializationException e) {
            Assert.assertEquals("SerializationUtils should not be able to serialize a null object.", e.getMessage());
        }
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(SerializationUtils.serialize(this.serializeObject).getBytes());
        ClassLoaderAwareObjectInputStream classLoaderAwareObjectInputStream = new ClassLoaderAwareObjectInputStream(byteArrayInputStream, classLoader);
        Object deserializedObject = classLoaderAwareObjectInputStream.readObject();
        Assert.assertEquals(this.serializeObject, deserializedObject);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(SerializationUtils.serialize(this.serializeObject).getBytes());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();
    }
}