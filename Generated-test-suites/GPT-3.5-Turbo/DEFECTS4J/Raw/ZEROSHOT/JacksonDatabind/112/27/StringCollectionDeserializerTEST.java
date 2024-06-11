import static org.junit.Assert.*;
import org.junit.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize() throws IOException {
        String json = "[\"value1\", \"value2\"]";
        Collection<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Collection<String> result = deserializer.deserialize(p, ctxt);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        String json = "[\"value1\", \"value2\"]";
        Collection<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Collection<String> result = deserializer.deserializeWithType(p, ctxt, null);

        assertEquals(expected, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        String json = "\"value1\"";
        Collection<String> expected = new ArrayList<>();
        expected.add("value1");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Collection<String> result = deserializer.handleNonArray(p, ctxt, new ArrayList<String>());

        assertEquals(expected, result);
    }

}