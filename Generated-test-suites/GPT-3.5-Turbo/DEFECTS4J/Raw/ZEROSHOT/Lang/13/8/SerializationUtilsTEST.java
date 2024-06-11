import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testCloneNullObject() {
        Object object = null;
        Object clonedObject = SerializationUtils.clone(object);
        assertNull(clonedObject);
    }

    @Test
    public void testCloneSerializableObject() {
        SerializableObject object = new SerializableObject("Test Object");
        SerializableObject clonedObject = SerializationUtils.clone(object);

        assertNotNull(clonedObject);
        assertNotSame(object, clonedObject);
        assertEquals(object.getValue(), clonedObject.getValue());
    }

    @Test(expected = SerializationException.class)
    public void testCloneNonSerializableObject() {
        NonSerializableObject object = new NonSerializableObject("Test Object");
        SerializationUtils.clone(object);
    }

    @Test
    public void testSerializeObjectToOutputStream() {
        SerializableObject object = new SerializableObject("Test Object");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(object, outputStream);

        byte[] serializedObject = outputStream.toByteArray();
        assertNotNull(serializedObject);
        assertTrue(serializedObject.length > 0);
    }

    @Test
    public void testSerializeObjectToByteArray() {
        SerializableObject object = new SerializableObject("Test Object");
        byte[] serializedObject = SerializationUtils.serialize(object);

        assertNotNull(serializedObject);
        assertTrue(serializedObject.length > 0);
    }

    @Test
    public void testDeserializeObjectFromInputStream() {
        SerializableObject object = new SerializableObject("Test Object");
        byte[] serializedObject = SerializationUtils.serialize(object);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedObject);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);

        assertNotNull(deserializedObject);
        assertTrue(deserializedObject instanceof SerializableObject);
        assertEquals(object.getValue(), ((SerializableObject) deserializedObject).getValue());
    }

    @Test
    public void testDeserializeObjectFromByteArray() {
        SerializableObject object = new SerializableObject("Test Object");
        byte[] serializedObject = SerializationUtils.serialize(object);

        Object deserializedObject = SerializationUtils.deserialize(serializedObject);

        assertNotNull(deserializedObject);
        assertTrue(deserializedObject instanceof SerializableObject);
        assertEquals(object.getValue(), ((SerializableObject) deserializedObject).getValue());
    }

    private static class SerializableObject implements Serializable {
        private String value;

        public SerializableObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static class NonSerializableObject {
        private String value;

        public NonSerializableObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}