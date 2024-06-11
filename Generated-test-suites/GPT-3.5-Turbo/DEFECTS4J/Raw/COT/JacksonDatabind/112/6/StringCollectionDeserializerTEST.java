import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new StringCollectionDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = createJsonParser("[\"test1\", \"test2\", \"test3\"]");
        DeserializationContext ctxt = createDeserializationContext();
        Collection<String> expected = new ArrayList<>();
        expected.add("test1");
        expected.add("test2");
        expected.add("test3");
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser parser = createJsonParser("[]");
        DeserializationContext ctxt = createDeserializationContext();
        Collection<String> expected = new ArrayList<>();
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonParser parser = createJsonParser("[null]");
        DeserializationContext ctxt = createDeserializationContext();
        Collection<String> expected = new ArrayList<>();
        expected.add(null);
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJsonArray() throws IOException {
        JsonParser parser = createJsonParser("\"test\"");
        DeserializationContext ctxt = createDeserializationContext();
        deserializer.deserialize(parser, ctxt);
    }

    private JsonParser createJsonParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(json);
    }

    private DeserializationContext createDeserializationContext() {
        return new DeserializationContext(null, null);
    }
}