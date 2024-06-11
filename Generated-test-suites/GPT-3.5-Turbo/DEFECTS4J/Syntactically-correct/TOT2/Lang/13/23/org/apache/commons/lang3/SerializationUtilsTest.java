import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest {

    // Test for clone method with typical use case
    @Test
    public void testCloneTypical() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(map);

        assertEquals(map, clonedMap);
        assertNotSame(map, clonedMap);
    }

    // Test for clone method with null input
    @Test
    public void testCloneNullInput() {
        Map<String, Integer> map = null;
        Map<String, Integer> clonedMap = SerializationUtils.clone(map);

        assertNull(clonedMap);
    }

    // Test for serialize method with typical use case
    @Test
    public void testSerializeTypical() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);

        byte[] serializedData = SerializationUtils.serialize(map);
        assertNotNull(serializedData);
    }

    // Test for serialize method with null input
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullInput() {
        SerializationUtils.serialize(null);
    }

    // Test for deserialize method with typical use case
    @Test
    public void testDeserializeTypical() {
        byte[] data = {1, 2, 3, 4};
        Object deserializedObject = SerializationUtils.deserialize(data);

        assertNotNull(deserializedObject);
    }

    // Test for deserialize method with null input
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInput() {
        SerializationUtils.deserialize(null);
    }

    // Test for ClassLoaderAwareObjectInputStream resolveClass method with valid class
    @Test
    public void testResolveClassValid() throws IOException, ClassNotFoundException {
        ClassLoaderAwareObjectInputStream ois = new ClassLoaderAwareObjectInputStream(null, getClass().getClassLoader());
        ObjectStreamClass desc = ObjectStreamClass.lookup(String.class);
        Class<?> resolvedClass = ois.resolveClass(desc);

        assertEquals(String.class, resolvedClass);
    }

    // Test for ClassLoaderAwareObjectInputStream resolveClass method with invalid class
    @Test(expected = ClassNotFoundException.class)
    public void testResolveClassInvalid() throws IOException, ClassNotFoundException {
        ClassLoaderAwareObjectInputStream ois = new ClassLoaderAwareObjectInputStream(null, null);
        ObjectStreamClass desc = ObjectStreamClass.lookup("NonExistingClass");
        ois.resolveClass(desc);
    }

}