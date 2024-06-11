import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone_success() {
        // Typical use case: Cloning a serializable object
        MyObject obj = new MyObject(1, "test");
        MyObject cloneObj = SerializationUtils.clone(obj);
        assertNotSame(obj, cloneObj);
        assertEquals(obj.getId(), cloneObj.getId());
        assertEquals(obj.getName(), cloneObj.getName());
    }

    @Test
    public void testClone_nullInput() {
        // Error scenario: Cloning a null object
        MyObject obj = null;
        MyObject cloneObj = SerializationUtils.clone(obj);
        assertNull(cloneObj);
    }

    @Test
    public void testSerialize_success() throws IOException {
        // Typical use case: Serializing an object
        MyObject obj = new MyObject(1, "test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Object deserializedObj = SerializationUtils.deserialize(bais);
        assertNotNull(deserializedObj);
        assertTrue(deserializedObj instanceof MyObject);
        MyObject deserializedMyObject = (MyObject) deserializedObj;
        assertEquals(obj.getId(), deserializedMyObject.getId());
        assertEquals(obj.getName(), deserializedMyObject.getName());
    }

    @Test(expected = SerializationException.class)
    public void testSerialize_nullOutputStream() {
        // Error scenario: Serializing an object with null OutputStream
        MyObject obj = new MyObject(1, "test");
        SerializationUtils.serialize(obj, null);
    }

    @Test
    public void testDeserialize_success() throws IOException {
        // Typical use case: Deserializing an object
        MyObject obj = new MyObject(1, "test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Object deserializedObj = SerializationUtils.deserialize(bais);
        assertNotNull(deserializedObj);
        assertTrue(deserializedObj instanceof MyObject);
        MyObject deserializedMyObject = (MyObject) deserializedObj;
        assertEquals(obj.getId(), deserializedMyObject.getId());
        assertEquals(obj.getName(), deserializedMyObject.getName());
    }

    @Test(expected = SerializationException.class)
    public void testDeserialize_nullInputStream() {
        // Error scenario: Deserializing an object with null InputStream
        SerializationUtils.deserialize(null);
    }

    // Additional test cases for edge cases can be added here

    private static class MyObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private int id;
        private String name;

        public MyObject(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}