import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[1, 2, 3, 4, 5]";
        Collection<Integer> collection = mapper.readValue(jsonArray, CollectionDeserializer.class);
        assertEquals(5, collection.size());
        for (int i = 0; i < collection.size(); i++) {
            assertEquals(i + 1, collection.get(i));
        }
    }

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[]";
        Collection<Integer> collection = mapper.readValue(jsonArray, CollectionDeserializer.class);
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testNullCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "null";
        Collection<Integer> collection = mapper.readValue(jsonArray, CollectionDeserializer.class);
        assertNull(collection);
    }

    @Test
    public void testEmptyListDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[ ]";
        List<Integer> list = mapper.readValue(jsonArray, CollectionDeserializer.class);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testNullValueInCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[null, 1, 2, 3, null]";
        Collection<Integer> collection = mapper.readValue(jsonArray, CollectionDeserializer.class);
        assertEquals(5, collection.size());
        assertTrue(collection.contains(null));
    }

    @Test
    public void testEmptyMapDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[{}]";
        Map<String, Integer> map = mapper.readValue(jsonArray, CollectionDeserializer.class);
        assertTrue(map.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializationWithInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "abc";
        mapper.readValue(jsonArray, CollectionDeserializer.class);
    }
}