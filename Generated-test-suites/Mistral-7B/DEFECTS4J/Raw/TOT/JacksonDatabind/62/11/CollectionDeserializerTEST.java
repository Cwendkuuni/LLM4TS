import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        CollectionDeserializer deserializer = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(Collections.emptyList(), deserializer.forProperty(null));
    }

    @Test
    public void testSingleElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"one\"]";
        CollectionDeserializer deserializer = mapper.readValue(json, CollectionDeserializer.class);
        List<String> result = deserializer.forProperty(null);
        assertEquals(Collections.singletonList("one"), result);
    }

    @Test
    public void testNullCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        CollectionDeserializer deserializer = mapper.readValue(json, CollectionDeserializer.class);
        List<Object> result = deserializer.forProperty(null);
        assertEquals(Collections.singleton(null), result);
    }

    @Test
    public void testComplexCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2.5, \"three\", true, null, [], {}}";
        CollectionDeserializer deserializer = mapper.readValue(json, CollectionDeserializer.class);
        List<Object> result = deserializer.forProperty(null);
        List<Object> expected = Arrays.asList(1, 2.5, "three", true, null, Collections.emptyList(), new HashMap<>());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"invalid\": []}"; // JSON object instead of array
        CollectionDeserializer deserializer = mapper.readValue(json, CollectionDeserializer.class);
        deserializer.forProperty(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCollectionElementType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"one\", 2, true]"; // Incompatible types: String, int, boolean
        CollectionDeserializer deserializer = mapper.registerTypeDeserializer(ArrayList.class, new InvalidTypeDeserializer())
                .readValue(json, CollectionDeserializer.class);
        deserializer.forProperty(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCollectionElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null, [1]]"; // Contains an array instead of single value
        CollectionDeserializer deserializer = mapper.readValue(json, CollectionDeserializer.class);
        deserializer.forProperty(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCircularReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{ \"ref\": {" + ObjectMapper.DEFAULT_REFERENCE_NAME + ": 1" + "}}]";
        CollectionDeserializer deserializer = mapper.registerTypeDeserializer(HashMap.class, new CircularReferenceDeserializer())
                .readValue(json, CollectionDeserializer.class);
        deserializer.forProperty(null);
    }

    static class CircularReferenceDeserializer extends StdDeserializer<HashMap<String, Object>> {
        public CircularReferenceDeserializer() { this(null); }
        public CircularReferenceDeserializer(Class<HashMap<String, Object>> t) { super(t); }

        @Override
        public HashMap<String, Object> deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            HashMap<String, Object> map = new HashMap<>();
            deserializeReference(p, ctxt, map);
            return map;
        }

        @SuppressWarnings({"unchecked"})
        private void deserializeReference(JsonParser p, DeserializationContext ctxt, HashMap<String, Object> map)
                throws IOException {
            if (p.nextToken() != JsonToken.FIELD_NAME) {
                throw ctxt.mappingException("Expected START_OBJECT, but found " + p.getCurrentName());
            }
            String fieldName = p.getCurrentName();
            p.nextToken(); // VALUE_STRING
            String refName = ObjectMapper.DEFAULT_REFERENCE_NAME;
            if (!fieldName.equals(refName)) {
                throw ctxt.mappingException("Expected reference with name [" + refName + "], but found [" + fieldName + "]");
            }
            Object id = p.getValueAsToken().getText();
            Object ref = map.get(id);
            if (ref == null) {
                throw ctxt.mappingException("Unknown id [" + id + "]");
            }
            deserializeReference(p, ctxt, (HashMap<String, Object>) ref);
            map.put(id, map);
        }
    }

    static class InvalidTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            throw new JsonMappingException("Invalid collection element type");
        }
    }
}