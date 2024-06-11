import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.Collection;

public class CollectionDeserializerTest {

    private CollectionDeserializer collectionDeserializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        collectionDeserializer = new CollectionDeserializer();
        objectMapper = new ObjectMapper(new JsonFactory());
    }

    @Test
    public void testDeserialize_emptyArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        assert result.isEmpty();
    }

    @Test
    public void testDeserialize_singleElementArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1]");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        assert result.size() == 1;
        assert result.contains(1);
    }

    @Test
    public void testDeserialize_nestedArrays() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[[1, 2], [3, 4]]");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        assert result.size() == 2;
        assert result.contains(new int[]{1, 2});
        assert result.contains(new int[]{3, 4});
    }

    @Test
    public void testDeserialize_withNullValues() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[null, null]");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        assert result.size() == 2;
        assert result.contains(null);
    }

    @Test(expected = Exception.class)
    public void testDeserialize_invalidJson() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        collectionDeserializer.deserialize(jsonParser, deserializationContext);
    }
}