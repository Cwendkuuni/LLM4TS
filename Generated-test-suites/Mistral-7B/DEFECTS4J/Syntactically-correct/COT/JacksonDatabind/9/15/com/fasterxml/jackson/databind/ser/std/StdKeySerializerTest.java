import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaGenerator;
import com.fasterxml.jackson.databind.jsonschema.SchemaPrinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class StdKeySerializerTest {

    @Test
    public void testSerialize() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();

        Object value = new Date();
        serializer.serialize(value, gen, provider);
        gen.flush();

        String expected = "\"\""; // Quotes are added by Jackson when serializing a Date
        String actual = new String(gen.getOutputBuffer().toString().getBytes(), "UTF-8");
        assertEquals(expected, actual);

        value = "testString";
        gen.reset(); // Reset the generator for the next test
        serializer.serialize(value, gen, provider);
        gen.flush();

        expected = "\"testString\"";
        actual = new String(gen.getOutputBuffer().toString().getBytes(), "UTF-8");
        assertEquals(expected, actual);
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeDateNull() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();

        Object value = null;
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException, IOException {
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator();
        JsonSchema schema = schemaGen.findSchemaForSerializer(serializer);

        SchemaPrinter printer = schemaGen.getPrinter();
        String schemaString = printer.printPretty(schema);

        String expected = "{\n" +
                "  \"title\": \"string\",\n" +
                "  \"type\": \"string\"\n" +
                "}";

        assertEquals(expected, schemaString);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {
            @Override
            public void reportGetFormatName(String name) throws JsonMappingException {
                assertEquals("string", name);
            }
        };

        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorNullTypeHint() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testSerializeMapEntryKey() throws IOException {
        Map.Entry<String, Integer> entry = Map.entry("testKey", 1);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonSerializer<Map.Entry<String, Integer>> entrySerializer = mapper.findValueSerializer(Map.Entry.class);
        entrySerializer.serialize(entry, gen, provider);

        String expected = "{\"key\":\"testKey\",\"value\":1}";
        String actual = new String(gen.getOutputBuffer().toString().getBytes(), "UTF-8");
        assertEquals(expected, actual);
    }
}