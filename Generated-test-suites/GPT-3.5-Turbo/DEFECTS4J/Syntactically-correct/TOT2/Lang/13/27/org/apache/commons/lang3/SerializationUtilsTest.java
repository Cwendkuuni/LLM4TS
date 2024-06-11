import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.commons.lang3.SerializationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SerializationUtilsTest {

    SerializationUtilsTest obj;

    @Before
    public void setUp() throws Exception {
        obj = new SerializationUtilsTest();
    }

    @After
    public void tearDown() throws Exception {
        obj = null;
    }

    @Test
    public void testClone() {
        SerializationUtilsTest object = new SerializationUtilsTest();
        SerializationUtilsTest clonedObject = SerializationUtils.clone(object);
        assertNotNull(clonedObject);
        assertNotSame(object, clonedObject);
    }

    @Test
    public void testSerialize() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtilsTest object = new SerializationUtilsTest();
        SerializationUtils.serialize(object, outputStream);
        byte[] data = outputStream.toByteArray();
        assertNotNull(data);
        assertTrue(data.length > 0);
    }

    @Test
    public void testDeserialize() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtilsTest object = new SerializationUtilsTest();
        SerializationUtils.serialize(object, outputStream);
        byte[] data = outputStream.toByteArray();
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        SerializationUtilsTest deserializedObject = (SerializationUtilsTest) SerializationUtils.deserialize(inputStream);
        
        assertNotNull(deserializedObject);
        assertNotSame(object, deserializedObject);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeWithNullObject() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, outputStream);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeWithNullStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeWithEmptyByteArray() {
        SerializationUtils.deserialize(new byte[0]);
    }
}