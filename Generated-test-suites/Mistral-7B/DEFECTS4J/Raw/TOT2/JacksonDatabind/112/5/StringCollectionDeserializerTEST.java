import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyStringCollectionDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[ ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);
        List<String> result = deserializer.deserialize(parser, new DeserializationContext(null, null), Collections.emptyList());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testStringCollectionDeserializationWithSingleValue() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"test\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);
        List<String> result = deserializer.deserialize(parser, new DeserializationContext(null, null), Collections.emptyList());
        assertEquals(Collections.singletonList("test"), result);
    }

    @Test
    public void testStringCollectionDeserializationWithMultipleValues() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"test1\",\"test2\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);
        List<String> result = deserializer.deserialize(parser, new DeserializationContext(null, null), Collections.emptyList());
        assertEquals(Collections.singletonList(Collections.singletonList("test1", "test2")), result);
    }
}