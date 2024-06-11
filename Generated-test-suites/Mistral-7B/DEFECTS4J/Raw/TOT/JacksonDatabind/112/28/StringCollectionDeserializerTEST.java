import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    public StringCollectionDeserializerTest() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        this.objectMapper = new ObjectMapper(jsonFactory);
    }

    @Test
    public void testDeserializeEmptyCollection() throws IOException {
        String jsonEmpty = "[]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonEmpty);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(TypeFactory.defaultInstance.constructCollectionType(List.class, String.class), null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testDeserializeSingleValueCollection() throws IOException {
        String jsonSingleValue = "[\"test\"]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonSingleValue);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(TypeFactory.defaultInstance.constructCollectionType(List.class, String.class), null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        List<String> expected = new ArrayList<>();
        expected.add("test");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeNullCollection() throws IOException {
        String jsonNull = "[null]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNull);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(TypeFactory.defaultInstance.constructCollectionType(List.class, String.class), null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeCollectionWithCustomDeserializer() throws IOException {
        String jsonCustomDeserializer = "[{\"name\":\"John\",\"age\":10}]";
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "custom";
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(TypeFactory.defaultInstance.constructCollectionType(List.class, String.class), null, null, stringDeserializer, null, null);
        Collection<String> result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonCustomDeserializer), objectMapper.getDeserializationContext());
        List<String> expected = new ArrayList<>();
        expected.add("custom");
        expected.add("custom"); // there should be two elements due to deserializing JSON objects
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeCollectionWithInvalidJson() throws IOException {
        String jsonInvalid = "{\"name\":\"John\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonInvalid);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(TypeFactory.defaultInstance.constructCollectionType(List.class, String.class), null, null, null, null, null);
        deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeCollectionWithNullInput() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(TypeFactory.defaultInstance.constructCollectionType(List.class, String.class), null, null, null, null, null);
        deserializer.deserialize(null, objectMapper.getDeserializationContext());
    }
}