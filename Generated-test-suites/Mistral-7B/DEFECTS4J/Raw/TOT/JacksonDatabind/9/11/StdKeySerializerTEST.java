import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

    private ObjectMapper objectMapper = JsonMapperFactory.getMapper();

    @Test
    public void testSerializeDate() throws JsonGenerationException, IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = objectMapper.getSerializerProvider();

        Date date = new Date();
        String expected = "\"" + date.toISO8601String() + "\"";

        String result = serializeAndGetKey(date, serializer, provider);
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = objectMapper.getSerializerProvider();

        String value = "test";
        String expected = "\"" + value + "\"";

        String result = serializeAndGetKey(value, serializer, provider);
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = objectMapper.getSerializerProvider();

        String expected = "null";

        String result = serializeAndGetKey(null, serializer, provider);
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeComplexObject() throws JsonGenerationException, IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = objectMapper.getSerializerProvider();

        Map<String, Object> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", new Date());

        String expected = "{\"key1\":1,\"key2\":\"" + new Date().toISO8601String() + "\"}";

        String result = serializeAndGetKey(map, serializer, provider);
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidType() throws JsonGenerationException, IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = objectMapper.getSerializerProvider();

        int value = 1;

        String result = serializeAndGetKey(value, serializer, provider);

        // This test will throw an exception as the serializer only accepts Object type
    }

    private String serializeAndGetKey(Object value, StdKeySerializer serializer, SerializerProvider provider) throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jgen = provider.getFactory().createGenerator(stringWriter);
        serializer.serialize(value, jgen, provider);
        jgen.close();
        return stringWriter.toString();
    }

    @Test
    public void testGetSchema() {
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectNode expectedSchema = objectMapper.createObjectNode();
        expectedSchema.put("type", "string");

        ObjectNode resultSchema = serializer.getSchema(objectMapper.getSerializerProvider(), Object.class.getType());

        assertEquals(expectedSchema, resultSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = objectMapper.getPolymorphicSerializerProvider().findRootValueSerializer(String.class);

        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());

        assertEquals(visitor.expectStringFormat(Object.class.getType()), visitor);
    }
}