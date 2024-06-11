import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize_emptyCollection() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        String json = "[]";
        Collection<String> result = deserializer.deserialize(json);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserialize_singleValue() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        String json = "[\"value\"]";
        Collection<String> result = deserializer.deserialize(json);
        assertEquals(1, result.size());
        assertEquals("value", result.iterator().next());
    }

    @Test
    public void testDeserialize_multipleValues() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        String json = "[\"value1\", \"value2\", \"value3\"]";
        Collection<String> result = deserializer.deserialize(json);
        assertEquals(3, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("value3"));
    }

    @Test
    public void testDeserialize_withNullValues() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        String json = "[\"value1\", null, \"value2\"]";
        Collection<String> result = deserializer.deserialize(json);
        assertEquals(3, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains(null));
        assertTrue(result.contains("value2"));
    }

    @Test
    public void testDeserialize_invalidJson() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        String json = "[value1, value2, value3]";
        try {
            Collection<String> result = deserializer.deserialize(json);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // success
        }
    }

    @Test
    public void testDeserializeWithType() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        String json = "[\"value1\", \"value2\", \"value3\"]";
        try {
            Object result = deserializer.deserializeWithType(json);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }
}