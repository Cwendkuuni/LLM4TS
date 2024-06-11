import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;;

public class StringCollectionDeserializerTest {
    @Test
    public void testDeserializeEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testDeserializeSingleValueCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"Hello World\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], null);
        assertEquals(Arrays.asList("Hello World"), result);
    }

    @Test
    public void testDeserializeCollectionWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"Hello World\", null]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], null);
        assertEquals(Arrays.asList("Hello World", null), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeCollectionWithInvalidValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1, \"Hello World\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], null);
        fail("Expected JsonMappingException");
    }
}