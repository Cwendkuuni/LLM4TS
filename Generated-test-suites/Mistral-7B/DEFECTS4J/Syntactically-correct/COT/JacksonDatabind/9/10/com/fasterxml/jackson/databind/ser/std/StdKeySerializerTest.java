import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mapper.getSerializationConfig().getProvider();

        Date date = new Date();
        String result = "";

        serializer.serialize(date, new JsonGenerator(new OutputStreamWriter(System.out)), provider);
        // Verify that the default serialization for Date is used instead
        assertFalse(serializer.isCached());

        // Test serialization with JsonGenerator
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, jsonGenerator, provider);
        result = jsonGenerator.getOutputSummarizer().toString();
        assertEquals("\"${ISO8601_DATE}\"", result);

        // Test serialization with ObjectMapper
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("key", date);
        String json = mapper.writeValueAsString(map);
        JsonNode node = mapper.readTree(json);
        assertEquals("key", node.get("key").asText());
        assertEquals("\"${ISO8601_DATE}\"", node.get("key").asText());
    }

    @Test
    public void testSerializeString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new SerializerProvider() {
            @Override
            public void defaultSerializeValue(Object value, JsonGenerator jgen) throws IOException {
                jgen.writeNull();
            }
        };

        String str = "test";
        serializer.serialize(str, null, provider);

        assertEquals("test", serializer.getSerializationShift());
        assertEquals("test", serializer.getSerializationFeature("FAIL_ON_EMPTY_BEANS", false) ? serializer.getSerializationShift() : serializer.getValueAsString(null, null, null, null, null, null, null, null, null));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        assertEquals("string", serializer.getSchema(null, null).toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorWithNullTypeHint() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithValidTypeHint() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper() {}, new JavaType(String.class));
    }
}