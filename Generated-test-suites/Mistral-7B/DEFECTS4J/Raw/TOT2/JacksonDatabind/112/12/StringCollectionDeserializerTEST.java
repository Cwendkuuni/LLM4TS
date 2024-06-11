import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testDefaultDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"hello\", \"world\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(node, StringCollectionDeserializer.class)[0]);
        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @Test
    public void testValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[{\"value\":\"hi\"}, {\"value\":\"there\"}]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                new BasicValueDeserializer<>(), null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(node, StringCollectionDeserializer.class)[0]);
        assertEquals(Arrays.asList("hi", "there"), result);
    }

    @Test(expected = IOException.class)
    public void testEmptyArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        deserializer.deserialize(mapper.getFactory().createParser("[ ]"), new TestContext());
    }

    @Test(expected = IOException.class)
    public void testNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        deserializer.deserialize(mapper.getFactory().createParser("[null]"), new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testUnexpectedToken() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        deserializer.deserialize(mapper.getFactory().createParser("[1]"), new TestContext());
    }
}