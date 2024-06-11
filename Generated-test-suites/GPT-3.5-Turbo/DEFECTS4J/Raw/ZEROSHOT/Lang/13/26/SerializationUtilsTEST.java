import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    private SerializationUtils serializationUtils;

    @Before
    public void setup() {
        serializationUtils = new SerializationUtils();
    }

    @Test
    public void testCloneNullObject() {
        Object object = null;
        Object clonedObject = SerializationUtils.clone(object);
        assertNull(clonedObject);
    }

    @Test
    public void testCloneSerializableObject() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        Map<String, Integer> clonedMap = SerializationUtils.clone(map);

        assertNotNull(clonedMap);
        assertNotSame(map, clonedMap);
        assertEquals(map, clonedMap);
    }

    @Test(expected = SerializationException.class)
    public void testCloneClassNotFoundException() {
        ClassLoaderAwareObjectInputStream in = serializationUtils.new ClassLoaderAwareObjectInputStream(null, null);
        in.resolveClass(null);
    }

    @Test
    public void testSerializeNullObject() {
        Object object = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializationUtils.serialize(object, outputStream);
        byte[] bytes = outputStream.toByteArray();

        assertNotNull(bytes);
        assertEquals(0, bytes.length);
    }

    @Test
    public void testSerializeSerializableObject() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializationUtils.serialize(map, outputStream);
        byte[] bytes = outputStream.toByteArray();

        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeIllegalArgumentException() {
        Serializable obj = null;
        serializationUtils.serialize(obj, null);
    }

    @Test
    public void testDeserializeNullInputStream() {
        InputStream inputStream = null;
        Object deserializedObject = serializationUtils.deserialize(inputStream);
        assertNull(deserializedObject);
    }

    @Test
    public void testDeserializeObject() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        byte[] bytes = serializationUtils.serialize(map);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        Object deserializedObject = serializationUtils.deserialize(inputStream);

        assertNotNull(deserializedObject);
        assertTrue(deserializedObject instanceof Map);
        assertEquals(map, deserializedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeIllegalArgumentException() {
        byte[] objectData = null;
        serializationUtils.deserialize(objectData);
    }

}