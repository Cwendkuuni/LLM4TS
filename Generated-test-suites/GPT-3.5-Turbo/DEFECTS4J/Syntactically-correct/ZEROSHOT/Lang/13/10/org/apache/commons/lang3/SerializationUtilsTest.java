import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class SerializationUtilsTest {

    @Test
    public void testCloneNull() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testClone() {
        SerializableObject obj = new SerializableObject(1, "test");

        SerializableObject clonedObj = SerializationUtils.clone(obj);

        assertEquals(obj.getId(), clonedObj.getId());
        assertEquals(obj.getName(), clonedObj.getName());
    }

    @Test(expected = SerializationException.class)
    public void testCloneSerializationException() {
        NotSerializableObject obj = new NotSerializableObject(1, "test");

        SerializationUtils.clone(obj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        SerializationUtils.serialize(null, baos);
    }

    @Test
    public void testSerialize() {
        SerializableObject obj = new SerializableObject(1, "test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        SerializationUtils.serialize(obj, baos);

        byte[] serializedObject = baos.toByteArray();

        assertNotNull(serializedObject);
        assertTrue(serializedObject.length > 0);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeSerializationException() {
        NotSerializableObject obj = new NotSerializableObject(1, "test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        SerializationUtils.serialize(obj, baos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testDeserialize() {
        SerializableObject obj = new SerializableObject(1, "test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        SerializableObject deserializedObj = (SerializableObject)SerializationUtils.deserialize(bais);

        assertEquals(obj.getId(), deserializedObj.getId());
        assertEquals(obj.getName(), deserializedObj.getName());
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeSerializationException() {
        byte[] serializedObject = new byte[] { 1, 2, 3, 4, 5 };
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);

        SerializationUtils.deserialize(bais);
    }

    private class SerializableObject implements Serializable {
        private int id;
        private String name;

        public SerializableObject(int id, String name) {
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

    private class NotSerializableObject {
        private int id;
        private String name;

        public NotSerializableObject(int id, String name) {
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