import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private static ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() throws Exception {
        mapper = new ObjectMapper();
    }

    // Test for the constructor with no arguments
    @Test
    public void testStdKeyDeserializerConstructor() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TypeFactory.defaultInstance().constructType(String.class));
        assertEquals(String.class, deserializer.getKeyClass());
    }

    // Test deserialization of a basic String key
    @Test
    public void testDeserializeBasicStringKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TypeFactory.defaultInstance().constructType(String.class));
        Object key = deserializer.deserializeKey("testKey", mapper.getDeserializationContext());
        assertEquals("testKey", key);
    }

    // Test deserialization of a null key
    @Test
    public void testDeserializeNullKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TypeFactory.defaultInstance().constructType(String.class));
        Object key = deserializer.deserializeKey(null, mapper.getDeserializationContext());
        assertNull(key);
    }

    // Test deserialization of an invalid key
    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TypeFactory.defaultInstance().constructType(String.class));
        deserializer.deserializeKey("invalidKey", mapper.getDeserializationContext());
    }

    // Test deserialization of an Enum key
    @Test
    public void testDeserializeEnumKey() throws Exception {
        EnumKD deserializer = new EnumKD(EnumResolver.construct(Locale.class), Locale.class.getMethod("valueOf", String.class));
        Object key = deserializer.deserializeKey("en_US", mapper.getDeserializationContext());
        assertEquals(Locale.US, key);
    }

    // Test deserialization of a Class key
    @Test
    public void testDeserializeClassKey() throws Exception {
        StringKD deserializer = StringKD.forType(String.class);
        Object key = deserializer.deserializeKey("java.lang.String", mapper.getDeserializationContext());
        assertEquals(String.class, key.getClass());
    }

    // Test deserialization of a URL key
    @Test
    public void testDeserializeURLKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TypeFactory.defaultInstance().constructType(URL.class));
        Object key = deserializer.deserializeKey("http://example.com", mapper.getDeserializationContext());
        assertTrue(key instanceof URL);
        assertEquals("http://example.com", ((URL) key).toString());
    }

    // Test deserialization of a UUID key
    @Test
    public void testDeserializeUUIDKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TypeFactory.defaultInstance().constructType(UUID.class));
        Object key = deserializer.deserializeKey("5b6d8226-b3f3-4e92-9d2a-baf31684ebd7", mapper.getDeserializationContext());
        assertTrue(key instanceof UUID);
        assertEquals("5b6d8226-b3f3-4e92-9d2a-baf31684ebd7", key.toString());
    }
}