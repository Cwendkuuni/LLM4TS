import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mapper.getSerializerProvider();

        Date date = new Date();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, jgen, provider);
        jgen.flush();

        String expected = "\"2022-12-31T13:59:59.000Z\""; // A date in the future for testing purposes
        String actual = jgen.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeString() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        String value = "testString";

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, jgen, mapper.getSerializerProvider());
        jgen.flush();

        String actual = jgen.toString();
        String expected = "\"testString\"";

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectNode schema = serializer.getSchema(new SerializerProvider(), Object.class.getType());

        String expectedType = "string";
        String actualType = schema.get("type").asText();

        assertEquals(expectedType, actualType);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNull() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(null, jgen, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNonStringOrDate() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        int value = 1;

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, jgen, mapper.getSerializerProvider());
    }
}