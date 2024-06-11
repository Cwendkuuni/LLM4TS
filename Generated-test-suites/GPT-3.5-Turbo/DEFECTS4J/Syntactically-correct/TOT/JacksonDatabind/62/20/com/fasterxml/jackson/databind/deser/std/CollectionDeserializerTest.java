import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import com.fasterxml.jackson.databind.JsonMappingException;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize_EmptyArray_ReturnsEmptyCollection() throws Exception {
        String json = "[]";
        CollectionDeserializer deserializer = new CollectionDeserializer(
                Collection.class, null, null, null);
        Collection<Object> result = deserializer.deserialize(
                new ObjectMapper().readTree(json), null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserialize_NullValue_ReturnsNull() throws Exception {
        String json = "null";
        CollectionDeserializer deserializer = new CollectionDeserializer(
                Collection.class, null, null, null);
        Collection<Object> result = deserializer.deserialize(
                new ObjectMapper().readTree(json), null);
        assertNull(result);
    }

    @Test
    public void testDeserialize_SingleValue_ReturnsCollectionWithSingleValue() throws Exception {
        String json = "[1]";
        CollectionDeserializer deserializer = new CollectionDeserializer(
                Collection.class, null, null, null);
        Collection<Object> result = deserializer.deserialize(
                new ObjectMapper().readTree(json), null);
        assertEquals(1, result.size());
        assertTrue(result.contains(1));
    }

    @Test
    public void testDeserialize_MultipleValues_ReturnsCollectionWithAllValues() throws Exception {
        String json = "[1,2,3]";
        CollectionDeserializer deserializer = new CollectionDeserializer(
                Collection.class, null, null, null);
        Collection<Object> result = deserializer.deserialize(
                new ObjectMapper().readTree(json), null);
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidArray_ThrowsJsonMappingException() throws Exception {
        String json = "[1,2,";
        CollectionDeserializer deserializer = new CollectionDeserializer(
                Collection.class, null, null, null);
        deserializer.deserialize(new ObjectMapper().readTree(json), null);
    }
}